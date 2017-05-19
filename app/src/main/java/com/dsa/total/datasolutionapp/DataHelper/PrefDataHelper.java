package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

/**
 * Created by konamgil on 2017-05-19.
 */

// 프리퍼런스를 사용할 수 있게 도와주는 헬퍼 클래스이다
public class PrefDataHelper {
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;

    /**
     * 생성자
     * @param context
     */
    public PrefDataHelper(Context context) {
        this.mContext = context;
        openPrefEditor();
    }

    /**
     * 쉐어드프리퍼런스 에디터 열기
     */
    private void openPrefEditor(){
        //프리퍼런스 열고 edit을 초기화한다
        mPrefs = mContext.getSharedPreferences("phoneBook", Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }

    /**
     * pause 시점에 프리퍼런스에 json 파일들을 담을 것이다
     */
    public void saveJsonFileInPref(JSONObject jsonObject){

        Gson gson = new Gson();

        //json 전체 오브젝트를 문자열 String json에 담는다
        String json = gson.toJson(jsonObject);

        editor.putString("MyJson", json);
        editor.commit();
    }

    public JsonObject getJsonFileFromPref(){
        Gson gson = new Gson();
        String json = mPrefs.getString("MyJson","");
        JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();
//        JSONObject jsonData = new JSONObject(jsonObject);
//        jsonObject.
        return jsonObject;
    }
}
