package com.example.fit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaCadastro02 extends AppCompatActivity {

    private EditText ageInput, heightInput, weightInput;
    private Button btnFinalizarCadastro;
    private ImageButton btnVoltar, ageIncrease, ageDecrease, heightIncrease, heightDecrease, weightIncrease, weightDecrease;
    private final Handler handler = new Handler();
    private String nome, email, senha, genero;
    private boolean isAgeValid = true, isHeightValid = true, isWeightValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro02);

        initComponents();
        setButtonListeners();

        // Inicialização segura com valores padrão, evitando strings vazias
        ageInput.setText("0");
        heightInput.setText("1.50");
        weightInput.setText("50.0");
    }

    private void initComponents() {
        // Incialização dos componentes da tela
        ageInput = findViewById(R.id.age_input);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        btnVoltar = findViewById(R.id.bt_voltar);
        btnFinalizarCadastro = findViewById(R.id.botao_finalizar_cadastro);
        ageIncrease = findViewById(R.id.age_increase);
        ageDecrease = findViewById(R.id.age_decrease);
        heightIncrease = findViewById(R.id.height_increase);
        heightDecrease = findViewById(R.id.height_decrease);
        weightIncrease = findViewById(R.id.weight_increase);
        weightDecrease = findViewById(R.id.weight_decrease);

        // Recuperando os dados da Intent
        Intent intent = getIntent();
        nome = intent.getStringExtra("NOME");
        email = intent.getStringExtra("EMAIL");
        senha = intent.getStringExtra("SENHA");
        genero = intent.getStringExtra("GENERO");
    }

    private void setButtonListeners() {
        // Configuração dos ouvintes para os botões da tela
        btnVoltar.setOnClickListener(v -> finish());
        btnFinalizarCadastro.setOnClickListener(v -> finalizarCadastro());

        // Incremento de idade com validação
        setupContinuousAdjust(ageIncrease, () -> adjustAge(true));
        setupContinuousAdjust(ageDecrease, () -> adjustAge(false));

        // Incremento de altura com validação
        setupContinuousAdjust(heightIncrease, () -> adjustHeight(true));
        setupContinuousAdjust(heightDecrease, () -> adjustHeight(false));

        // Incremento de peso com validação
        setupContinuousAdjust(weightIncrease, () -> adjustWeight(true));
        setupContinuousAdjust(weightDecrease, () -> adjustWeight(false));
    }

    private void setupContinuousAdjust(View view, Runnable action) {
        // Configuração de ajuste contínuo para os botões
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (isAgeValid && isHeightValid && isWeightValid) {
                        handler.post(action);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(action);
                    break;
            }
            return true;
        });
    }

    private void finalizarCadastro() {
        // Finaliza o cadastro e valida os inputs
        if (validateInputs()) {
            salvarDadosUsuario();
        }
    }

    private boolean validateInputs() {
        try {
            int age = Integer.parseInt(ageInput.getText().toString());
            if (age < 0 | age > 200) {
                Toast.makeText(this, "Idade deve estar entre 0 e 200 anos.", Toast.LENGTH_SHORT).show();
                return false;
            }

            float height = Float.parseFloat(heightInput.getText().toString());
            if (height <= 0) {
                Toast.makeText(this, "Informe uma altura válida.", Toast.LENGTH_SHORT).show();
                return false;
            }

            float weight = Float.parseFloat(weightInput.getText().toString());
            if (weight <= 0 | weight > 500) {
                Toast.makeText(this, "Peso deve estar entre 0 e 500 kg.", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Verifique os dados inseridos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void adjustAge(boolean increase) {
        // Ajuste da idade com validação
        int age = Integer.parseInt(ageInput.getText().toString());
        if (increase && age < 200) {
            age++;
        } else if (!increase && age > 0) {
            age--;
        } else {
            isAgeValid = false;
            showToast("Idade inválida. Deve ser entre 0 e 200 anos.");
            return;
        }
        ageInput.setText(String.valueOf(age));
    }

    private void adjustHeight(boolean increase) {
        float height = Float.parseFloat(heightInput.getText().toString());
        if (increase) {
            if (height >= 3.00f) {
                showToast("Altura inválida. Não pode ser maior que 3 metros.");
                return; // Sai do método sem fazer ajustes, pois já atingiu o limite máximo
            }
            height += 0.01f;
        } else {
            if (height <= 0.50f) {
                showToast("Altura inválida. Não pode ser menor que 0.50 metros.");
                return; // Sai do método sem fazer ajustes, pois já atingiu o limite mínimo
            }
            height -= 0.01f;
        }
        heightInput.setText(String.format("%.2f", height));
    }
    private void adjustWeight(boolean increase) {
        // Ajuste do peso com validação
        float weight = Float.parseFloat(weightInput.getText().toString());
        if (increase && weight < 500.0f) {
            weight += 0.01f;
        } else if (!increase && weight > 0.0f) {
            weight -= 0.01f;
        } else {
            isWeightValid = false;
            showToast("Peso inválido. Deve ser entre 0 e 500 kg.");
            return;
        }
        weightInput.setText(String.format("%.2f", weight));
    }

    private void showToast(String message) {
        Toast.makeText(TelaCadastro02.this, message, Toast.LENGTH_SHORT).show();
    }

    private void salvarDadosUsuario() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String age = ageInput.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();

        User user = new User(nome, email, senha, genero, age, height, weight);

        db.collection("Users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TelaCadastro02.this, "Cadastro concluído com sucesso.", Toast.LENGTH_SHORT).show();
                    irParaTelaDeLogin();
                })
                .addOnFailureListener(e -> Toast.makeText(TelaCadastro02.this, "Falha ao salvar dados do usuário.", Toast.LENGTH_SHORT).show());
    }

    private void irParaTelaDeLogin() {
        startActivity(new Intent(TelaCadastro02.this, FormLogin.class));
        finish();
    }
}



