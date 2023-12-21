package com.example.fit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class pedometro extends AppCompatActivity implements LocationListener {

    private TextView totalPassos;
    private Button btnReset;
    private TextView totalCalorias;

    private int passos = 0;
    private double calorias;

    private LocationManager locationManager;

    private static final int STEP_DELAY = 500;
    private long lastStepTime = 0;
    private static final double MAX_SPEED_KMH = 30.0;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_pedometro);
        } else {
            setContentView(R.layout.activity_pedometro);
        }

        totalPassos = findViewById(R.id.idTotalPassos);
        totalCalorias = findViewById(R.id.idCaloria);
        btnReset = findViewById(R.id.idReset);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verifica se o GPS está habilitado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        // Solicitar permissões de localização se não estiverem concedidas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passos = 0;
                totalPassos.setText("0");
                calorias = 0;
                totalCalorias.setText("0");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permissão de localização não concedida. O aplicativo pode não funcionar corretamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    @Override
    public void onLocationChanged(Location location) {
        // Calcula a velocidade com base na mudança na posição e no tempo
        double speed = location.getSpeed(); // velocidade em metros/segundo

        // Convertendo a velocidade para km/h
        speed *= 3.6;

        // Verifica se a velocidade está abaixo da velocidade máxima permitida
        if (speed <= MAX_SPEED_KMH) {
            // Verifica se houve um passo baseado na velocidade e no intervalo mínimo
            long currentTime = System.currentTimeMillis();
            if (speed > 1.4 && currentTime - lastStepTime > STEP_DELAY) {
                lastStepTime = currentTime;

                passos++;
                totalPassos.setText(String.valueOf(passos));

                calorias = passos / 20.0; // Considerando um cálculo de calorias mais preciso
                totalCalorias.setText(String.format("%.2f", calorias));
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove o ouvinte de localização ao encerrar a atividade
        locationManager.removeUpdates(this);
    }
}
