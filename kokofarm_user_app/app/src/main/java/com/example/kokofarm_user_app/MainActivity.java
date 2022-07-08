package com.example.kokofarm_user_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kokofarm_user_app.databinding.ActivityMainBinding;
import com.example.kokofarm_user_app.kkf_utils.SimpleTimer;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;
import com.example.kokofarm_user_app.piece.MainButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // BottomNavigationView 관련
    private BottomNavigationView botNav;

    // Toolbar 및 Side Navigation(Drawer) 관련
    private NavigationView sideNav;
    private DrawerLayout drawerLayout;

    // Fragment
    private BreedFragment breedFragment = new BreedFragment();              // 사육정보
    private OutRecordFragment outRecordFragment = new OutRecordFragment();  // 출하내역

    // 앱 종료
    private long backPressedTime = 0L;

    // navigation
    private NavHostFragment navHostFragment;
    private NavController navController;

    private Context context;
    boolean themeColor;

    // 플로팅바 관련
    SimpleTimer timer;
    int tik = 0;
    final int hideCount = 2;
    Animation fadeIn;
    Animation fadeOut;

    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 동 선택 레이아웃 숨기기 타이머
        startHideTimer();

        // 애니메이션 로드
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        // 다크모드 관련
        themeColor = SettingFragment.modeLoad(getApplicationContext());
        Log.e("themeColor", "" + themeColor);
        SettingFragment.applyTheme(themeColor);

        // DrawerLayout
        drawerLayout = binding.mainDrawer;

        // Drawer open 이벤트
        binding.mainSideMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.openDrawer(GravityCompat.END);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        // 슬라이드 동작으로 열고 닫기 막음 (오로지 버튼 터치로만 개폐 가능)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NestedScrollView scrollView = binding.mainScrollView;
        PageManager.getInstance().setFragmentManager(getSupportFragmentManager());
        PageManager.getInstance().setScrollView(scrollView);

        Log.e("onCreate", "onCreate");

        sideNav = binding.mainSideNav;
        botNav = binding.mainBottomNav;

        setClickEvent();
        setSelectBar();

        botNav.setSelectedItemId(R.id.nav_home);

    }

    @Override
    protected void onStart() {

//        Log.e("onStart", "onStart");

        super.onStart();
    }

    @Override
    protected void onResume() {

//        Log.e("onResume", "onResume");

        super.onResume();
    }

    @Override
    protected void onDestroy() {

//        Log.e("onDestroy", "onDestroy");
        stopHideTimer();
        super.onDestroy();
    }

    // activity 터치 이벤트
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int userAction = event.getAction();
        switch (userAction) {
            case MotionEvent.ACTION_DOWN:
                if(tik > hideCount){
                    tik = 0;
                    floatingFadeIn();
                }
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void setClickEvent(){
        botNav.setOnItemSelectedListener(item -> PageManager.getInstance().eventBottomSelect(item.getItemId()));

        sideNav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.nav_env:
                    PageManager.getInstance().moveActivity(this, OutEnvironmentActivity.class);
                    return true;

                case R.id.nav_breed_info:
                    PageManager.getInstance().moveActivity(this, BreedInfoActivity.class);
                    return true;

                case R.id.nav_record:
                    PageManager.getInstance().moveActivity(this, OutRecordActivity.class);
                    return true;

                case R.id.nav_settings:
                    PageManager.getInstance().moveActivity(this, SettingActivity.class);
                    return true;
            }

            return false;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSelectBar(){
        LinearLayout lay = binding.selectBar;

        lay.removeAllViews();

        MainButton btn = new MainButton(this);
        btn.setContents("");
        lay.addView(btn);

        JSONObject buffer = DataCacheManager.getInstance().loadBufferData();

        for (Iterator<String> it = buffer.keys(); it.hasNext(); ){
            String key = it.next();

            btn = new MainButton(this);
            btn.setContents(key.substring(6));
            lay.addView(btn);
        }
    }

    // 액티비티 이동
//    public void moveActivity(Class<?> cls){
//        Intent intent = new Intent(getApplicationContext(), cls);
//        startActivity(intent);
//    }

    // Back 처리 (Drawer 닫기, HomeFragment 에서 앱 종료)
    @Override
    public void onBackPressed() {

        // Drawer 닫기
        if(drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);

//        } else if(homeFragment != null){    // 앱종료
//        } else if(homeFragment.isVisible()){    // 앱종료
        } else if (navController.getCurrentDestination().getId() == R.id.nav_home){

            // homeFragment 의 onBackPressed 메소드 호출
            if(System.currentTimeMillis() > backPressedTime + 1500){
                backPressedTime = System.currentTimeMillis();
                Toast toast = Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }
    
    private void startHideTimer(){
        stopHideTimer();

        timer = SimpleTimer.create_inst();

        timer.set_runnable(() -> {

            if(tik < hideCount){
                tik++;
            }
            else if(tik == hideCount){
                tik++;
                runOnUiThread(() -> {
                    floatingFadeOut();
                });
            }

            Log.e("tik", "" + tik );
        });
        timer.start(100, 1000);
    }

    private void stopHideTimer(){
        if(timer != null){
            timer.stop();
            timer = null;
        }

        tik = 0;
    }

    private void floatingFadeIn(){
        binding.selectBar.startAnimation(fadeIn);
        binding.mainFab.scrollTopBtn.startAnimation(fadeIn);
//        binding.selectBar.setVisibility(View.VISIBLE);
//        binding.mainFab.scrollTopBtn.setVisibility(View.VISIBLE);
    }

    private void floatingFadeOut(){
        binding.selectBar.startAnimation(fadeOut);
        binding.mainFab.scrollTopBtn.startAnimation(fadeOut);
//        binding.selectBar.setVisibility(View.GONE);
//        binding.mainFab.scrollTopBtn.setVisibility(View.GONE);
    }



//    // fragment 뒤로가기
//    @Override
//    public boolean onSupportNavigateUp(){
//        if(fragmentManager.getBackStackEntryCount() == 0){
//            finish();
//        } else {
//            fragmentManager.popBackStack();
//        }
//        return super.onSupportNavigateUp();
//    }

}