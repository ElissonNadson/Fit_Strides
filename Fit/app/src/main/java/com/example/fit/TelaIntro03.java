package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;  // Importe a classe correta

public class TelaIntro03 extends AppCompatActivity {

    private Button botaoProximaTela;  // Corrija o tipo para Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_intro03);

        botaoProximaTela = (Button) findViewById(R.id.botaoProximaTela);  // Corrija o tipo para Button

        botaoProximaTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TelaIntro03.this, FormLogin.class);
                startActivity(intent);

            }
        });
    }
}
