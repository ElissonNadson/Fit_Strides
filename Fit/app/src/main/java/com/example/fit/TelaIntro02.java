package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class TelaIntro02 extends AppCompatActivity {

    private ImageButton botaoProximaTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_intro02);

        botaoProximaTela = (ImageButton) findViewById(R.id.botaoProximaTela);

        botaoProximaTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TelaIntro02.this, FormLogin.class);
                startActivity(intent);
            }
        });
    }
}
