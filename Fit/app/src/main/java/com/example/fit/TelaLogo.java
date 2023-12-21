package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TelaLogo extends AppCompatActivity {

    private static final int TEMPO_DE_CARREGAMENTO = 3000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_logo);

        // Inicializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Se o usuário estiver logado, vá direto para a Tela Principal
                    Intent intent = new Intent(TelaLogo.this, TelaPrincipal.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Se não estiver logado, continue com a tela de introdução
                    Intent intent = new Intent(TelaLogo.this, TelaIntro01.class);
                    startActivity(intent);
                }
            }
        }, TEMPO_DE_CARREGAMENTO);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica novamente se o usuário está conectado no onStart em caso de mudança de estado
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(TelaLogo.this, TelaPrincipal.class));
            finish();
        }
    }
}