package com.example.fit.ui.passos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PassosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PassosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is passos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}