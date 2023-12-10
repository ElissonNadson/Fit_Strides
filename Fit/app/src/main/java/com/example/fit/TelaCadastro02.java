package com.example.fit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastro02 extends AppCompatActivity {

    private EditText ageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro02);

        ageInput = findViewById(R.id.age_input);
        ImageButton ageIncrease = findViewById(R.id.age_increase);
        ImageButton ageDecrease = findViewById(R.id.age_decrease);

        ageIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementAge();
            }
        });

        ageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementAge();
            }
        });
    }

    private void incrementAge() {
        int currentAge = getCurrentAge();
        setCurrentAge(currentAge + 1);
    }

    private void decrementAge() {
        int currentAge = getCurrentAge();
        setCurrentAge(currentAge - 1);
    }

    private int getCurrentAge() {
        String ageString = ageInput.getText().toString();
        if (!ageString.isEmpty()) {
            return Integer.parseInt(ageString);
        } else {
            return 0; // Pode definir um valor padrão ou lidar com isso de acordo com a sua lógica.
        }
    }

    private void setCurrentAge(int age) {
        ageInput.setText(String.valueOf(age));
    }

    // Adicione o resto da sua lógica conforme necessário...
}
