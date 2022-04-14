package com.example.kkf_user_2_0.ui.breed_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BreedInfoViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public BreedInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
