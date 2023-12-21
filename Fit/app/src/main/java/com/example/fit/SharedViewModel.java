// SharedViewModel.java
package com.example.fit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Integer> currentProgress = new MutableLiveData<>();
    private final MutableLiveData<Integer> dailyWaterGoal = new MutableLiveData<>();
    private final MutableLiveData<List<WaterRecord>> waterRecords = new MutableLiveData<>();

    public SharedViewModel() {
        // Inicializa com um progresso e meta padrão (você pode personalizar isso)
        currentProgress.setValue(0);
        dailyWaterGoal.setValue(0);
    }

    public void setCurrentProgress(int progress) {
        currentProgress.setValue(progress);
    }

    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    public void setDailyWaterGoal(int goal) {
        dailyWaterGoal.setValue(goal);
    }

    public LiveData<Integer> getDailyWaterGoal() {
        return dailyWaterGoal;
    }

    public void setWaterRecords(List<WaterRecord> records) {
        waterRecords.setValue(records);
    }

    public LiveData<List<WaterRecord>> getWaterRecords() {
        return waterRecords;
    }

    public void incrementProgress(int amount) {
        if (currentProgress.getValue() != null) {
            setCurrentProgress(currentProgress.getValue() + amount);
        }
    }

    public void updateProgress(int difference) {
        if (currentProgress.getValue() != null) {
            int current = currentProgress.getValue();
            setCurrentProgress(Math.max(0, current + difference));
        }
    }

    public void decrementProgress(int amount) {
        if (currentProgress.getValue() != null) {
            setCurrentProgress(currentProgress.getValue() - amount);
        }
    }
}
