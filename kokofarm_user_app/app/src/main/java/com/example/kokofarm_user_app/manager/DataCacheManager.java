package com.example.kokofarm_user_app.manager;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.kokofarm_user_app.kkf_utils.BackTasker;
import com.example.kokofarm_user_app.kkf_utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DataCacheManager {

    private static DataCacheManager instance = null;

    private static final String API_KEY = "06071227041701229789";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DataCacheManager getInstance(){
        if(instance == null){
            instance = new DataCacheManager();
        }

        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DataCacheManager(){
        cacheDataMap = new HashMap<>();
        cacheStampMap = new HashMap<>();
        cacheFeedPerMap = new HashMap<>();
    }

    // 멤버 선언부
    private String userID = "kk0071";
    private String userPW = "24342434";
    private String selectFarm = "KF0071";
    private String selectDong = "01";

    private HashMap<String, JSONObject> cacheDataMap;
    private HashMap<String, Long> cacheStampMap;
    private HashMap<String, String> cacheFeedPerMap;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPW(String userPW){
        this.userPW = userPW;
    }

    public void setSelectFarm(String selectFarm){
        this.selectFarm = selectFarm;
    }

    public void setSelectDong(String selectDong){
        this.selectDong = selectDong;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public JSONObject getCacheData(String key){
        return cacheDataMap.getOrDefault(key, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String key, HashMap<String, String> map){

        JSONObject ret;
        //String key = map.get("setComm");

        if(key == null) return null;

        if(needRefresh(key)){
            ret = getApiJson(map);
            cacheDataMap.put(key, ret);
            cacheStampMap.put(key, DateUtil.get_inst().get_now_timestamp());
        }
        else{
            ret = cacheDataMap.get(key);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean needRefresh(String key){

        boolean ret = false;

        if(cacheDataMap.containsKey(key)){
            long prev = cacheStampMap.get(key);
            long curr = DateUtil.get_inst().get_now_timestamp();

            if(curr - prev > 300){
                ret = true;
            }
        }
        else{
            ret = true;
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getBufferData(String id){

        JSONObject buffer = getCacheData("buffer");
        if(buffer == null){
            loadBufferData();
            buffer = getCacheData("buffer");
        }

        JSONObject ret = null;

        if(buffer != null){
            try {
                ret = buffer.getJSONObject(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private String getApiData(HashMap<String, String> request){

        final String API_URL = "http://api.kokofarm.co.kr/contents/android_api.php";

        String recvMsg = "";

        BackTasker bt = new BackTasker() {

            @Override
            protected String doInBackground() {

                String ret = "";
                try{

                    String postData = "apiKey=" + API_KEY;

                    if(request == null) return ret;
                    for(Map.Entry<String, String> entry : request.entrySet()){
                        postData += "&" + entry.getKey() + "=" + entry.getValue();
                    }

                    Log.e("postData", postData);

                    URL url = new URL(API_URL + "?" + postData);

                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("POST");
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                    DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
                    out.writeBytes(postData);
                    out.flush();
                    out.close();

                    InputStreamReader inReader = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(inReader);
                    StringBuffer buffer = new StringBuffer();

                    String str = "";
                    while ((str = reader.readLine()) != null){
                        buffer.append(str);
                    }
                    ret = buffer.toString();

                    reader.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ret;
            }
        };

        try {
            recvMsg = (String) bt.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return recvMsg;
    }

    private JSONObject getApiJson(HashMap<String, String> map){

        try {
            String data = getApiData(map);
            JSONObject jo = new JSONObject(data);

            if(jo.get("errCode").equals("00")){
                return jo.getJSONObject("retData");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadBufferData(){

        JSONObject json = getCacheData("buffer", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", userID);
            put("setComm", "buffer");
        }});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getFeedPerData(String id){

        String farm = "";
        //String dong = "";

        if(id.length() > 6){
            farm = id.substring(0, 6);
            //dong = id.substring(6);
        }
        else{
            farm = id;
        }

        if(cacheFeedPerMap.containsKey(id) && !needRefresh("feedPer")){
            return cacheFeedPerMap.get(id);
        }

        final String farmID = farm;

        JSONObject json = getCacheData("feedPer", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", userID);
            put("farmID", farmID);
            put("setComm", "feedPer");
        }});

        try {
            for (Iterator<String> it = json.keys(); it.hasNext(); ){
                String key = it.next();

            }
            Log.e("getFeedPerData", json.getJSONObject("KF007102").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";

    }
}
