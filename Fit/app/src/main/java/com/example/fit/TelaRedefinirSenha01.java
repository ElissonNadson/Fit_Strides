package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaRedefinirSenha01 extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha01);
    }

    // Método chamado quando o botão Voltar é clicado
    public void voltarOnClick(View view) {
        // Navega de volta para a tela de login
        startActivity(new Intent(this, FormLogin.class));
    }

    // Método chamado quando o botão Redefinir Senha é clicado
    public void onredefinirsenhaClicked(View view) {
        // Navega para a próxima tela de redefinição de senha
        startActivity(new Intent(this, TelaRedefinirSenha02.class));
    }
}
