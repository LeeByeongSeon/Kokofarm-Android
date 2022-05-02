package com.example.kokofarm_user_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kokofarm_user_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;

    // BottomNavigationView 관련
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;

    // Toolbar 및 Side Navigation(Drawer) 관련
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    // Fragment manager
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // Fragment
    private HomeFragment homeFragment = new HomeFragment();                 // 홈
    private FeedFragment feedFragment = new FeedFragment();                 // 급이 급수
    private ScaleFragment scaleFragment = new ScaleFragment();              // IoT 저울
    private EnvFragment envFragment = new EnvFragment();                    // 외기환경
    private BreedFragment breedFragment = new BreedFragment();              // 사육정보
    private OutRecordFragment outRecordFragment = new OutRecordFragment();  // 출하내역

    // 앱 종료
    private long backPressedTime = 0L;

    // 스크롤
    boolean scrollStop;

    // navigation
    private NavHostFragment navHostFragment;
    private NavController navController;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        navController = Objects.requireNonNull(navHostFragment).getNavController();
        
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

    }

    // Fragment 이동 제어
    public void replaceFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // 프래그먼트 전환 시 애니메이션 커밋 전에 세팅해줄 것
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);                                     // 그 전 스택 가져옴
        transaction.commit();
    }

    // Side Menu & Bottom Menu Touch Event
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_breed:
                replaceFragment(breedFragment);
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.nav_record:
                replaceFragment(outRecordFragment);
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
        }
        return true;
    }

    // ToolBar 이름 설정
    private void setToolbarName(String farmName){
        TextView textView = binding.mainTextview;
        textView.setText(farmName);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize(32);
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

//    @Override
//    public boolean onSupportNavigateUp(){
//        return navController.navigateUp();
//    }
}