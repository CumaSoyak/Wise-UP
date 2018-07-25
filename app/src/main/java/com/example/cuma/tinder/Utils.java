package com.example.cuma.tinder;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.cuma.tinder.Class.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Utils {

    //TODO Soruları json dosyasının içindende random çekmem lazım yoksa hep 1. sorudan başlar

    private  static final String TAG="Utils";
    public static List<Profile> profileList;

    public void key(int anahtar){

    }

    public static List<Profile> loadProfiles(Context context,int anahtar){
        int kategori=anahtar;
        try {
            GsonBuilder gsonBuilder=new GsonBuilder();
            Gson gson=gsonBuilder.create();
            if (kategori==1){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"tarih.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
                }
           else if (kategori==2){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"bilim.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
            }
            else if (kategori==3){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"eglence.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
            }
            else if (kategori==4){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"cografya.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
            }
            else if (kategori==5){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"sanat.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
            }
            else if (kategori==6){
                JSONArray array=new JSONArray(loadJSONFromAsset(context,"spor.json"));
                profileList=new ArrayList<>();

                for (int i = 0; i <array.length() ; i++){
                    Profile profile=gson.fromJson(array.getString(i),Profile.class);
                    profileList.add(profile);

                }
            }
            return profileList;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        }
        private static String loadJSONFromAsset(Context context,String jsonFileName){
            String json=null;
            InputStream is=null;
            try {
                AssetManager manager=context.getAssets();
                Log.e(TAG,"path"+jsonFileName);
                is=manager.open(jsonFileName);
                int size=is.available();
                byte [] buffer=new byte[size];
                is.read(buffer);
                is.close();
                json=new String(buffer,"UTF-8");

            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
            return json;
        }

}
