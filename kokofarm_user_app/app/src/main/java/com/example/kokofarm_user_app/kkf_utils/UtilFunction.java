package com.example.kokofarm_user_app.kkf_utils;


import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UtilFunction {

    // 토스트 메시지 출력
    public static void toastShort(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void toastLong(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean fileExist(String file_path){

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

    public static String fileRead(String file_path){
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

    public static boolean fileWrite(String file_path, String contents){
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

    public static boolean fileDelete(String file_path){
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

    public static boolean privateFileExist(Context context, String file_path){
        return fileExist(context.getFilesDir() + file_path);
    }

    public static String privateFileRead(Context context, String file_path){
        return fileRead(context.getFilesDir() + file_path);
    }

    public static boolean privateFileWrite(Context context, String file_path, String contents){
        return fileWrite(context.getFilesDir() + file_path, contents);
    }

    public static boolean privateFileDelete(Context context, String file_path){
        return fileDelete(context.getFilesDir() + file_path);
    }
}
