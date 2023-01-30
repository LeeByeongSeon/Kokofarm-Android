package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class SettingFragment extends PreferenceFragmentCompat{

    SharedPreferences sharePrefer;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    SwitchPreferenceCompat preferSwitch;

    private Context mContext;

    public SettingFragment() {}

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey);
//        preferSwitch = (SwitchPreferenceCompat) findPreference("theme");

//        if(rootKey == null){
//        }

        sharePrefer = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.equals("theme")){
//                    Log.d("TEST", s);
                    if(sharePrefer.getBoolean("theme", true)){
                        applyTheme(true);
                        modeSave(s, true);
                    } else {
                        modeSave(s, false);
                        applyTheme(false);
                    }
                }
            }
        };
    }

    public static void applyTheme(boolean b){
        if (b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (!(b)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {// 안드로이드 10이상
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            // 안드로이드 10미만
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }
    }

    public void modeSave(String key, boolean b){
        SharedPreferences s;
        s = getActivity().getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static boolean modeLoad(Context context){
        SharedPreferences s;
        s = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        boolean loadMode = s.getBoolean("theme", false);

        return loadMode;
    }

    @Override
    public void onResume(){
        super.onResume();
        // 기본 설정이 변경될때 호출될 콜백 등록
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause(){
        super.onPause();
        // 이전 콜백을 등록 취소
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
    }
}
