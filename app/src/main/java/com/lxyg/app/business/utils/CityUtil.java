package com.lxyg.app.business.utils;

import android.content.Context;

import com.lxyg.app.business.bean.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by 王沛栋 on 2015/11/4.
 */
public class CityUtil {

    public static List<City> readCity(Context context){
        try{
            InputStreamReader inputStream = new InputStreamReader(context.getAssets().open("city.json"),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return JsonUtils.parseList(stringBuilder.toString(), City.class);


        }catch (Exception e){
            return null;
        }
    }


    private static List<City> mCityData;
    public static List<City> getCityList(Context context){
        if (mCityData == null){
            mCityData = new ArrayList<>();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("city.json"), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();

                mCityData = JsonUtils.parseList(stringBuilder.toString(),City.class);

//                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//                Log.i("TESTJSON", "cat=" + jsonObject.getString("cat"));
//                JSONArray jsonArray = jsonObject.getJSONArray("language");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    Log.i("TESTJSON", "----------------");
//                    Log.i("TESTJSON", "id=" + object.getInt("id"));
//                    Log.i("TESTJSON", "name=" + object.getString("name"));
//                    Log.i("TESTJSON", "ide=" + object.getString("ide"));
//                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mCityData;
    }

}
