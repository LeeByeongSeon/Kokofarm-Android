package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.kokofarm.kokofarm_user_app.databinding.FragmentHomeBinding;
import com.kokofarm.kokofarm_user_app.databinding.FragmentLoginBinding;
import com.kokofarm.kokofarm_user_app.manager.ConfigManager;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private boolean isLogOut = false;

    public LoginFragment(){

    }

    public LoginFragment(boolean isLogOut) {
        this.isLogOut = isLogOut;
    }

    public static LoginFragment newInstance(boolean isLogOut) {

        return new LoginFragment(isLogOut);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("onCreate", "onCreate: Login");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);

        binding.tvError.setVisibility(View.GONE);

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        Log.e("onDestroyView", "onDestroyView: Login");
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        binding.btnLogin.setOnClickListener(view -> {
            String id = binding.etId.getText().toString();
            String pw = binding.etPw.getText().toString();

            int result = DataCacheManager.getInstance().login(id, pw);

            if(result != 0){
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText("* 아이디 또는 패스워드 입력 오류");
            }
            else{

                // 컨피그 저장
                ConfigManager.getInstance().writeConfig(
                        DataCacheManager.getInstance().getUserID(),
                        DataCacheManager.getInstance().getUserPW(),
                        DataCacheManager.getInstance().getUserType()
                );

                if(DataCacheManager.getInstance().getUserType().equals("admin")){
                    PageManager.getInstance().showManagerNav(true);
                    PageManager.getInstance().movePage("manager");
                }
                else{
                    PageManager.getInstance().movePage("home");
                }
            }
        });

        if(isLogOut){
            PageManager.getInstance().clearLastDong();
            DataCacheManager.getInstance().setUserType("");
            DataCacheManager.getInstance().setUserID("");
            DataCacheManager.getInstance().setUserPW("");
            DataCacheManager.getInstance().setSelectDong("");
            DataCacheManager.getInstance().setSelectFarm("");
        }

        // 기존 로그인 확인
        if(ConfigManager.getInstance().readConfig()){

            Log.e("readConfig", "true");
            String id = ConfigManager.getInstance().getOption("userID");
            String pw = ConfigManager.getInstance().getOption("userPW");

            binding.etId.setText(id);
            binding.etPw.setText(pw);

            if(!isLogOut){
                binding.btnLogin.performClick();
            }

        }else{
            Log.e("readConfig", "false");
        }
    }
}