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
    private String rawDataFromJson;
    private JSONArray jarray;

    /**
     * 생성자
     * @param context
     */
    public JsonDataHelper(Context context) {
        this.mContext = context;
    }


    /**
     * json 파일의 담겨있는 문자열 정보를 array에 담아서 array를 반환한다
     * @return JSONArray
     */
    public JSONArray getJsonData(){
        rawDataFromJson = loadJSONFromAsset();

        jarray = null;
        try {
            jarray = new JSONArray(rawDataFromJson);
//            for (int i=0; i<jarray.length(); i++){
//                 jObject = jarray.getJSONObject(i);
//                listdata.add(jarray.getString(i));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jarray;
    }

    /**
     * asset 폴더에 있는 json 파일을 로드한다
     * @return 읽어들인 json 파일의 문자열 정보가 담겨있다
     */
    public String loadJSONFromAsset() {
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
    public JSONObject makeJsonObject(String name, String addr, String tel){
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("name" ,name);
            jObject.put("addr" ,addr);
            jObject.put("tel" ,tel);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jObject;
    }

}
