package com.example.fit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Encontrar o botão no layout
        Button btnGoToPedometro = findViewById(R.id.btnGoToPedometro);

        // Configurar um clique listener para o botão
        btnGoToPedometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o botão é clicado, iniciar a atividade Pedometro
                Intent intent = new Intent( home.this, pedometro.class);
                startActivity(intent);
            }
        });
    }
}
