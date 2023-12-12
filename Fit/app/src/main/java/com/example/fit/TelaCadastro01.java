package com.example.fit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TelaCadastro01 extends AppCompatActivity {

    private EditText edit_email, edit_senha, edit_nomecompleto;
    private RadioButton rg_radio_male, rg_radio_female;
    private RadioGroup gender_radio_group;
    private Button bt_continuecadastro;

    String[] mensagens = {"preencha todos os campos", "cadastro realizado com sucesso"};

    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro01);

        IniciarComponente();

        bt_continuecadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarUsuario(v);
            }
        });
    }

    private void CadastrarUsuario(View v){
        String nome = edit_nomecompleto.getText().toString();
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        // Verificar qual botão de rádio está selecionado
        int selectedId = gender_radio_group.getCheckedRadioButtonId();
        String genero = "";
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            genero = selectedRadioButton.getText().toString();
        }

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || genero.isEmpty()) {
            Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        SalvarDadosUsuario();
                        Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else{
                        String erro;
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e) {
                            erro = "digite uma senha com no mínimo 6 caracteres";
                        }catch (FirebaseAuthUserCollisionException e){
                            erro = "essa conta já foi cadastrada";
                        }catch(FirebaseAuthInvalidCredentialsException e){
                            erro = "e-mail inválido";
                        }catch (Exception e){
                            erro = "erro ao cadastrar usuário";
                        }
                        Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            });
        }
    }

    private void SalvarDadosUsuario(){
        String nome = edit_nomecompleto.getText().toString();
        String genero = "";
        int selectedId = gender_radio_group.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            genero = selectedRadioButton.getText().toString();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuario = new HashMap<>();
        usuario.put("nome", nome);
        usuario.put("genero", genero); // Adicionando o gênero ao banco de dados

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db","sucesso ao salvar os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error","erro ao salvar os dados" + e.toString());
                    }
                });
    }

    private void IniciarComponente(){
        edit_nomecompleto = findViewById(R.id.edit_nomecompleto);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        rg_radio_male = findViewById(R.id.rg_radio_male);
        rg_radio_female = findViewById(R.id.rg_radio_female);
        gender_radio_group = findViewById(R.id.gender_radio_group);
        bt_continuecadastro = findViewById(R.id.bt_continuecadastro);
    }
}
