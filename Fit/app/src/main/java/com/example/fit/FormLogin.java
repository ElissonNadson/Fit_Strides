package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button bt_entrar, bt_redefinirSenha, bt_register;
    private CheckBox rememberMe;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth; // Adicionado para autenticação do Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        // Inicializamos o FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializa os componentes
        progressBar = findViewById(R.id.progressBar);
        editEmail = findViewById(R.id.edit_Email);
        editSenha = findViewById(R.id.edit_Password);
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_redefinirSenha = findViewById(R.id.bt_redefinirSenha);
        bt_register = findViewById(R.id.bt_register);

        // Ouvintes de clique para os botões
        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        bt_redefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a tela de redefinição de senha
                startActivity(new Intent(FormLogin.this, TelaRedefinirSenha01.class));
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a tela de cadastro
                startActivity(new Intent(FormLogin.this, TelaCadastro01.class));
            }
        });
    }

    private void loginUser() {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (email.isEmpty()) {
            editEmail.setError("E-mail é obrigatório.");
            return;
        }

        if (senha.isEmpty()) {
            editSenha.setError("Senha é obrigatória.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Aqui acontece o processo de login utilizando o Firebase
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(FormLogin.this, "Login bem-sucedido.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FormLogin.this, home.class));
                        finish();
                    } else {
                        Toast.makeText(FormLogin.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}