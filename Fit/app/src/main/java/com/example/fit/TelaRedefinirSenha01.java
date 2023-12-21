package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TelaRedefinirSenha01 extends AppCompatActivity {

    private EditText editEmail;
    private ImageButton btVoltar;
    private Button btRedefinirSenha;
    private Button btFazerLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha01);

        initViews();
        setupFirebaseAuth();
        setupListeners();
    }

    private void initViews() {
        editEmail = findViewById(R.id.caixadeemail);
        btVoltar = findViewById(R.id.bt_voltar);
        btRedefinirSenha = findViewById(R.id.bt_redefinirsenha2);
        btFazerLogin = findViewById(R.id.bt_proceed_to_login); // Inicialização do novo botão
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupListeners() {
        btRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btFazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaRedefinirSenha01.this, FormLogin.class));
                finish();
            }
        });
    }

    private void sendPasswordResetEmail() {
        String email = editEmail.getText().toString().trim();

        if (email.isEmpty()) {
            editEmail.setError("Por favor, insira um e-mail.");
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TelaRedefinirSenha01.this,
                        "Instruções para redefinição de senha foram enviadas para seu e-mail.",
                        Toast.LENGTH_LONG).show();


                btRedefinirSenha.setVisibility(View.GONE);
                btFazerLogin.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(TelaRedefinirSenha01.this,
                        "Falha ao enviar e-mail de redefinição de senha.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}