package com.kokofarm.kokofarm_user_app.manager;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.kokofarm.kokofarm_user_app.R;
import com.kokofarm.kokofarm_user_app.kkf_utils.BackTasker;
import com.kokofarm.kokofarm_user_app.kkf_utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
    private static final String API_URL = "http://api.kokofarm.co.kr/contents/";
    private static final String API_NAME = "android_api.php";
    private static final int REFRESH_TIME = 300;

    // 멤버 선언부
    private boolean loginStatus = false;

    private String userType = "";
    private String userID = "";
    private String userPW = "";
    private String selectFarm = "";
    private String selectDong = "";

    private HashMap<String, HashMap<String, JSONObject>> cacheDataMap;      //[farmID][dataComm]
    private HashMap<String, HashMap<String, Long>> cacheStampMap;           //[farmID][dataComm]

    public void setUserType(String userType) {
        this.userType = userType;
    }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DataCacheManager getInstance(){
        if(instance == null){
            instance = new DataCacheManager();
        }

        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DataCacheManager(){

        Log.e("DataCacheManager", "INIT");

        cacheDataMap = new HashMap<>();
        cacheStampMap = new HashMap<>();
    }

    public String getUserType(){ return userType; }
    public String getUserID(){ return userID; }
    public String getUserPW(){ return userPW; }
    public String getSelectFarm(){ return selectFarm; }
    public String getSelectDong(){ return selectDong; }
    public String getSelectKey(){ return selectFarm + selectDong;}
    public HashMap<String, HashMap<String, JSONObject>> getCacheDataMap(){ return cacheDataMap; }

    public boolean isLogin(){
        return loginStatus;
    }

    public int login(String id, String pw){

        int ret = 1;

        String response = getApiData("login_api.php", new HashMap<String, String>() {{
            put("setComm", "login");
            put("userID", id);
            put("userPW", pw);
            put("apiKey", API_KEY);
        }});

        Log.e("response", response);

        try {
            JSONObject jo = new JSONObject(response);
            if(jo.getString("errCode").equals("00")){
                JSONObject retData = jo.getJSONObject("retData");

                setUserType(retData.getString("userType"));
                setUserID(retData.getString("userID"));
                setUserPW(retData.getString("userPW"));

                if(userType.equals("farm")){
                    setSelectFarm(retData.getString("farmID"));
                }
//                setSelectFarm(retData.getString("farmID"));

                loginStatus = true;
            }

            ret = Integer.parseInt(jo.getString("errCode"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String type){
//        return cacheDataMap.getOrDefault(selectFarm, new HashMap<>()).getOrDefault(key, null);
        return getCacheData(selectFarm, selectDong, "", type);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String code, String type){
//        return cacheDataMap.getOrDefault(selectFarm, new HashMap<>()).getOrDefault(key, null);
        return getCacheData(selectFarm, selectDong, code, type);
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public JSONObject getCacheData(String farm, String key){
//        return cacheDataMap.getOrDefault(farm, new HashMap<>()).getOrDefault(key, null);
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public JSONObject getCacheData(String key, HashMap<String, String> map){
//        return getCacheData(selectFarm, key, map);
//    }

    // 입출하코드(code)로 분기되는 데이터 : sensorHistory, avgWeight
    // 농장(farm)으로 분기되는 데이터 : buffer, cell, outSensor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getCacheData(String farm, String dong, String code, String type){

        JSONObject ret;

        if(type == null) return null;

        String key = code.equals("") ? farm : code;

        if(needRefresh(key, type)){

            HashMap<String, String> map = new HashMap<String, String>() {{
                put("userType", userType);
                put("userID", userID);
                put("farmID", farm);
                put("dongID", dong);
                put("code", code);
                put("setComm", type);
            }};

            ret = getApiJson(map);
            cacheDataMap.get(key).put(type, ret);
            cacheStampMap.get(key).put(type, DateUtil.get_inst().get_now_timestamp());
        }
        else{
            ret = cacheDataMap.get(key).get(type);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getAllBuffer(){

        JSONObject ret;

        String key = "manager";
        String type = "allBuffer";

        if(needRefresh(key, type)){

            HashMap<String, String> map = new HashMap<String, String>() {{
                put("userType", userType);
                put("userID", userID);
                put("setComm", type);
            }};

            long nowStamp = DateUtil.get_inst().get_now_timestamp();

            ret = getApiJson(map);
            cacheDataMap.get(key).put(type, ret);
            cacheStampMap.get(key).put(type, nowStamp);

            // 전체 데이터를 가져온 버퍼를 각 동 버퍼에 담는 작업
            // [KF000601][rowJson] 형식의 데이터를 순차적으로 돌면서
            // [KF0006][buffer][KF000601][rowJson] 형식으로 넣어주는 작업
            for (Iterator<String> it = ret.keys(); it.hasNext(); ){
                String id = it.next();      // ex) KF000601
                try {
                    JSONObject row = ret.getJSONObject(id);      // ex) KF000601 의 데이터

                    String farm = id.substring(0, 6);       // KF0006

                    // KF0006의 data와 stamp를 가져옴
                    HashMap<String, JSONObject> dataMap = cacheDataMap.getOrDefault(farm, new HashMap<>());
                    HashMap<String, Long> stampMap = cacheStampMap.getOrDefault(farm, new HashMap<>());

                    // KF0006의 버퍼를 현재시간에 갱신했다고 입력
                    stampMap.put("buffer", nowStamp);       // 스탬프를 입력함

                    // KF0006의 버퍼를 가져와서 버퍼에 KF000601 데이터를 담음
                    JSONObject bufferJson = dataMap.getOrDefault("buffer", new JSONObject());     // 버퍼를 찾음
                    bufferJson.put(id, row);
                    dataMap.put("buffer", bufferJson);

                    // 최종 다 담아진 data와 stamp를 KF0006에 갱신시킴
                    cacheStampMap.put(farm, stampMap);
                    cacheDataMap.put(farm, dataMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        else{
            ret = cacheDataMap.get(key).get(type);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getFarmList(){

        JSONObject ret;

        String key = "manager";
        String type = "farmList";

        if(needRefresh(key, type)){

            HashMap<String, String> map = new HashMap<String, String>() {{
                put("userType", userType);
                put("userID", userID);
                put("setComm", type);
            }};

            long nowStamp = DateUtil.get_inst().get_now_timestamp();

            ret = getApiJson(map);
            cacheDataMap.get(key).put(type, ret);
            cacheStampMap.get(key).put(type, nowStamp);

        }
        else{
            ret = cacheDataMap.get(key).get(type);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject getComeoutList(String farm, int start, int end){

        // stamp 저장 안함 계속 갱신
        JSONObject ret;

        String key = farm.equals("") ? "manager" : farm;
        String type = "comeoutList";

        if(needRefresh(key, type)){

            HashMap<String, String> map = new HashMap<String, String>() {{
                put("userType", userType);
                put("userID", userID);
                put("setComm", type);
                put("farmID", farm);
                put("start", Integer.toString(start));
                put("end", Integer.toString(end));
            }};

//            long nowStamp = DateUtil.get_inst().get_now_timestamp();

            ret = getApiJson(map);
            cacheDataMap.get(key).put(type, ret);
//            cacheStampMap.get(key).put(type, nowStamp);

        }
        else{
            ret = cacheDataMap.get(key).get(type);
        }

        return ret;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private boolean needRefresh(String key){
//        return needRefresh(selectFarm, key);
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean needRefresh(String key, String type){

        boolean ret = false;

        if(!cacheDataMap.containsKey(key)){
            cacheDataMap.put(key, new HashMap<>());
            cacheStampMap.put(key, new HashMap<>());
            return true;
        }

        if(cacheDataMap.get(key).containsKey(type)){
            long prev = cacheStampMap.get(key).getOrDefault(type, 0l);
            long curr = DateUtil.get_inst().get_now_timestamp();

            if(curr - prev > REFRESH_TIME){
                ret = true;
            }
        }
        else{
            ret = true;
        }

        return ret;
    }

    public void deleteCache(String key, String type){
        if(cacheStampMap.containsKey(key)){
            HashMap<String, Long> map = cacheStampMap.get(key);

            if(map.containsKey(type)){
                map.remove(type);
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public JSONObject getBufferData(String id){
//
//        String farm = id.length() > 6 ? id.substring(0, 6) : id;
//
//        JSONObject buffer = getCacheData(farm, "buffer");
//        if(buffer == null){
//            buffer = loadBufferData(farm);
//        }
//
//        JSONObject ret = null;
//
//        if(buffer != null){
//            try {
//                ret = buffer.getJSONObject(id);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return ret;
//    }

    private String getApiData(String apiName, HashMap<String, String> request){
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

//                    URL url = new URL(API_URL + apiName + "?" + postData);
                    URL url = new URL(API_URL + apiName + "?" + postData);

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

    private String getApiData(HashMap<String, String> request){
        return getApiData(API_NAME, request);
    }

    private JSONObject getApiJson(HashMap<String, String> map){

        try {
            String data = getApiData(map);

//            Log.e("data", data);
            JSONObject jo = new JSONObject(data);

            if(jo.get("errCode").equals("00")){
                return jo.getJSONObject("retData");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    public String getDustStatus(int val){
        String ret = PageManager.getInstance().getRString(R.string.good);

        if(val >= 16 && val < 36) ret = PageManager.getInstance().getRString(R.string.moderate);
        else if(val >= 36 && val < 76) ret = PageManager.getInstance().getRString(R.string.unhealthy);
        else if(val >= 76) ret = PageManager.getInstance().getRString(R.string.very_unhealthy);
        else if(val < 0) ret = "-";

        return ret;
    }

    public String getWindDirection(int val){

        String ret = "-";

        switch(val){
            case 0:
            case 360:
                ret = PageManager.getInstance().getRString(R.string.north);
                break;
            case 45:
                ret = PageManager.getInstance().getRString(R.string.northeast);
                break;
            case 90:
                ret = PageManager.getInstance().getRString(R.string.east);
                break;
            case 135:
                ret = PageManager.getInstance().getRString(R.string.southeast);
                break;
            case 180:
                ret = PageManager.getInstance().getRString(R.string.south);
                break;
            case 225:
                ret = PageManager.getInstance().getRString(R.string.southwest);
                break;
            case 270:
                ret = PageManager.getInstance().getRString(R.string.west);
                break;
            case 315:
                ret = PageManager.getInstance().getRString(R.string.northwest);
                break;
            default :
                ret ="-";
                break;
        }

        return ret;
    }

    public int getLiveCnt(JSONObject dongJson){

        int live = 0;
        try {

            live = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu");
            live -= dongJson.getInt("cmDeathCount");
            live -= dongJson.getInt("cmCullCount");
            live -= dongJson.getInt("cmThinoutCount");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return live;
    }

    // 사육일지 데이터 인서트
    public void inputApiData(String comm, HashMap<String, String> dataMap){
        HashMap<String, String> map = new HashMap<String, String>() {{
            put("userType", userType);
            put("userID", userID);
            put("setComm", comm);
        }};

        for(Map.Entry<String, String> entry : dataMap.entrySet()){
            map.put(entry.getKey(), entry.getValue());
        }

        getApiData(map);
    }

}
