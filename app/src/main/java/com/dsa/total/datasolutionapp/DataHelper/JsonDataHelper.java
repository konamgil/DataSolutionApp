package com.dsa.total.datasolutionapp.DataHelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by konamgil on 2017-05-19.
 */

public class JsonDataHelper {

    private Context mContext;
    //assets 폴더에 있는 json 파일의 이름이다
    private String JsonDataFileName = "jsonphonebook.json";
    private JSONArray jarray = null;
    private ArrayList<phoneBookItemObject> mArrayList;
    /**
     * 생성자
     * @param context
     */
    public JsonDataHelper(Context context) {
        mContext = context;
        mArrayList = new ArrayList<phoneBookItemObject>();
        jarray = getJsonArrayDataFromFile();
    }


    /**
     * json array에 jObject add하기
     * @param jObject
     */
    public void addJsonObjectatArray(JSONObject jObject){
        //현재 jarray에 jobject를 put을 하고
        jarray.put(jObject);
        //적용된 jarray를 파일로 출력한다
        saveJsonFile(jarray);
    }

    //새로 만드는중
    public ArrayList getJsonData(){
        JSONArray dataArray  = getJsonArrayDataFromFile();

        for(int i =0; i<dataArray.length(); i++){
            try {
                JSONObject jObject = dataArray.getJSONObject(i);
                int _id = jObject.getInt("_id");
                String name = jObject.getString("name");
                String addr = jObject.getString("addr");
                String tel = jObject.getString("tel");
                String telFromDataHelper = jObject.getString("telFromDataHelper");

                mArrayList.add(new phoneBookItemObject(_id,name,addr,tel,telFromDataHelper));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mArrayList;
    }

    public void insertJsonData(int _id, String name, String addr, String tel,String Json){
        JSONObject newJsonObject = makeJsonObject(_id, name, addr, tel, Json);
        jarray.put(newJsonObject);
        saveJsonFile(jarray);
    }

    /**
     * //_id 값 받아와서 jarray를 돌면서 일치하는 _id가 있으면 해당 오브젝트의 순서를 삭제
     * @param _id
     */
    public void deleteJsonFile(int _id){
        int len = jarray.length();
        if(jarray != null){
            for(int i=0; i<len; i++){
                try {
                    int _idKey = jarray.getJSONObject(i).getInt("_id");
                    if(_idKey == _id){
                        jarray.remove(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        saveJsonFile(jarray);
    }

    /**
     * 수정
     * @param _id_edit
     * @param getName
     * @param getAddr
     * @param getTell
     * @param selectedDataStore
     */
    public void updateJsonFile(int _id_edit, String getName, String getAddr, String getTell, String selectedDataStore){
        JSONObject jObject = makeJsonObject(_id_edit, getName, getAddr, getTell,selectedDataStore);
        int len = jarray.length();
        if(jarray != null){
            for(int i=0; i<len; i++){
                try {
                    int _idKey = jarray.getJSONObject(i).getInt("_id");
                    if(_idKey == _id_edit){
                        jarray.remove(i);
                        jarray.put(i,jObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        saveJsonFile(jarray);
    }

    /**
     * asset 폴더에 있는 json 파일을 로드한다
     * @return 읽어들인 json 파일의 문자열 정보가 담겨있다
     */
    private JSONArray getJsonArrayDataFromFile() {
        String json = null;
        FileInputStream is = null;
        InputStream iss = null;
        JSONArray currentDataJsonData = null;
        try {
            File isFile = new File(mContext.getFilesDir() +"/"+ JsonDataFileName);

            if(isFile.exists() == false){

                iss = mContext.getAssets().open(JsonDataFileName); //assets 에서 가져오기
                int size = iss.available();
                byte[] buffer = new byte[size];
                iss.read(buffer);
                iss.close();
                json = new String(buffer, "UTF-8");

                File file = new File(mContext.getFilesDir() +"/");

                if( !file.exists() ) {  // 원하는 경로에 폴더가 있는지 확인
                    file.mkdirs();
                }
                copyFileFromAssets();
            }else {
                    is = new FileInputStream(mContext.getFilesDir() +"/"+ JsonDataFileName);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            currentDataJsonData = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currentDataJsonData;
    }

    /**
     * 이름,주소,전화번호를 받아서 jsonObject로 만들어준다
     * @param name
     * @param addr
     * @param tel
     * @return
     */
    private JSONObject makeJsonObject(int _id, String name, String addr, String tel,String Json){
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("_id", _id);
            jObject.put("addr" ,addr);
            jObject.put("name" ,name);
            jObject.put("tel" ,tel);
            jObject.put("telFromDataHelper",Json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jObject;
    }

    /**
     * 인자 값으로 받은 jarray 를 폴더에 파일로 출력 저장한다
     * @param mjarray
     */
    public void saveJsonFile(JSONArray mjarray){
        try {
            Writer output = null;
            File file = null;
            file = new File(mContext.getFilesDir() +"/"+ JsonDataFileName);
            output = new BufferedWriter(new FileWriter(file));
            output.write(mjarray.toString());
            output.close();
            Toast.makeText(mContext, "Composition saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * assets의 제이슨 파일을 내부패키지 폴더로 복사하기
     */
    public void copyFileFromAssets() {
        InputStream is = null;
        FileOutputStream fos = null;
//        File outDir = new File(mContext.getFilesDir() +"/");
//        outDir.mkdirs();

        try {
            is = mContext.getAssets().open(JsonDataFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            File outfile = new File(mContext.getFilesDir() +"/"+ JsonDataFileName);
            fos = new FileOutputStream(outfile);
            for (int c = is.read(buffer); c != -1; c = is.read(buffer)) {
                fos.write(buffer, 0, c);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



