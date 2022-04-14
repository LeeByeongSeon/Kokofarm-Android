package com.example.kkf_user_2_0.ui.out_record;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OutRecordViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OutRecordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
