package com.example.kkf_user_2_0.ui.iot_scale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IoTScaleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IoTScaleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}