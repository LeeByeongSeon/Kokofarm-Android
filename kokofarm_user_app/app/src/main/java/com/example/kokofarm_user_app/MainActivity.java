package com.example.kokofarm_user_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kokofarm_user_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;

    // BottomNavigationView 관련
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;

    // Toolbar 및 Side Navigation(Drawer) 관련
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    // Fragment manager
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // Fragment
    private BreedFragment breedFragment = new BreedFragment();              // 사육정보
    private OutRecordFragment outRecordFragment = new OutRecordFragment();  // 출하내역
    private SettingFragment settingFragment = new SettingFragment();

    // 앱 종료
    private long backPressedTime = 0L;

    // 스크롤
    boolean scrollStop;

    // navigation
    private NavHostFragment navHostFragment;
    private NavController navController;

    private Context context;
    boolean themeColor;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

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

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
//        navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController = navHostFragment.getNavController();
        
        // navigationView 랑 bottomNavigationView 를 navController 에 결합
        navigationView = binding.mainSideNav;
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        bottomNavigationView = binding.mainBottomNav;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                switch (navDestination.getId()){
                    case R.id.nav_dong:
//                        if(navDestination.getId() == R.id.nav_dong){
//                            binding.mainBottomNav.setVisibility(View.GONE);
//                        }else{
//                            binding.mainBottomNav.setVisibility(View.VISIBLE);
//                        }

                        break;
                }
            }
        });

        // 다크모드 관련
        themeColor = SettingFragment.modeLoad(getApplicationContext());
        SettingFragment.applyTheme(themeColor);
    }

    // Side Menu & Bottom Menu Touch Event
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_breed:
                replaceFragment(breedFragment);
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            case R.id.nav_record:
                replaceFragment(outRecordFragment);
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;

            case R.id.nav_settings:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    // Fragment 이동 제어
    public void replaceFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // 프래그먼트 전환 시 애니메이션 커밋 전에 세팅해줄 것
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);                                     // 그 전 스택 가져옴
        transaction.commit();
//        fragmentManager.executePendingTransactions();
    }

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

//    // fragment 뒤로가기
//    @Override
//    public boolean onSupportNavigateUp(){
//
//        if(fragmentManager.getBackStackEntryCount() == 0){
//            finish();
//        } else {
//            fragmentManager.popBackStack();
//        }
//
//        return super.onSupportNavigateUp();
//    }

}