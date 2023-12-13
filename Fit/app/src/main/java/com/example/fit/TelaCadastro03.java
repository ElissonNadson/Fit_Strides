package com.example.fit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// Um exemplo simples da classe Usuario que você deve definir em outro arquivo:
class Usuario {
    public String nome, email, senha, genero, idade, altura, peso, imageUrl;

    public Usuario(String nome, String email, String senha, String genero, String idade, String altura, String peso, String imageUrl) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
        this.imageUrl = imageUrl;
    }

    // Métodos getters e setters se necessários
}

public class TelaCadastro03 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 123;
    private ImageView imagePerfil;
    private Button botaoFinalizarCadastro;
    private Uri imagemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro03);

        imagePerfil = findViewById(R.id.image_perfil);
        botaoFinalizarCadastro = findViewById(R.id.botao_finalizar_cadastro);

        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        botaoFinalizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    if (imagemUri != null) {
                        // Se a imagem foi selecionada, inicie o upload e depois salve os dados do usuário
                        uploadImageAndSaveUserData(extras);
                    } else {
                        // Se nenhuma imagem foi selecionada, apenas salve os dados do usuário sem uma URL de imagem
                        saveUserData(extras, null);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagemUri = data.getData();
            imagePerfil.setImageURI(imagemUri);
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a Imagem"), PICK_IMAGE_REQUEST);
    }

    private void uploadImageAndSaveUserData(final Bundle extras) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId + ".jpg");

        fileRef.putFile(imagemUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        saveUserData(extras, imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaCadastro03.this, "Falha no upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(Bundle extras, String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Usuario usuario = new Usuario(
                extras.getString("NOME"),
                extras.getString("EMAIL"),
                extras.getString("SENHA"),
                extras.getString("GENERO"),
                extras.getString("IDADE"),
                extras.getString("ALTURA"),
                extras.getString("PESO"),
                imageUrl
        );

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userId)
                .setValue(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Redireciona para a tela de login após cadastro
                        Intent intent = new Intent(TelaCadastro03.this, FormLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaCadastro03.this, "Falha ao salvar os dados", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}