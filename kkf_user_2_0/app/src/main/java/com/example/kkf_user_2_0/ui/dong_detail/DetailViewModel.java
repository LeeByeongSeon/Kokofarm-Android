package com.example.kkf_user_2_0.ui.dong_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {

    // MutableLiveData 클래스를 사용하면 setValue(), postValue()를 사용하여 각각 기본 쓰레드, 백그라운드 쓰레드에서 값을 변경할 수 있다.
    // LiveData 클래스는 뭔가를 표시할 때, MutableLiveData 클래스는 뭔가를 설정/변경할 때에 적합한 것 같다
    private final MutableLiveData<String> mText;

    public DetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}