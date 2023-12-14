package com.example.fit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class pedometro extends AppCompatActivity implements SensorEventListener {

    private TextView totalPassos;
    private Button btnReset;
    private TextView totalCalorias;

    private int passos = 0;
    private double calorias;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private static final int ACCELEROMETER_SENSITIVITY = 8; // Ajuste conforme necessário
    private static final int STEP_DELAY = 500; // Intervalo mínimo entre passos em milissegundos
    private long lastStepTime = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica a orientação do dispositivo
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_pedometro);
        } else {
            setContentView(R.layout.activity_pedometro); // Use o layout de paisagem
        }

        setContentView(R.layout.activity_pedometro);

        totalPassos = findViewById(R.id.idTotalPassos);
        totalCalorias = findViewById(R.id.idCaloria);
        btnReset = findViewById(R.id.idReset);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, mAccelerometer);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            // Verifica se houve um passo baseado na mudança de aceleração e no intervalo mínimo
            long currentTime = System.currentTimeMillis();
            if (acceleration > ACCELEROMETER_SENSITIVITY &&
                    currentTime - lastStepTime > STEP_DELAY) {
                lastStepTime = currentTime;

                passos++;
                totalPassos.setText(String.valueOf(passos));

                calorias = passos / 20.0; // Considerando um cálculo de calorias mais preciso
                totalCalorias.setText(String.format("%.2f", calorias));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Seu código para lidar com mudanças na precisão do sensor (se necessário)
    }
}
