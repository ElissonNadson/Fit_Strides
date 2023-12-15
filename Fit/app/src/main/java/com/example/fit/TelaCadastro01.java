package com.example.fit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TelaCadastro01 extends AppCompatActivity {

    private EditText editNomeCompleto, editEmail, editSenha;
    private RadioGroup genderRadioGroup;
    private Button btnContinuarCadastro;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro01);

        iniciarComponentes();

        btnContinuarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaCadastro01.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cadastrarUsuario() {
        String nome = editNomeCompleto.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (nome.isEmpty() || email.isEmpty()  || senha.isEmpty()) {
            Toast.makeText(TelaCadastro01.this, "Nome, email e senha são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String genero = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        if (genero.isEmpty()) {
            Toast.makeText(TelaCadastro01.this, "Selecione o gênero.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            irParaTelaCadastro02(nome, email, senha, genero);
                        } else {
                            Toast.makeText(TelaCadastro01.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void irParaTelaCadastro02(String nome, String email, String senha, String genero) {
        Intent intent = new Intent(TelaCadastro01.this, TelaCadastro02.class);
        intent.putExtra("NOME", nome);
        intent.putExtra("EMAIL", email);
        intent.putExtra("SENHA", senha);
        intent.putExtra("GENERO", genero);
        startActivity(intent);
    }

    private void iniciarComponentes() {
        editNomeCompleto = findViewById(R.id.nome_completo);
        editEmail = findViewById(R.id.email);
        editSenha = findViewById(R.id.senha);
        genderRadioGroup = findViewById(R.id.group_genero);
        btnContinuarCadastro = findViewById(R.id.botao_continuar);
        btnVoltar = findViewById(R.id.botao_voltar);
    }
}