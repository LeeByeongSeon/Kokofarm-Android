package com.example.kokofarm_user_app;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UtilFunction {

    private static final String API_KEY = "06071227041701229789";

    // 토스트 메시지 출력
    public static void toast_short(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void toast_long(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean file_exist(String file_path){

        try{
            File file = new File(file_path);

            if(file.exists()){
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static String file_read(String file_path){
        String ret = "";

        try{
            File file = new File(file_path);
            if(file.exists()) {
                BufferedReader fp = new BufferedReader(new FileReader(file));

                String line = "";
                while ((line = fp.readLine()) != null){
                    ret += line;
                }
                fp.close();
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean file_write(String file_path, String contents){
        try{
            File file = new File(file_path);

            if(!file.exists()) {                                 //파일이 없으면
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            BufferedWriter fp = new BufferedWriter(new FileWriter(file, false));
            fp.write(contents);
            fp.close();

            return true;

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean file_delete(String file_path){
        try{
            File file = new File(file_path);

            if(file.exists()) {                                 //파일이 없으면
                file.delete();
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean private_file_exist(Context context, String file_path){
        return file_exist(context.getFilesDir() + file_path);
    }

    public static String private_file_read(Context context, String file_path){
        return file_read(context.getFilesDir() + file_path);
    }

    public static boolean private_file_write(Context context, String file_path, String contents){
        return file_write(context.getFilesDir() + file_path, contents);
    }

    public static boolean private_file_delete(Context context, String file_path){
        return file_delete(context.getFilesDir() + file_path);
    }

    public static String get_api_data(HashMap<String, String> request){
        String ret = "";

        try {
            ret = new RestAPITask(request).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static class RestAPITask extends AsyncTask<String, Void, String>{

        final String API_URL = "http://api.kokofarm.co.kr/contents/android_api.php";
        HashMap<String, String> request;

        public RestAPITask(HashMap<String, String> request){
            this.request = request;
        }

        @Override
        protected String doInBackground(String... strings) {

            String recvMsg = "";
            try{
                //URL url = new URL(API_URL);

                String postData = "apiKey=" + API_KEY;

                if(request == null) return recvMsg;
                for(Map.Entry<String, String> entry : request.entrySet()){
                    postData += "&" + entry.getKey() + "=" + entry.getValue();
                }

                Log.e("postData", postData);

                URL url = new URL(API_URL + "?" + postData);
                //URL url = new URL(API_URL);

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
                recvMsg = buffer.toString();

                reader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recvMsg;
        }
    }
}
