package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;

import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by konamgil on 2017-05-19.
 */

public class JsonDataHelper {

    private Context mContext;
    //assets 폴더에 있는 json 파일의 이름이다
    private String JsonDataFileName = "jsonphonebook.json";
    private JSONArray jarray = null;
    private String rawDataFromJson = null;
    /**
     * 생성자
     * @param context
     */
    public JsonDataHelper(Context context) {
        this.mContext = context;
        this.rawDataFromJson = loadJSONFromAsset();

        //json file 로부터 데이터 받아들임
        try {

            this.jarray = new JSONArray(rawDataFromJson);
            this.jarray = getJsonData();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void addJsonObjectatArray(JSONObject jObject){
        jarray.put(jObject);
    }

    /**
     * json 파일의 담겨있는 문자열 정보를 array에 담아서 array를 반환한다
     * @return JSONArray
     */
    public JSONArray getJsonData(){
        return jarray;
    }

    /**
     * asset 폴더에 있는 json 파일을 로드한다
     * @return 읽어들인 json 파일의 문자열 정보가 담겨있다
     */
    private String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = mContext.getAssets().open(JsonDataFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    /**
     * 이름,주소,전화번호를 받아서 jsonObject로 만들어준다
     * @param name
     * @param addr
     * @param tel
     * @return
     */
    public JSONObject makeJsonObject(int _id, String name, String addr, String tel,String Json){
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("_id", _id);
            jObject.put("name" ,name);
            jObject.put("addr" ,addr);
            jObject.put("tel" ,tel);
            jObject.put("telFromDataHelper",Json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jObject;
    }

}
