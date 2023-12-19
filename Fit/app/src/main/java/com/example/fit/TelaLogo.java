package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class TelaLogo extends AppCompatActivity {

    private static final int TEMPO_DE_CARREGAMENTO = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(TelaLogo.this, TelaIntro01.class);
                startActivity(intent);
            }
        }, TEMPO_DE_CARREGAMENTO);
    }
}
