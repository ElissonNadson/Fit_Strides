package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaRedefinirSenha03 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha03);
    }

    // Método chamado quando o botão Voltar é clicado
    public void voltarOnClick(View view) {
        // Navega de volta para a tela anterior de redefinição de senha
        startActivity(new Intent(this, FormLogin.class));
    }

    // Método chamado quando o botão Redefinir Senha é clicado
    public void onResetPasswordClicked(View view) {
        // Aqui você pode adicionar a lógica para redefinir a senha do usuário
        // Por exemplo, validar as senhas, atualizar no banco de dados, etc.
        // Após a redefinição bem-sucedida, você pode navegar para a tela de login ou home
        startActivity(new Intent(this, TelaRedefinirSenha04.class)); // ou outra tela conforme necessário
    }
}
