package com.example.kkf_user_2_0.ui.ext_env;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExtEnvViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExtEnvViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
