// Importações necessárias
package com.example.fit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Classe principal do pedômetro
public class pedometro extends AppCompatActivity implements LocationListener {

    // Elementos da interface do usuário
    private TextView totalPassos;
    private TextView totalDistancia;
    private TextView totalCalorias;
    private ImageButton btnReset;

    // Variáveis para rastreamento de dados
    private int passos = 0;
    private double distanciaTotal = 0;
    private double calorias = 0;

    // Gerenciador de localização
    private LocationManager locationManager;
    private Location startPoint;

    // Constantes utilizadas no cálculo
    private static final double AVERAGE_STRIDE_LENGTH = 0.762;  // Tamanho de passada médio em metros
    private static final double MIN_SPEED_KMH = 1.4;  // Velocidade mínima em km/h
    private static final double MAX_SPEED_KMH = 30.0;  // Velocidade máxima em km/h
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Constantes adicionadas
    private static final int LOCATION_UPDATE_INTERVAL = 1000;  // Intervalo de atualização de localização em milissegundos
    private static final double VELOCITY_THRESHOLD = 2.0;  // Limiar de velocidade para filtrar movimentos rápidos
    private static final double DIRECTION_CHANGE_THRESHOLD = 30.0;  // Limiar de mudança de direção para considerar um passo

    // Variável para armazenar o último timestamp de localização
    private long lastLocationTimestamp = 0;

    // Método chamado na criação da atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometro);

        // Inicializa os elementos da interface
        initializeUI();

        // Verifica e solicita permissões de localização
        checkAndRequestLocationPermissions();

        // Configura o botão de reset para chamar a função resetValues()
        btnReset.setOnClickListener(v -> sendUserDataToFirebase());
    }

    // Inicializa os elementos da interface do usuário
    private void initializeUI() {
        totalPassos = findViewById(R.id.idTotalPassos);
        totalDistancia = findViewById(R.id.idDistancia);
        totalCalorias = findViewById(R.id.idCaloria);
        btnReset = findViewById(R.id.idReset);
    }

    // Verifica e solicita permissões de localização
    private void checkAndRequestLocationPermissions() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verifica se o GPS está habilitado, caso contrário, abre as configurações
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        // Solicita permissões se não estiverem concedidas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Inicia as atualizações de localização
            startLocationUpdates();
        }
    }

    // Reseta os valores de passos, distância e calorias
    private void resetValues() {
        passos = 0;
        distanciaTotal = 0;
        calorias = 0;
        updateDisplay();
    }

    // Atualiza a exibição na interface do usuário
    @SuppressLint("DefaultLocale")
    private void updateDisplay() {
        totalPassos.setText(String.valueOf(passos));
        totalDistancia.setText(String.format("%.2f", distanciaTotal));
        totalCalorias.setText(String.format("%.2f", calorias));
    }

    // Função chamada após a solicitação de permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verifica se a permissão de localização foi concedida
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Inicia as atualizações de localização
                startLocationUpdates();
            } else {
                // Exibe um aviso se a permissão não foi concedida
                Toast.makeText(this, "Permissão de localização não concedida. O aplicativo pode não funcionar corretamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Inicia as atualizações de localização
    private void startLocationUpdates() {
        // Registra o ouvinte de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    this
            );
        }
    }

    // Função chamada quando a localização é alterada
    @Override
    public void onLocationChanged(Location location) {
        long currentTimestamp = System.currentTimeMillis();

        // Verifica se é a primeira localização ou se o intervalo de atualização foi atingido
        if (startPoint == null || currentTimestamp - lastLocationTimestamp >= LOCATION_UPDATE_INTERVAL) {
            // Calcula a velocidade em km/h
            double speed = location.getSpeed() * 3.6;  // Convertendo de m/s para km/h

            // Verifica se a velocidade está dentro da faixa desejada e a mudança de direção é significativa
            if (speed > VELOCITY_THRESHOLD && isDirectionChangeSignificant(location)) {
                // Calcula a distância entre os pontos inicial e atual
                distanciaTotal += startPoint.distanceTo(location) / AVERAGE_STRIDE_LENGTH;
                startPoint = location;

                // Atualiza a quantidade de passos apenas no final do percurso
                if (speed <= MIN_SPEED_KMH || speed >= MAX_SPEED_KMH) {
                    passos = calcularPassos(distanciaTotal);
                    updateDisplay();
                    distanciaTotal = 0;
                }

                // Atualiza o último timestamp de localização
                lastLocationTimestamp = currentTimestamp;
            }
        }
        calorias = passos / 20.0;
    }

    // Calcula a quantidade de passos com base na distância
    private int calcularPassos(double distancia) {
        return (int) (distancia / AVERAGE_STRIDE_LENGTH);
    }

    // Verifica se a mudança de direção é significativa
    private boolean isDirectionChangeSignificant(Location currentLocation) {
        if (startPoint == null) {
            return false;
        }

        double bearingChange = Math.abs(currentLocation.bearingTo(startPoint));

        // Verifica se a mudança de direção é maior que o limiar
        return bearingChange >= DIRECTION_CHANGE_THRESHOLD;
    }

    private void sendUserDataToFirebase() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("passos", passos);
        userData.put("distancia", distanciaTotal);
        userData.put("calorias", calorias);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idDoc = String.valueOf(new Date().getTime());

        if (user != null) {
            DocumentReference dr = db.collection("Users").document(user.getUid()).collection("Pedometro").document(idDoc);
            dr.set(userData)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Dados enviados para o Firebase!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Erro ao enviar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getApplicationContext(), "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
        }
        resetValues();
    }

    // Métodos obrigatórios da interface LocationListener, mas não utilizados neste caso
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    // Função chamada ao destruir a atividade, remove o ouvinte de localização
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }
}

