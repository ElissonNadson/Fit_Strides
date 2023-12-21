package com.example.fit.ui.agua;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fit.R;
import com.example.fit.WaterRecord;
import com.example.fit.WaterRecordAdapter;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AguaFragment extends Fragment {

    private RecyclerView recyclerView;
    private WaterRecordAdapter adaptadorRegistroAgua;
    private List<WaterRecord> registrosAgua;
    private FloatingActionButton botaoAdicionar, botaoOpcao500ml, botaoOpcaoPersonalizada, botaoOpcao200ml, botaoOpcao300ml;
    private int currentProgress = 0;

    private CircularProgressBar circularProgressBar;
    private TextView tvProgressText;
    private TextView tvDailyWaterGoal;


    private static final String KEY_CURRENT_PROGRESS = "current_progress";
    private static final String KEY_WATER_RECORDS = "water_records";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agua, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_today_records);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        registrosAgua = new ArrayList<>();
        adaptadorRegistroAgua = new WaterRecordAdapter(getContext(), registrosAgua);
        adaptadorRegistroAgua.setOnItemClickListener(new WaterRecordAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                final WaterRecord recordToEdit = registrosAgua.get(position);
                final String documentId = recordToEdit.getDocumentId(); // Obtenha o ID do documento do Firestore aqui.

                final EditText editAmount = new EditText(getContext());
                editAmount.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editAmount.setText(recordToEdit.getAmount().replace("ml", ""));

                new AlertDialog.Builder(getContext())
                        .setTitle("Editar quantidade de água")
                        .setView(editAmount)
                        .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String newAmount = editAmount.getText().toString();
                                if (!newAmount.isEmpty()) {
                                    int oldAmount = Integer.parseInt(recordToEdit.getAmount().replace("ml", ""));
                                    int newAmountInt = Integer.parseInt(newAmount);
                                    int difference = newAmountInt - oldAmount;

                                    // Atualizar no RecyclerView
                                    recordToEdit.setAmount(newAmount + "ml");
                                    adaptadorRegistroAgua.notifyItemChanged(position);

                                    // Atualizar na circularProgressBar
                                    updateProgress(difference);

                                    // Atualizar no Firestore
                                    updateFirestoreRecord(documentId, newAmountInt);
                                } else {
                                    Toast.makeText(getActivity(), "Por favor, insira uma quantidade válida.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            private void updateFirestoreRecord(String documentId, int newAmount) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();

                    db.collection("Users").document(userId)
                            .collection("registrosConsumo").document(documentId)
                            .update("amount", newAmount)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Erro ao atualizar registro", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getActivity(), "Usuário não está logado.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onDeleteClick(int position) {
                WaterRecord recordToDelete = registrosAgua.get(position);
                String documentId = recordToDelete.getDocumentId(); // Obter o ID do documento Firestore.

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmação")
                        .setMessage("Deseja deletar este registro?")
                        .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    // Chamada para o método de exclusão no Firestore.
                                    db.collection("Users").document(userId)
                                            .collection("registrosConsumo").document(documentId)
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                // Remoção do registro da lista local e da RecyclerView.
                                                int amountToDelete = Integer.parseInt(recordToDelete.getAmount().replace("ml", ""));
                                                registrosAgua.remove(position);
                                                adaptadorRegistroAgua.notifyItemRemoved(position);
                                                adaptadorRegistroAgua.notifyItemRangeChanged(position, adaptadorRegistroAgua.getItemCount());

                                                // Atualização do progresso após a exclusão do registro.
                                                updateProgress(-amountToDelete);

                                                Toast.makeText(getActivity(), "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Erro ao deletar registro", Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(getActivity(), "Usuário não está logado.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adaptadorRegistroAgua);

        botaoAdicionar = view.findViewById(R.id.fab_add);
        botaoOpcao500ml = view.findViewById(R.id.fab_option_500ml);
        botaoOpcaoPersonalizada = view.findViewById(R.id.fab_option_custom);
        botaoOpcao200ml = view.findViewById(R.id.fab_option_200ml);
        botaoOpcao300ml = view.findViewById(R.id.fab_option_300ml);

        botaoOpcao200ml.setVisibility(View.GONE);
        botaoOpcaoPersonalizada.setVisibility(View.GONE);
        botaoOpcao500ml.setVisibility(View.GONE);
        botaoOpcao300ml.setVisibility(View.GONE);

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOptionButtonsVisibility();
            }
        });

        botaoOpcao200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterRecord("200ml");
            }
        });

        botaoOpcao300ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterRecord("300ml");
            }
        });

        botaoOpcao500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterRecord("500ml");
            }
        });

        botaoOpcaoPersonalizada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        tvProgressText = view.findViewById(R.id.tv_water_intake_amount);
        tvDailyWaterGoal = view.findViewById(R.id.tv_daily_water_goal);
        updateProgressBar(currentProgress);
        setDailyWaterGoal();


        return view;
    }
    public void setDailyWaterGoal() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.contains("peso")) { // Alterado de "weight" para "peso"
                            Number peso = documentSnapshot.getDouble("peso"); // Alterado de "weight" para "peso"
                            if (peso != null) {
                                int dailyGoal = (int) (0.035 * peso.doubleValue() * 1000);
                                updateDailyWaterGoalUI(dailyGoal);
                            } else {
                                // Caso o campo "peso" esteja nulo.
                                tvDailyWaterGoal.setText("Peso não definido no perfil.");
                            }
                        } else {
                            // Caso o documento não contenha o campo "peso".
                            tvDailyWaterGoal.setText("Perfil de usuário não contém informações sobre peso.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Caso haja falha ao acessar o documento no Firestore.
                        Toast.makeText(getActivity(), "Não foi possível carregar o perfil do usuário.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Caso o usuário não esteja logado.
            tvDailyWaterGoal.setText("Usuário não logado.");
        }
    }

    private void updateDailyWaterGoalUI(int dailyGoal) {
        tvDailyWaterGoal.setText(String.format(Locale.getDefault(), "%,d ml", dailyGoal));
        circularProgressBar.setProgressMax(dailyGoal);
    }
    private void addWaterRecord(String amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());

            Map<String, Object> recordData = new HashMap<>();
            recordData.put("iconResId", R.drawable.ic_agua);
            recordData.put("time", currentTime);
            recordData.put("name", "Copo de água");
            recordData.put("amount", amount.replace("ml", "")); // Assumindo que você quer salvar apenas o número

            db.collection("Users").document(userId)
                    .collection("registrosConsumo")
                    .add(recordData)
                    .addOnSuccessListener(documentReference -> {
                        // Aqui você obtém o ID do documento e pode prosseguir com a criação do WaterRecord e atualização da UI
                        String documentId = documentReference.getId();
                        WaterRecord newRecord = new WaterRecord(R.drawable.ic_agua, currentTime, "Copo de água", amount, documentId);

                        registrosAgua.add(0, newRecord);
                        adaptadorRegistroAgua.notifyItemInserted(0);

                        // Atualizações adicionais da UI: scroll, toast, progresso e escondendo botões
                        recyclerView.scrollToPosition(0);
                        Toast.makeText(getActivity(), amount + " adicionado", Toast.LENGTH_SHORT).show();

                        botaoOpcao500ml.setVisibility(View.GONE);
                        botaoOpcaoPersonalizada.setVisibility(View.GONE);
                        botaoOpcao200ml.setVisibility(View.GONE);
                        botaoOpcao300ml.setVisibility(View.GONE);

                        currentProgress += Integer.parseInt(amount.replace("ml", ""));
                        updateProgressBar(currentProgress);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Erro ao adicionar registro", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "Usuário não está logado.", Toast.LENGTH_SHORT).show();
        }
    }


    private void salvarRegistroHidratacaoFirestore(String amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obter o usuário atual do Firebase Authentication
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid(); // O ID único do usuário

            // Você pode usar um timestamp como um ID de entrada único
            String idEntradaData = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());

            // Montando o objeto do registro para Firestore
            Map<String, Object> registroHidratacao = new HashMap<>();

            registroHidratacao.put("horario", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date()));
            registroHidratacao.put("quantidade", Integer.parseInt(amount.replace("ml", "")));

            // Salvando no Firestore no caminho usuarios/id_usuario/registrosConsumo/id_entrada_data
            db.collection("Users").document(userId)
                    .collection("registrosConsumo").document(idEntradaData)
                    .set(registroHidratacao)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Registro salvo", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Erro ao salvar registro", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "Usuário não está logado.", Toast.LENGTH_SHORT).show();
        }
    }




    private void toggleOptionButtonsVisibility() {
        int currentVisibility = botaoOpcao200ml.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        botaoOpcao500ml.setVisibility(currentVisibility);
        botaoOpcaoPersonalizada.setVisibility(currentVisibility);
        botaoOpcao200ml.setVisibility(currentVisibility);
        botaoOpcao300ml.setVisibility(currentVisibility);
    }

    private void showCustomDialog() {
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Insira a quantidade em ml");

        new AlertDialog.Builder(getContext())
                .setTitle("Personalizar quantidade de água")
                .setView(input)
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String quantidade = input.getText().toString();
                        if (!quantidade.isEmpty()) {
                            addWaterRecord(quantidade + "ml");
                        } else {
                            Toast.makeText(getActivity(), "Por favor, insira uma quantidade válida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void updateProgressBar(int progress) {
        circularProgressBar.setProgress(progress);
        tvProgressText.setText(getString(R.string.progress_text, progress));
    }

    private void updateProgress(int amount) {
        currentProgress += amount;
        updateProgressBar(currentProgress);
    }
}
