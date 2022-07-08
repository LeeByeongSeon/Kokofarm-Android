package com.example.kokofarm_user_app.manager;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.kokofarm_user_app.MainActivity;
import com.example.kokofarm_user_app.kkf_utils.BackTasker;
import com.example.kokofarm_user_app.kkf_utils.DateUtil;
import com.example.kokofarm_user_app.kkf_utils.FloatCompute;

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
import java.util.LinkedHashMap;
import java.util.List;
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

    private HashMap<String, HashMap<String, JSONObject>> cacheDataMap;      //[farmID][dataComm]
    private HashMap<String, HashMap<String, Long>> cacheStampMap;           //[farmID][dataComm]
    private HashMap<String, float[]> cacheFeedPerMap;                        //[id][data]

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

    public String getUserID(){ return userID; }
    public String getUserPW(){ return userPW; }
    public String getSelectFarm(){ return selectFarm; }
    public String getSelectDong(){ return selectDong; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public JSONObject getCacheData(String key){
        return cacheDataMap.getOrDefault(selectFarm, new HashMap<>()).getOrDefault(key, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public JSONObject getCacheData(String farm, String key){
        return cacheDataMap.getOrDefault(farm, new HashMap<>()).getOrDefault(key, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String key, HashMap<String, String> map){
        return getCacheData(selectFarm, key, map);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String farm, String key, HashMap<String, String> map){

        JSONObject ret;

        if(key == null) return null;

        if(needRefresh(farm, key)){
            ret = getApiJson(map);
            cacheDataMap.get(farm).put(key, ret);
            cacheStampMap.get(farm).put(key, DateUtil.get_inst().get_now_timestamp());
        }
        else{
            ret = cacheDataMap.get(farm).get(key);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean needRefresh(String key){
        return needRefresh(selectFarm, key);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean needRefresh(String farm, String key){

        boolean ret = false;

        if(!cacheDataMap.containsKey(farm)){
            cacheDataMap.put(farm, new HashMap<>());
            cacheStampMap.put(farm, new HashMap<>());
            return true;
        }

        if(cacheDataMap.get(farm).containsKey(key)){
            long prev = cacheStampMap.get(farm).get(key);
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

        String farm = id.length() > 6 ? id.substring(0, 6) : id;

        JSONObject buffer = getCacheData(farm, "buffer");
        if(buffer == null){
            buffer = loadBufferData(farm);
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
    public JSONObject loadBufferData(){
        return loadBufferData(selectFarm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject loadBufferData(String farm){

        JSONObject json = getCacheData("buffer", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", userID);
            put("farmID", farm);
            put("setComm", "buffer");
        }});

        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject loadFcrData(){

        JSONObject json = getCacheData("fcr", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", userID);
            put("setComm", "fcr");
        }});

        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject loadDailyFeedBreedData(){
        return loadDailyFeedBreedData(selectFarm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject loadDailyFeedBreedData(String farm){

        JSONObject json = getCacheData("feedPer", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", userID);
            put("farmID", farm);
            put("setComm", "feedPer");
        }});

        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public float[] getFeedPerData(String id){

        String farm = id.length() > 6 ? id.substring(0, 6) : id;

        if(cacheFeedPerMap.containsKey(id) && !needRefresh("feedPer")){
            return cacheFeedPerMap.get(id);
        }

        JSONObject json = loadDailyFeedBreedData(farm);
        JSONObject buffer = loadBufferData(farm);

        LinkedHashMap<String, HashMap<String, Integer>> totalList = new LinkedHashMap<>();

        try {
            for (Iterator<String> it = json.keys(); it.hasNext(); ){
                String key = it.next();
                JSONObject dongJson = json.getJSONObject(key);
                JSONObject dongBuffer = buffer.getJSONObject(key);

                int inCount = dongBuffer.getInt("cmInsu") + dongBuffer.getInt("cmExtraSu");
                int live = 0;
                int death = 0;
                float feedPer = 0f;
                float waterPer = 0f;

                String inDate = dongBuffer.getString("cmIndate").substring(0, 10);
                String outDate = dongBuffer.getString("cmOutdate");
                String endDate = outDate.length() < 2 ? DateUtil.get_inst().get_now() : outDate;
                endDate = endDate.substring(0, 10);

                String loopDate = inDate;

                // 동별로 구하기
                while(true){

                    if(!dongJson.isNull(loopDate)){

                        if(!totalList.containsKey(loopDate)){
                            totalList.put(loopDate, new HashMap<>());
                        }
                        HashMap<String, Integer> totalMap = totalList.get(loopDate);

                        JSONObject jo = dongJson.getJSONObject(loopDate);

                        int feed = jo.getInt("feed") * 1000;
                        feed += inDate.equals(loopDate) ? dongBuffer.getInt("cmAlreadyFeed") * 1000 : 0;

                        int water = jo.getInt("water");

                        live = inCount - death;

                        feedPer += FloatCompute.divide(feed, live);
                        waterPer += FloatCompute.divide(water, live);

                        death += jo.getInt("death") + jo.getInt("cull") + jo.getInt("thinout");

                        totalMap.put("feed", totalMap.getOrDefault("feed", 0) + feed);
                        totalMap.put("water", totalMap.getOrDefault("water", 0) + water);
                        totalMap.put("live", totalMap.getOrDefault("live", 0) + live);

//                        Log.e("getFeed", feed + " " + water + " " + live);
                    }

                    if(loopDate.equals(endDate)){
                        break;
                    }

                    loopDate = DateUtil.get_inst().get_plus_minus_minute_time(loopDate + " 12:00:00", 24 * 60);
                    loopDate = loopDate.substring(0, 10);
                }

                float[] data = new float[]{feedPer, waterPer, 0f};
                cacheFeedPerMap.put(key, data);
//                Log.e("getFeedPerData", json.getJSONObject(key).toString());
            }

            float feedPer = 0f;
            float waterPer = 0f;
            // 동 전체 구하기
            for(Map.Entry<String, HashMap<String, Integer>> entry : totalList.entrySet()){
                String date = entry.getKey();
                HashMap<String, Integer> map = entry.getValue();

                feedPer += FloatCompute.divide(map.get("feed"), map.get("live"));
                waterPer += FloatCompute.divide(map.get("water"), map.get("live"));

//                Log.e("date", date);
            }

            float[] data = new float[]{feedPer, waterPer, 0f};
            cacheFeedPerMap.put(farm, data);


        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        return cacheFeedPerMap.get(id);

    }
}
