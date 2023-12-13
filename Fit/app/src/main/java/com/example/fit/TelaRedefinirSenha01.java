package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha01);

        editEmail = findViewById(R.id.caixadeemail);
        btVoltar = findViewById(R.id.bt_voltar);
        btRedefinirSenha = findViewById(R.id.bt_redefinirsenha2);

        mAuth = FirebaseAuth.getInstance();

        btRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Fecha a tela atual e volta para a tela anterior
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
                // Após enviar o e-mail você pode querer redirecionar o usuário para a tela de login
                // ou dar a opção dele verificar o e-mail e digitar a nova senha.
            } else {
                Toast.makeText(TelaRedefinirSenha01.this,
                        "Falha ao enviar e-mail de redefinição de senha.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}