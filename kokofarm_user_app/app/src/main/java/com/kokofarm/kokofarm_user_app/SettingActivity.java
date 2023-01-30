package com.kokofarm.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class SettingActivity extends AppCompatActivity {    // SettingFragment 의 화면을 띄우기 위한 빈 액티비티 생성
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        if(savedInstanceState == null){
            Toolbar toolbar = findViewById(R.id.setting_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("설정");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fragmentManager = getSupportFragmentManager();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.setting_fragment, new SettingFragment())
                    .commit();
//        }
    }

//    @Override
//    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref){
//        final Bundle args = pref.getExtras();
//        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
//                getClassLoader(),
//                pref.getFragment());
//        fragment.setArguments(args);
//        fragment.setTargetFragment(caller, 0);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.setting_fragment, fragment)
//                .addToBackStack(null)
//                .commit();
//
//        return true;
//    }
}
