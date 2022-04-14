package com.example.kkf_user_2_0.ui.dong_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DongViewModel extends ViewModel {

    // MutableLiveData : Data 를 감싸기 위한 Wrapper 클래스
    private final MutableLiveData<String> liveData;

    public DongViewModel() {
        liveData = new MutableLiveData<>();
        liveData.setValue(""); // setValue : UI 쓰레드, postValue : Background 쓰레드
    }

    public LiveData<String> getText() {
        return liveData;
    }
}