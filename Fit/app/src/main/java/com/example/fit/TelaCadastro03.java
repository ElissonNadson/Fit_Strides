package com.example.fit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastro03 extends AppCompatActivity {

    // Incluir elementos para seleção de fotos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro03);

        // Implementar captura ou seleção de fotos e salvar no Firebase
        // ...

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nome = extras.getString("NOME");
            String email = extras.getString("EMAIL");
            String senha = extras.getString("SENHA");
            String genero = extras.getString("GENERO");
            String idade = extras.getString("IDADE");
            String altura = extras.getString("ALTURA");
            String peso = extras.getString("PESO");
            // Incluir lógica para capturar a foto e salvar todos os dados no Firebase
        }
    }

    // Precisará de um método para salvar os dados no Firebase e fazer upload das fotos para o Firebase Storage
    // ...
}