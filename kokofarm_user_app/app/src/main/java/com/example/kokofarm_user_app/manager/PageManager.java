package com.example.kokofarm_user_app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kokofarm_user_app.BreedFragment;
import com.example.kokofarm_user_app.BreedInfoActivity;
import com.example.kokofarm_user_app.DongFragment;
import com.example.kokofarm_user_app.FeedFragment;
import com.example.kokofarm_user_app.HomeFragment;
import com.example.kokofarm_user_app.OutEnvironmentActivity;
import com.example.kokofarm_user_app.OutRecordActivity;
import com.example.kokofarm_user_app.R;
import com.example.kokofarm_user_app.ScaleFragment;
import com.example.kokofarm_user_app.SettingActivity;

public class PageManager {

    private static PageManager instance = null;

//    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    NestedScrollView scrollView;

    private int lastItem = 0;
    private String lastDong = "";

    public static PageManager getInstance(){
        if(instance == null){
            instance = new PageManager();
        }

        return instance;
    }

    private PageManager(){

    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;

    }

    public void setScrollView(NestedScrollView scrollView){
        this.scrollView = scrollView;
    }

    public void moveFragment(Fragment fragment){
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // 프래그먼트 전환 시 애니메이션 커밋 전에 세팅해줄 것
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);                            // 그 전 스택 가져옴
        transaction.commit();

        scrollView.scrollTo(0, 0);
    }

    public void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public boolean eventBottomSelect(int id){

        if(lastItem == id){
            return false;
        }

        lastItem = id;

        movePage();

        return true;
    }

    public boolean eventSelectBar(String dong){

        if(lastDong.equals(dong)){
            return false;
        }

        lastDong = dong;

        movePage();

        return true;

    }

    public void movePage(){

        switch (lastItem){

            case R.id.nav_home:
                if(lastDong.equals("")){
                    moveFragment(HomeFragment.newInstance());
                }
                else{
                    moveFragment(DongFragment.newInstance(lastDong));
                }
                break;

            case R.id.nav_feed:
                moveFragment(FeedFragment.newInstance());
                break;

            case R.id.nav_scale:
                moveFragment(ScaleFragment.newInstance());
                break;

            case R.id.nav_breed:
                moveFragment(BreedFragment.newInstance());
                break;

        }
    }

}
