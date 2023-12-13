package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class TelaIntro01 extends AppCompatActivity {

    private ImageButton botaoProximaTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_intro01);

        botaoProximaTela = (ImageButton) findViewById(R.id.botaoProximaTela);

        botaoProximaTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TelaIntro01.this, TelaIntro02.class);
                startActivity(intent);
            }
        });
    }
}
