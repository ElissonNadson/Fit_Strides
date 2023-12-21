// HomeViewModel.java
package com.example.fit.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> stepCount = new MutableLiveData<>();
    private MutableLiveData<Integer> waterPercentage = new MutableLiveData<>();

    public HomeViewModel() {
        // Inicializa com valores padrão (você pode personalizar isso)
        stepCount.setValue(0);
        waterPercentage.setValue(0);
    }

    public LiveData<Integer> getStepCount() {
        return stepCount;
    }

    public LiveData<Integer> getWaterPercentage() {
        return waterPercentage;
    }

    // Métodos para atualizar os valores dos passos e porcentagem de água
    public void setStepCount(int steps) {
        stepCount.setValue(steps);
    }

    public void setWaterPercentage(int percentage) {
        waterPercentage.setValue(percentage);
    }

}
