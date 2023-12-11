package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaRedefinirSenha02 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha02);
    }

    // Método chamado quando o botão Voltar é clicado
    public void voltarOnClick(View view) {
        // Navega de volta para a tela anterior de redefinição de senha
        startActivity(new Intent(this, TelaRedefinirSenha01.class));
    }

    // Método chamado quando o botão Verificar Código é clicado
    public void onVerifyCodeClicked(View view) {
        // Navega para a próxima tela de redefinição de senha
        startActivity(new Intent(this, TelaRedefinirSenha03.class));
    }
}
