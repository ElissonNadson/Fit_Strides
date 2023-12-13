package com.example.fit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastro02 extends AppCompatActivity {

    private EditText ageInput, heightInput, weightInput;
    private Button btnContinuar;
    private ImageButton btnVoltar, ageIncrease, ageDecrease, heightIncrease, heightDecrease, weightIncrease, weightDecrease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro02);

        initComponents();

        btnVoltar.setOnClickListener(v -> finish());

        ageIncrease.setOnClickListener(v -> adjustInputByOne(ageInput, true));
        ageDecrease.setOnClickListener(v -> adjustInputByOne(ageInput, false));
        heightIncrease.setOnClickListener(v -> adjustInputByOne(heightInput, true));
        heightDecrease.setOnClickListener(v -> adjustInputByOne(heightInput, false));
        weightIncrease.setOnClickListener(v -> adjustInputByOne(weightInput, true));
        weightDecrease.setOnClickListener(v -> adjustInputByOne(weightInput, false));

        btnContinuar.setOnClickListener(v -> goToTelaCadastro03());
    }

    private void initComponents() {
        // Conectar inputs e botões com os ids do layout
        ageInput = findViewById(R.id.age_input);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        btnVoltar = findViewById(R.id.bt_voltar);
        btnContinuar = findViewById(R.id.continue_button);

        ageIncrease = findViewById(R.id.age_increase);
        ageDecrease = findViewById(R.id.age_decrease);
        heightIncrease = findViewById(R.id.height_increase);
        heightDecrease = findViewById(R.id.height_decrease);
        weightIncrease = findViewById(R.id.weight_increase);
        weightDecrease = findViewById(R.id.weight_decrease);
    }

    private void adjustInputByOne(EditText input, boolean increase) {
        int currentValue = 0;
        if (!input.getText().toString().isEmpty()) {
            currentValue = Integer.parseInt(input.getText().toString());
        }
        if (increase) {
            currentValue++;
        } else {
            currentValue = (currentValue > 0) ? currentValue - 1 : 0;
        }
        input.setText(String.valueOf(currentValue));
    }

    private void goToTelaCadastro03() {
        if (validateInputs()) {
            String age = ageInput.getText().toString();
            String height = heightInput.getText().toString();
            String weight = weightInput.getText().toString();

            Intent intent = new Intent(TelaCadastro02.this, TelaCadastro03.class);
            intent.putExtras(getIntent().getExtras()); // Passando os dados recebidos
            intent.putExtra("IDADE", age);
            intent.putExtra("ALTURA", height);
            intent.putExtra("PESO", weight);
            startActivity(intent);
        }
    }

    private boolean validateInputs() {
        if (ageInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informe sua idade.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (heightInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informe sua altura.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (weightInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informe seu peso.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}