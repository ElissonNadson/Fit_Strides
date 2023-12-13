package com.example.fit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaCadastro02 extends AppCompatActivity {

    private static final String TAG = "TelaCadastro02";

    private EditText ageInput, heightInput, weightInput;
    private Button btnFinalizarCadastro;
    private ImageButton btnVoltar;

    // Variáveis para armazenar os dados recebidos da TelaCadastro01
    private String nome, email, senha, genero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro02);
        Log.d(TAG, "onCreate da TelaCadastro02 iniciado.");

        // Inicializa os componentes e obtém os dados passados pela Intent.
        initComponents();

        // Define os listeners dos botões
        btnVoltar.setOnClickListener(v -> finish());
        btnFinalizarCadastro.setOnClickListener(v -> finalizarCadastro());
    }

    private void initComponents() {
        ageInput = findViewById(R.id.age_input);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        btnVoltar = findViewById(R.id.bt_voltar);
        btnFinalizarCadastro = findViewById(R.id.botao_finalizar_cadastro);

        // Recupera as informações das Intent extras passadas pela TelaCadastro01
        Intent intent = getIntent();
        nome = intent.getStringExtra("NOME");
        email = intent.getStringExtra("EMAIL");
        senha = intent.getStringExtra("SENHA");
        genero = intent.getStringExtra("GENERO");

        Log.d(TAG, "Componentes inicializados.");
    }

    private void finalizarCadastro() {
        Log.d(TAG, "Botão de finalizar cadastro pressionado.");
        if (validateInputs()) {
            salvarDadosUsuario();
        } else {
            Log.d(TAG, "Falha na validação dos inputs.");
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

    private void salvarDadosUsuario() {
        Log.d(TAG, "Iniciando o método salvarDadosUsuario().");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String age = ageInput.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();

        // Cria o objeto User com os dados obtidos da tela anterior
        User user = new User(nome, email, senha, genero, age, height, weight);

        // Salva o objeto do usuário no Firestore
        db.collection("Users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Dados do usuário salvos com sucesso.");
                    Toast.makeText(TelaCadastro02.this, "Cadastro concluído com sucesso.", Toast.LENGTH_SHORT).show();
                    irParaTelaDeLogin();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Falha ao salvar dados: " + e.toString());
                    Toast.makeText(TelaCadastro02.this, "Falha ao salvar dados do usuário.", Toast.LENGTH_SHORT).show();
                });
    }

    private void irParaTelaDeLogin() {
        Log.d(TAG, "Preparando para transicionar para a tela de login (FormLogin).");
        Intent intent = new Intent(TelaCadastro02.this, FormLogin.class);
        startActivity(intent);
        Log.d(TAG, "A transição para FormLogin foi iniciada. Finalizando TelaCadastro02.");
        finish();
    }
}