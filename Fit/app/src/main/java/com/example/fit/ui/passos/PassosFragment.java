package com.example.fit.ui.passos;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.fit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PassosFragment extends Fragment implements LocationListener {

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

    // Variáveis adicionadas para Firebase
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passos, container, false);

        totalPassos = view.findViewById(R.id.idTotalPassos);
        totalCalorias = view.findViewById(R.id.idCaloria);
        btnReset = view.findViewById(R.id.idReset);

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }

        // Inicializa Firebase
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passos = 0;
                totalPassos.setText("0");
                calorias = 0;
                totalCalorias.setText("0");
            }
        });

        return view;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        double speed = location.getSpeed() * 3.6;

        if (speed <= MAX_SPEED_KMH) {
            long currentTime = System.currentTimeMillis();
            if (speed > 1.4 && currentTime - lastStepTime > STEP_DELAY) {
                lastStepTime = currentTime;

                passos++;
                totalPassos.setText(String.valueOf(passos));

                calorias = passos / 20.0;
                totalCalorias.setText(String.format("%.2f", calorias));
            }
        }

        // Adicionado código para enviar dados para o Firebase
        sendUserDataToFirebase();
    }

    private void sendUserDataToFirebase() {
        // Verifica se o usuário está autenticado
        if (user != null) {
            // Cria um mapa com os dados
            Map<String, Object> userData = new HashMap<>();
            userData.put("passos", passos);
            userData.put("calorias", calorias);

            // Cria uma referência ao documento no Firebase
            String idDoc = String.valueOf(new Date().getTime());
            DocumentReference dr = db.collection("Users").document(user.getUid()).collection("Pedometro").document(idDoc);

            // Adiciona os dados ao Firebase
            dr.set(userData);
        } else {
            Toast.makeText(requireContext(), "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        locationManager.removeUpdates(this);
    }
}
