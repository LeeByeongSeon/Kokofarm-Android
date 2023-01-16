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
import android.content.Intent;
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
import com.example.kokofarm_user_app.kkf_utils.FloatCompute;
import com.example.kokofarm_user_app.kkf_utils.SimpleTimer;
import com.example.kokofarm_user_app.manager.ConfigManager;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;
import com.example.kokofarm_user_app.piece.DongCardView;
import com.example.kokofarm_user_app.piece.MainButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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

        // 메인 액티비티 함수 접근을 위함
        PageManager.getInstance().setMainActivity(this);
        ConfigManager.getInstance().setContext(getApplicationContext());

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

        // 상단 이동 버튼
        binding.mainFab.scrollTopBtn.setOnClickListener(view -> scrollView.scrollTo(0, 0));

        sideNav = binding.mainSideNav;
        botNav = binding.mainBottomNav;

        setClickEvent();

        // 로그인 분기 작업
        checkLogin();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkLogin(){
        // 로그인 분기
        if(DataCacheManager.getInstance().getUserType().equals("")){
            PageManager.getInstance().movePage("login");
            return;
        }

        if(DataCacheManager.getInstance().getUserType().equals("admin")){
            showManagerNav(true);
            PageManager.getInstance().movePage("manager");
        }
        else{
            showManagerNav(false);
            botNav.setSelectedItemId(R.id.nav_home);
        }
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

                case R.id.nav_manager:
                    PageManager.getInstance().movePage("manager");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;

                case R.id.nav_env:
                    PageManager.getInstance().movePage("outSensor");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;

                case R.id.nav_breed_info:
                    PageManager.getInstance().moveActivity(this, BreedInfoActivity.class);
                    return true;

                case R.id.nav_record:
                    PageManager.getInstance().movePage("comeoutRecord");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    return true;

                case R.id.nav_settings:
                    PageManager.getInstance().moveActivity(this, SettingActivity.class);
                    return true;

                case R.id.nav_logout:
                    drawerLayout.closeDrawer(GravityCompat.END);
                    PageManager.getInstance().movePage("logout");
                    return true;
            }

            return false;
        });
    }

    // Back 처리 (Drawer 닫기, HomeFragment 에서 앱 종료)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {

        int lastItem = PageManager.getInstance().getLastItem();
        String lastDong = PageManager.getInstance().getLastDong();

        // Drawer 닫기
        if(drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        // 동 세부 화면인 경우 - 홈으로 이동
        if(!lastDong.equals("")){
            PageManager.getInstance().eventSelectBar("");
            return;
        }

        // 홈화면이 아닌 페이지에 있고, 로그인이나 관리자 페이지가 아닌 경우 홈화면으로 이동
        if (lastItem != R.id.nav_home && lastItem != 0){
            botNav.setSelectedItemId(R.id.nav_home);
            return;
//            // homeFragment 의 onBackPressed 메소드 호출
//            if(System.currentTimeMillis() > backPressedTime + 1500){
//                backPressedTime = System.currentTimeMillis();
//                Toast toast = Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
//                toast.show();
//            } else {
//                finish();
//            }
        }

        // 홈화면인 경우 로그인된 계정이 관리자일 때 관리자 페이지로 보냄
        if(lastItem == R.id.nav_home){
            if(DataCacheManager.getInstance().getUserType().equals("admin")){
                PageManager.getInstance().movePage("manager");
                return;
            }
        }

        finish();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSelectBar(JSONObject buffer){
        LinearLayout lay = binding.selectBar;

        lay.removeAllViews();

        MainButton btn = new MainButton(this);
        btn.setContents("");
        lay.addView(btn);

        for (Iterator<String> it = buffer.keys(); it.hasNext(); ){
            String key = it.next();

            btn = new MainButton(this);
            btn.setContents(key.substring(6));
            lay.addView(btn);
        }
    }

    public void hideToolbar(boolean hide){
        if(hide){
            binding.mainToolbar.setVisibility(View.GONE);
        }
        else{
            binding.mainToolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideTopInfo(boolean hide){
        if(hide){
            binding.layTopInfo.setVisibility(View.GONE);
        }
        else{
            binding.layTopInfo.setVisibility(View.VISIBLE);
        }
    }

    public void hideBottomNav(boolean hide){
        if(hide){
            binding.mainBottomNav.setVisibility(View.GONE);
        }
        else{
            binding.mainBottomNav.setVisibility(View.VISIBLE);
        }
    }

    public void hideSelectBar(boolean hide){
        if(hide){
            binding.selectBar.setVisibility(View.GONE);
        }
        else{
            binding.selectBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideSideNav(boolean hide){
        if(hide){
            binding.mainSideNav.setVisibility(View.GONE);
        }
        else{
            binding.mainSideNav.setVisibility(View.VISIBLE);
        }
    }

    public void setTopContentsCollapsing(String s){
        binding.mainCollapsing.setTitle(s);
    }

    public void setTopLeftTitle(String s){
        binding.tvTopLeftTitle.setText(s);
    }

    public void setTopLeftContents(String s){
        binding.tvTopLeftContent.setText(s);
    }

    public void setTopCenterTitle(String s){
        binding.tvTopCenterTitle.setText(s);
    }

    public void setTopCenterContents(String s){
        binding.tvTopCenterContent.setText(s);
    }

    public void setTopRightTitle(String s){
        binding.tvTopRightTitle.setText(s);
    }

    public void setTopRightContents(String s){
        binding.tvTopRightContent.setText(s);
    }

    public void setTopContents(String title, String left, String center, String right){
        binding.mainCollapsing.setTitle(title);
        binding.tvTopLeftContent.setText(left);
        binding.tvTopCenterContent.setText(center);
        binding.tvTopRightContent.setText(right);
    }

    public void bottomNavClick(int id){
        botNav.setSelectedItemId(id);
    }

    public void showManagerNav(boolean b){
        sideNav.getMenu().getItem(0).getSubMenu().getItem(0).setVisible(b);
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