package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaRedefinirSenha04 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha04);
    }

    // Método chamado quando o botão Fazer Login é clicado
    public void onLoginClicked(View view) {
        // Navega para a tela de login
        startActivity(new Intent(this, FormLogin.class));
    }
}
