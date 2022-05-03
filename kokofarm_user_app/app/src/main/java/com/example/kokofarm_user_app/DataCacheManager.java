package com.example.kokofarm_user_app;

public class DataCacheManager {

    private static DataCacheManager instance = null;

    public static DataCacheManager getInstance(){
        if(instance == null){
            instance = new DataCacheManager();
        }

        return instance;
    }

    private DataCacheManager(){}

    private String farmID;
    private String dongID;

    

}
