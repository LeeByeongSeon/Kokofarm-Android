package com.example.kkf_user_2_0.ui.feed_water;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedWaterViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FeedWaterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
