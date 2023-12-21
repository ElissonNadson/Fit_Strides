package com.example.fit.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit.R;
import com.example.fit.FormLogin;
import com.example.fit.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private TextView tvSteps;
    private ProgressBar progressBarSteps;
    private TextView tvWaterPercentage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurando o botão de logout
        FloatingActionButton btnLogout = root.findViewById(R.id.fab_logout);
        btnLogout.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });

        // Inicializando os elementos de interface
        tvSteps = root.findViewById(R.id.tv_steps);
        progressBarSteps = root.findViewById(R.id.progressbar_steps);
        tvWaterPercentage = root.findViewById(R.id.tv_percentage);

        // Observa a quantidade de passos
        homeViewModel.getStepCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer steps) {
                // Atualiza o TextView de quantidade de passos
                tvSteps.setText(String.valueOf(steps));
            }
        });

        // Observa a porcentagem de água
        homeViewModel.getWaterPercentage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer percentage) {
                // Atualiza o TextView de porcentagem de água
                tvWaterPercentage.setText(String.valueOf(percentage) + "%");

                // Atualiza a ProgressBar
                progressBarSteps.setProgress(percentage);
            }
        });

        return root;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("Você tem certeza que deseja deslogar?")
                .setPositiveButton("Deslogar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut(); // Desloga o usuário

                        Intent intent = new Intent(getActivity(), FormLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("logout", true); // Indica que o usuário fez logout
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
