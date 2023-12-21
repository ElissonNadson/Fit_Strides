package com.example.fit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> currentProgress = new MutableLiveData<>();
    private final MutableLiveData<Integer> dailyWaterGoal = new MutableLiveData<>();

    public SharedViewModel() {
        // Inicialize com um progresso e meta padrão (você pode personalizar isso)
        currentProgress.setValue(0);
        dailyWaterGoal.setValue(0); // Exemplo de meta padrão: 2000ml
    }

    // Métodos para atualizar e obter o progresso da água
    public void setCurrentProgress(int progress) {
        currentProgress.setValue(progress);
    }

    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    // Métodos para atualizar e obter a meta diária de água
    public void setDailyWaterGoal(int goal) {
        dailyWaterGoal.setValue(goal);
    }

    public LiveData<Integer> getDailyWaterGoal() {
        return dailyWaterGoal;
    }

    // Você pode adicionar métodos que seu código precisa para incrementar/decrementar o progresso atual
    // Isto poderia ser útil para atualizar o progresso quando adicionar ou remover registros de água
    public void incrementProgress(int amount) {
        if (currentProgress.getValue() != null) {
            setCurrentProgress(currentProgress.getValue() + amount);
        }
    }

    public void decrementProgress(int amount) {
        if (currentProgress.getValue() != null) {
            setCurrentProgress(currentProgress.getValue() - amount);
        }
    }
}