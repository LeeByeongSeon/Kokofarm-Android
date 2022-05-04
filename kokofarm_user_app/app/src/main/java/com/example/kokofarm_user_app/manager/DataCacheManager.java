package com.example.kokofarm_user_app.manager;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.kokofarm_user_app.kkf_utils.DateUtil;
import com.example.kokofarm_user_app.kkf_utils.UtilFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class DataCacheManager {

    private static DataCacheManager instance = null;

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

    private HashMap<String, JSONObject> cacheDataMap;
    private HashMap<String, Long> cacheStampMap;
    private HashMap<String, String> cacheFeedPerMap;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public JSONObject getJsonData(String key){
        return cacheDataMap.getOrDefault(key, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getJsonData(String key, HashMap<String, String> map){

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
    public String getBufferData(String id, String key){

        JSONObject buffer = getJsonData("buffer");
        if(buffer == null){
            loadBufferData();
            buffer = getJsonData("buffer");
        }

        String ret = "";

        if(buffer != null){
            try {
                JSONObject dong = buffer.getJSONObject(id);
                ret = dong.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private JSONObject getApiJson(HashMap<String, String> map){

        try {
            String data = UtilFunction.get_api_data(map);
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

        JSONObject json = getJsonData("buffer", new HashMap<String, String>() {{
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

        JSONObject json = getJsonData("feedPer", new HashMap<String, String>() {{
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
