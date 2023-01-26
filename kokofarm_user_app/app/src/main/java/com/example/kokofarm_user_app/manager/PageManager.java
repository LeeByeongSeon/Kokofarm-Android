package com.example.kokofarm_user_app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kokofarm_user_app.BreedFragment;
import com.example.kokofarm_user_app.BreedInfoActivity;
import com.example.kokofarm_user_app.BreedInputFragment;
import com.example.kokofarm_user_app.BreedListFragment;
import com.example.kokofarm_user_app.DongFragment;
import com.example.kokofarm_user_app.FeedChartFragment;
import com.example.kokofarm_user_app.FeedFragment;
import com.example.kokofarm_user_app.HomeFragment;
import com.example.kokofarm_user_app.LoginFragment;
import com.example.kokofarm_user_app.MainActivity;
import com.example.kokofarm_user_app.ManagerFragment;
import com.example.kokofarm_user_app.OutEnvironmentActivity;
import com.example.kokofarm_user_app.OutRecordActivity;
import com.example.kokofarm_user_app.OutRecordFragment;
import com.example.kokofarm_user_app.OutSensorFragment;
import com.example.kokofarm_user_app.R;
import com.example.kokofarm_user_app.ScaleChartFragment;
import com.example.kokofarm_user_app.ScaleFragment;
import com.example.kokofarm_user_app.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;

public class PageManager {

    private static PageManager instance = null;

//    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    NestedScrollView scrollView;

    private int lastItem = 0;
    private String lastDong = "";

    private String selectDate = "";
    private String selectCode = "";

    MainActivity mainActivity;

    private String topContentsName = "";
    private String topContentsWeight = "";
    private String topContentsLive = "";
    private String topContentsDiff = "";

    public static PageManager getInstance(){
        if(instance == null){
            instance = new PageManager();
        }

        return instance;
    }

    private PageManager(){

    }

    public int getLastItem() {
        return lastItem;
    }

    public String getLastDong() {
        return lastDong;
    }
    public void clearLastDong() {lastDong = ""; }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;

    }

    public void setScrollView(NestedScrollView scrollView){
        this.scrollView = scrollView;
    }

    public String getRString(int id){
        return mainActivity.getResources().getString(id);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public boolean eventBreedCardSelect(String code, String date){

        if(selectDate.equals(date)){
            return false;
        }

        selectCode = code;
        selectDate = date;

        movePage();

        mainActivity.hideSelectBar(true);
        mainActivity.hideBottomNav(true);

        return true;
    }

    public void movePage(){

        mainActivity.hideToolbar(false);
        mainActivity.hideTopInfo(false);
        mainActivity.hideSelectBar(false);
        mainActivity.hideBottomNav(false);
        mainActivity.hideSideNav(false);

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
                if(lastDong.equals("")){
                    moveFragment(FeedFragment.newInstance());
                }
                else{
                    moveFragment(FeedChartFragment.newInstance(lastDong));
                }
                break;

            case R.id.nav_scale:
                if(lastDong.equals("")){
                    moveFragment(ScaleFragment.newInstance());
                }
                else{
                    moveFragment(ScaleChartFragment.newInstance(lastDong));
                }
                break;

            case R.id.nav_breed:

                if(selectDate.equals("")){
                    if(lastDong.equals("")){
                        moveFragment(BreedFragment.newInstance());
                    }
                    else{
                        moveFragment(BreedListFragment.newInstance(lastDong));
                    }
                }
                else{
                    moveFragment(BreedInputFragment.newInstance(selectCode, selectDate));
                    selectCode = "";
                    selectDate = "";
                }

                break;

        }
    }

    public void movePage(String pageName){

        Log.e("movePage", pageName);

        switch (pageName){
            case "login":
                lastItem = 0;
                mainActivity.hideToolbar(true);
                mainActivity.hideTopInfo(true);
                mainActivity.hideSelectBar(true);
                mainActivity.hideBottomNav(true);
                mainActivity.hideSideNav(true);
                moveFragment(LoginFragment.newInstance(false));
                break;

            case "logout":
                lastItem = 0;
                mainActivity.setTopContentsCollapsing("");
                mainActivity.hideToolbar(true);
                mainActivity.hideTopInfo(true);
                mainActivity.hideSelectBar(true);
                mainActivity.hideBottomNav(true);
                mainActivity.hideSideNav(true);
                moveFragment(LoginFragment.newInstance(true));
                break;

            case "manager":
                lastItem = 0;
                mainActivity.hideSelectBar(true);
                mainActivity.hideBottomNav(true);
                moveFragment(ManagerFragment.newInstance());
    
                // 데이터 세팅전에 미리 나와버려서 ManagerFragment 실행 후로 옮김
//                mainActivity.hideToolbar(false);
//                mainActivity.hideTopInfo(false);
                break;

            case "home":
                mainActivity.hideToolbar(false);
                mainActivity.hideTopInfo(false);
                mainActivity.hideSelectBar(false);
                mainActivity.hideBottomNav(false);
                mainActivity.hideSideNav(false);

                mainActivity.bottomNavClick(R.id.nav_home);
//                eventBottomSelect(R.id.nav_home);
                break;

            case "outSensor":
                lastItem = 1;
                mainActivity.hideToolbar(false);
                mainActivity.hideTopInfo(false);
                mainActivity.hideSelectBar(true);
                mainActivity.hideBottomNav(false);
                mainActivity.hideSideNav(false);

                moveFragment(OutSensorFragment.newInstance());
                break;

            case "comeoutRecord":
                lastItem = 1;
                mainActivity.hideToolbar(false);
                mainActivity.hideTopInfo(false);
                mainActivity.hideSelectBar(true);
                mainActivity.hideBottomNav(false);
                mainActivity.hideSideNav(false);

                moveFragment(OutRecordFragment.newInstance());
                break;
        }

    }

    public void setTopContentsData(String name, String weight, String live, String diff){
        topContentsName = name;
        topContentsWeight = weight;
        topContentsLive = live;
        topContentsDiff = diff;
    }

    public void showFarmTopContents(String dong){

        String name = dong.equals("") ? topContentsName : topContentsName + "-" + dong + "동";

        setTopContentsCollapsing(name);
        setTopLeftTitle(getRString(R.string.avg_weight));
        setTopLeftContents(topContentsWeight);
        setTopCenterTitle(getRString(R.string.live_count));
        setTopCenterContents(topContentsLive);
        setTopRightTitle(getRString(R.string.sd_block));
        setTopRightContents(topContentsDiff);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showManagerTopContents(String title){
        JSONObject farmList = DataCacheManager.getInstance().getFarmList();

        int in = 0;
        int out = 0;
        int dongInCount = 0;

        try {

            for (Iterator<String> it = farmList.keys(); it.hasNext(); ){
                String id = it.next();     // ex) KF0006

                JSONObject info = farmList.getJSONObject(id);
                int comein = info.getInt("comein");

                // 입추 출하 분기 - 추후에 출하 농장 추가 시 0인거 아닌거 분기하면 됨
                if(comein > 0){
                    in++;
                    dongInCount += comein;
                }
                else{
                    out++;
                }
            }

            int farmCnt = in + out;

            setTopContentsCollapsing(title);
            PageManager.getInstance().setTopLeftTitle(PageManager.getInstance().getRString(R.string.total_farm));
            PageManager.getInstance().setTopLeftContents(farmCnt + "");
            PageManager.getInstance().setTopCenterTitle(PageManager.getInstance().getRString(R.string.enter_farm));
            PageManager.getInstance().setTopCenterContents(in + "");
            PageManager.getInstance().setTopRightTitle(PageManager.getInstance().getRString(R.string.enter_dong));
            PageManager.getInstance().setTopRightContents(dongInCount + "");

            hideToolbar(false);
            hideTopInfo(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSelectBar(JSONObject buffer){
        mainActivity.setSelectBar(buffer);
    }

    public void setTopContentsCollapsing(String s){
        mainActivity.setTopContentsCollapsing(s);
    }

    public void setTopLeftTitle(String s){
        mainActivity.setTopLeftTitle(s);
    }

    public void setTopLeftContents(String s){
        mainActivity.setTopLeftContents(s);
    }

    public void setTopCenterTitle(String s){
        mainActivity.setTopCenterTitle(s);
    }

    public void setTopCenterContents(String s){
        mainActivity.setTopCenterContents(s);
    }

    public void setTopRightTitle(String s){
        mainActivity.setTopRightTitle(s);
    }

    public void setTopRightContents(String s){
        mainActivity.setTopRightContents(s);
    }

    public void setTopContents(String title, String left, String center, String right){
        mainActivity.setTopContents(title, left, center, right);
    }

    public void hideToolbar(boolean hide){
        mainActivity.hideToolbar(hide);
    }

    public void hideTopInfo(boolean hide){
        mainActivity.hideTopInfo(hide);
    }

    public void showManagerNav(boolean b){
        mainActivity.showManagerNav(b);
    }

}
