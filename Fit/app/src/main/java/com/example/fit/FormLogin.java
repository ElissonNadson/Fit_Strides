package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fit.databinding.ActivityFormLoginBinding;

public class FormLogin extends AppCompatActivity {

    private ActivityFormLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navega para a página Home ao clicar no botão Entrar
        binding.btEntrar.setOnClickListener(v -> {
            startActivity(new Intent(this, home.class));
        });

        // Navega para a página de redefinição de senha ao clicar no botão Redefinir senha
        binding.btRedefinirsenha.setOnClickListener(v -> {
            startActivity(new Intent(this, TelaRedefinirSenha01.class));
        });

        // Navega para a página de cadastro ao clicar no botão Registre-se
        binding.btRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, TelaCadastro01.class));
        });
    }
}
