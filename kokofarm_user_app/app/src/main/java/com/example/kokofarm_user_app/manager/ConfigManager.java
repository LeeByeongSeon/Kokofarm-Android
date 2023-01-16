package com.example.kokofarm_user_app.manager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.kokofarm_user_app.kkf_utils.UtilFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigManager {

    private static ConfigManager instance = null;

    public static ConfigManager getInstance() {
        if(instance == null){
            instance = new ConfigManager();
        }
        return instance;
    }

    final String CONFIG_FILE_NAME = "/config/kokofarm_app.cfg";                               //설정파일 명

    HashMap<String, String> configMap = new HashMap<>();

    private Context context;

    public ConfigManager(){

    }

    public void setContext(Context context){
        this.context = context;
    }

    public String getDirPath(){
        String ret = "";

        try{
            String sdcard = Environment.getExternalStorageState();

            if ( !sdcard.equals(Environment.MEDIA_MOUNTED)) {
                // SD카드가 마운트되어있지 않음
                ret = Environment.getRootDirectory().getAbsolutePath() + "/Kokofarm";
            }
            else {
                // SD카드가 마운트되어있음
                ret = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kokofarm";
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }

    public String getDownloadFolderPath(){
        String ret = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

        return ret;
    }

    public boolean readConfig(){

        String read = UtilFunction.privateFileRead(context, CONFIG_FILE_NAME);

        if(read.length() < 5){
            return false;
        }

        try{
            String[] tokens = read.split(";");

            for(String token : tokens){
                String[] option = token.trim().split(":");

                if(option.length > 1){
                    configMap.put(option[0], option[1]);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(configMap.size() > 0){
            return true;
        }

        return false;
    }

    public String getOption(String key){
        String val = configMap.get(key);
        val = val == null ? "" : val;
        return val;
    }

    public boolean writeConfig(String m_id, String m_pw, String m_type){
        String write = "";
        write += "userID:" + m_id + ";\n";
        write += "userPW:" + m_pw + ";\n";
        write += "userType:" + m_type + ";\n";

        return UtilFunction.privateFileWrite(context, CONFIG_FILE_NAME, write);
    }

    public boolean writeConfig(HashMap<String, String> map){
        String write = "";
        for (Entry entry : map.entrySet()){
            write += entry.getKey() + "|" + entry.getValue() + ";\n";
        }

        return UtilFunction.privateFileWrite(context, CONFIG_FILE_NAME, write);
    }

    public boolean deleteConfig(){
        return  UtilFunction.privateFileDelete(context, CONFIG_FILE_NAME);
    }
}
