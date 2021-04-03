package com.isabella.aprenderingles.ui.animais;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnimaisViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnimaisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}