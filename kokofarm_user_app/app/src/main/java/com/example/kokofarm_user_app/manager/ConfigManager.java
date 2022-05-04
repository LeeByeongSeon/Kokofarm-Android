package com.example.kokofarm_user_app.manager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.kokofarm_user_app.kkf_utils.UtilFunction;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static ConfigManager instance = null;

    public static ConfigManager get_inst() {
        if(instance == null){
            instance = new ConfigManager();
        }
        return instance;
    }

    final String CONFIG_FILE_NAME = "/config/kokofarm_user.cfg";                               //설정파일 명
    final String URL_HEN = "http://app.kokofarm.co.kr/contents/0000.php";
    final String URL_LAY = "http://app.kokofarm.kr/contents/0000.php";

    final String MISO_HEN = "http://control.kokofarm.co.kr/app/contents/0000.php";

    HashMap<String, String> config_map = new HashMap<String, String>();

    private Context context;

    public ConfigManager(){

    }

    public void set_context(Context context){
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
        String ret = "";

        ret = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

        return ret;
    }

    public boolean readConfig(){

        String read = UtilFunction.private_file_read(context, CONFIG_FILE_NAME);

        if(read.length() < 5){
            return false;
        }

        try{
            String[] tokens = read.split(";");

            for(String token : tokens){
                String[] option = token.trim().split(":");

                if(option.length > 1){
                    config_map.put(option[0], option[1]);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(config_map.size() > 0){
            return true;
        }

        return false;
    }

    public String getOption(String key){
        String val = config_map.get(key);
        val = val == null ? "" : val;
        return val;
    }

    public boolean write_config(String m_id, String m_pw, String m_type){
        String write = "";
        write += "user_id:" + m_id + ";\n";
        write += "user_pw:" + m_pw + ";\n";
        write += "type:" + m_type + ";\n";

        return UtilFunction.private_file_write(context, CONFIG_FILE_NAME, write);
    }

    public boolean write_config(HashMap<String, String> map){
        String write = "";
        for (Map.Entry entry : map.entrySet()){
            write += entry.getKey() + "|" + entry.getValue() + ";\n";
        }

        return UtilFunction.private_file_write(context, CONFIG_FILE_NAME, write);
    }

    public boolean delete_config(){
        return  UtilFunction.private_file_delete(context, CONFIG_FILE_NAME);
    }
}
