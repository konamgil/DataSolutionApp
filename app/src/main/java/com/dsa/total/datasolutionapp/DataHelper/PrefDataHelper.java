package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.dsa.total.datasolutionapp.DataAdapter.ListAdapter;
import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

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

    //삭제
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteJsonFileInPref(int position){

        String strPref = mPrefs.getString("MyJson", null);
        Gson gson = new Gson();

        List<phoneBookItemObject> sectionlist = gson.fromJson(strPref, new TypeToken<List<phoneBookItemObject>>(){}.getType());
        ArrayList<phoneBookItemObject> itemArrayList = new ArrayList<phoneBookItemObject>(sectionlist);
        ListAdapter mListAdapter = new ListAdapter(mContext);
        phoneBookItemObject thisItem = (phoneBookItemObject)mListAdapter.getItem(position);
        itemArrayList.remove(thisItem);

        String json = gson.toJson(itemArrayList);
        editor.putString("MyJson", json);
        editor.commit();

    }
    /**
     *  프리퍼런스에 json 로 바꾸어 저장한다
     */
    public void saveJsonFileInPref(int _id, String telName,String telNumber, String telAddress){

        Gson gson = new Gson();
        String result = mPrefs.getString("MyJson","");
        List<phoneBookItemObject> sectionlist = null;
        ArrayList<phoneBookItemObject> itemArrayList = new ArrayList<phoneBookItemObject>();

        if(result.equals("[]")){
            editor.putString("MyJson", "");
            editor.commit();
            String json = gson.toJson(itemArrayList);
            sectionlist = gson.fromJson(json, new TypeToken<List<phoneBookItemObject>>(){}.getType());
            itemArrayList = new ArrayList<phoneBookItemObject>(sectionlist);
            itemArrayList.add(new phoneBookItemObject(_id,telName,telNumber,telAddress,"Preference"));
        }

        String json = gson.toJson(itemArrayList);
        editor.putString("MyJson", json);
        editor.commit();
    }

    public ArrayList getJsonFileFromPref(){

        String strPref = mPrefs.getString("MyJson", null);

        if(strPref != null) {
            // do some thing
            Gson gson = new Gson();
            String result = mPrefs.getString("MyJson","");
            if(result.equals("")){
                return new ArrayList<phoneBookItemObject>();
            }
            List<phoneBookItemObject> sectionlist = gson.fromJson(result, new TypeToken<List<phoneBookItemObject>>(){}.getType());
            ArrayList<phoneBookItemObject> itemArrayList = new ArrayList<phoneBookItemObject>(sectionlist);

            return itemArrayList;
        }
        return new ArrayList<phoneBookItemObject>();

    }
}
