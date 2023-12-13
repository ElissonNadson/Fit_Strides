package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class TelaRedefinirSenha02 extends AppCompatActivity {

    private Button btFazerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha02);

        btFazerLogin = findViewById(R.id.button_fazer_login);

        btFazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaRedefinirSenha02.this, FormLogin.class));
                finish();
            }
        });
    }
}