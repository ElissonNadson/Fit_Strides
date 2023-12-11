package com.example.fit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TelaCadastro01 extends AppCompatActivity {

    private EditText edit_email, edit_senha, edit_nocompleto;
    private RadioButton rg_radio_male, rg_radio_female;
    private RadioGroup gender_radio_group;
    private Button bt_continuecadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro01);
        IniciarComponente();

        bt_continuecadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCamposPreenchidos()) {
                    // Inicia a TelaCadastro02
                    Intent intent = new Intent(TelaCadastro01.this, TelaCadastro02.class);
                    startActivity(intent);
                } else {
                    // Nem todos os campos foram preenchidos, mostre uma mensagem ao usuário
                    Toast.makeText(TelaCadastro01.this, "Por favor, preencha todos os campos e selecione um gênero.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void IniciarComponente(){
        edit_nocompleto = findViewById(R.id.edit_nocompleto);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        rg_radio_male = findViewById(R.id.rg_radio_male);
        rg_radio_female = findViewById(R.id.rg_radio_female);
        gender_radio_group = findViewById(R.id.gender_radio_group);
        bt_continuecadastro = findViewById(R.id.bt_continuecadastro);
    }

    private boolean verificarCamposPreenchidos() {
        // Verifica se os campos de texto estão vazios
        if (edit_nocompleto.getText().toString().trim().isEmpty() ||
                edit_email.getText().toString().trim().isEmpty() ||
                edit_senha.getText().toString().trim().isEmpty()) {
            return false; // Retorna falso se algum campo de texto estiver vazio
        }

        // Verifica se algum RadioButton de gênero está selecionado
        if (gender_radio_group.getCheckedRadioButtonId() == -1) {
            return false; // Retorna falso se nenhum RadioButton estiver selecionado
        }

        return true; // Retorna verdadeiro se todos os campos estiverem preenchidos
    }
}
