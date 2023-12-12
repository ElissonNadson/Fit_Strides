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

    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro02);

        ageInput = findViewById(R.id.age_input);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        btnVoltar = findViewById(R.id.bt_voltar);
        btnContinuar = findViewById(R.id.continue_button);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton ageIncrease = findViewById(R.id.age_increase);
        ImageButton ageDecrease = findViewById(R.id.age_decrease);

        // Defina os listeners para ageIncrease e ageDecrease
        // ...

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaTelaCadastro03();
            }
        });
    }

    private void irParaTelaCadastro03() {
        String age = ageInput.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();

        if (age.isEmpty()) {
            Toast.makeText(TelaCadastro02.this, "Informe sua idade.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (height.isEmpty()) {
            Toast.makeText(TelaCadastro02.this, "Informe sua altura.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weight.isEmpty()) {
            Toast.makeText(TelaCadastro02.this, "Informe seu peso.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(TelaCadastro02.this, TelaCadastro03.class);
        intent.putExtras(getIntent().getExtras()); // Inclui as informações anteriores
        intent.putExtra("IDADE", age);
        intent.putExtra("ALTURA", height);
        intent.putExtra("PESO", weight);
        startActivity(intent);
    }

    // Implemente os métodos para incrementar e decrementar a idade, altura e peso
    // ...
}