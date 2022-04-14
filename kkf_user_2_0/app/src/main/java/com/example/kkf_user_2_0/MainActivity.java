package com.example.kkf_user_2_0;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkf_user_2_0.ui.breed_info.BreedInfoFragment;
import com.example.kkf_user_2_0.ui.dong_list.DongFragment;
import com.example.kkf_user_2_0.ui.ext_env.ExtEnvFragment;
import com.example.kkf_user_2_0.ui.feed_water.FeedWaterFragment;
import com.example.kkf_user_2_0.ui.out_record.OutRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.kkf_user_2_0.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Binding
    public ActivityMainBinding binding;

    // Fragment 추가/변경/삭제(Fragment UI 관리) 를 위해 생성
    private FragmentTransaction fragmentTransaction;
    
    // 가상키보드
    private InputMethodManager touchScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding 을 사용하여 setContentView. findViewById 보다 안전(?)하고 xml 객체 참조가 편함
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // 세로 화면 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // ActionBar 를 theme.xml 에서 없애주고 ToolBar 로 커스텀
        Toolbar toolbar = binding.customToolbar;
        setToolbarName("청운농장");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 활성화 돼있는 시스템 키보드를 제어하기 위한 변수
        touchScreen = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        // Binding 사용, 주석이 기존 방법
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationView navView = binding.navView;


        // 여기서 하단 탭 메뉴(각 fragment)를 activity_main 의 <fragment> 안에 주입
        NavController navController = Navigation.findNavController(this, R.id.fragment_zone);
        NavigationUI.setupWithNavController(navView, navController);

//        HorizontalScrollView horizontalScrollView = binding.horizontalScrollView;
//        horizontalScrollView.setVisibility(View.GONE);

    }

    // 가상키보드 제어
    public final void hideKeyboard(View v){
        InputMethodManager Imm = this.touchScreen;

        if(Imm != null){
            Imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // ToolBar 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_nav_menu, menu);

        return true;
    }

    // ToolBar 메뉴 눌렸을때
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        switch(menuItem.getItemId()){
            default:
                return super.onOptionsItemSelected(menuItem);

            case R.id.menu1:
                FeedWaterFragment feedWaterFragment = new FeedWaterFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_zone, feedWaterFragment)
                    .setReorderingAllowed(true)
                    .commit();
                break;

            case R.id.menu2:
                ExtEnvFragment extEnvFragment = new ExtEnvFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_zone, extEnvFragment)
                        .setReorderingAllowed(true)
                        .commit();
                break;

            case R.id.menu3:
                BreedInfoFragment breedInfoFragment = new BreedInfoFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_zone, breedInfoFragment)
                        .setReorderingAllowed(true)
                        .commit();
                break;

            case R.id.menu4:
                OutRecordFragment outRecordFragment = new OutRecordFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_zone, outRecordFragment)
                        .setReorderingAllowed(true)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // ActionBar 대신 ToolBar 사용
    private void setToolbarName(String farmName){
        TextView textView = binding.customTextview;
        textView.setText(farmName);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(35);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("START :::::::::: " ,"onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("RESUME :::::::::: " ,"onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("PAUSE :::::::::: " ,"onPause");
    }

    @Override
    protected  void onStop(){
        super.onStop();
        Log.d("STOP :::::::::: " , "onStop");
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.d("DESTROY :::::::::: ", "onDestroy");
    }
}