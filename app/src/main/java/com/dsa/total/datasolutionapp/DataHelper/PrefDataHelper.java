package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void saveJsonFileInPref(int _id, String telName,String telNumber, String telAddress){

        Gson gson = new Gson();
        String result = mPrefs.getString("MyJson","");
        List<phoneBookItemObject> sectionlist = gson.fromJson(result, new TypeToken<List<phoneBookItemObject>>(){}.getType());
        ArrayList<phoneBookItemObject> itemArrayList = new ArrayList<phoneBookItemObject>(sectionlist);

        //json 전체 오브젝트를 문자열 String json에 담는다
//        ArrayList<phoneBookItemObject> phoneList  = new ArrayList<phoneBookItemObject>();
//        phoneList.add(new phoneBookItemObject(_id,telName,telNumber,telAddress,"Preference"));
        itemArrayList.add(new phoneBookItemObject(_id,telName,telNumber,telAddress,"Preference"));


//        phoneBookItemObject[] item = new phoneBookItemObject(_id,telName,telNumber,telAddress,"Preference");
//        String json = gson.toJson(phoneList);
        String json = gson.toJson(itemArrayList);

        editor.putString("MyJson", json);
        editor.commit();
    }

    public ArrayList getJsonFileFromPref(){
        Gson gson = new Gson();
        String result = mPrefs.getString("MyJson","");
        if(result.equals("")){
            return null;
        }
//        phoneBookItemObject[] itemArray = gson.fromJson(result, phoneBookItemObject[].class);
        List<phoneBookItemObject> sectionlist = gson.fromJson(result, new TypeToken<List<phoneBookItemObject>>(){}.getType());
//        List<phoneBookItemObject> itemList = Arrays.asList(itemArray);
        ArrayList<phoneBookItemObject> itemArrayList = new ArrayList<phoneBookItemObject>(sectionlist);



        return itemArrayList;
    }
}
