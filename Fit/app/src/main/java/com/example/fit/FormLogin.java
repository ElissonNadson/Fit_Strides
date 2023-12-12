package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FormLogin extends AppCompatActivity {

    private Button bt_entrar, bt_redefinirSenha, bt_register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        // Inicializa os botões
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_redefinirSenha = findViewById(R.id.bt_redefinirSenha);
        bt_register = findViewById(R.id.bt_register);

        // Define os ouvintes de clique para os botões
        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a tela Home
                startActivity(new Intent(FormLogin.this, home.class));
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
}
