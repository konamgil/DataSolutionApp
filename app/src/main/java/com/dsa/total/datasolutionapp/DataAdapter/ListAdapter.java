package com.dsa.total.datasolutionapp.DataAdapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsa.total.datasolutionapp.DataHelper.JsonDataHelper;
import com.dsa.total.datasolutionapp.DataHelper.PrefDataHelper;
import com.dsa.total.datasolutionapp.DataHelper.XmlDataHelper;
import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.dsa.total.datasolutionapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by konamgil on 2017-05-19.
 */

public class ListAdapter extends BaseAdapter {

    private Context mContext;

    //listadapter 부분
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    //원본 리스트
    private ArrayList<phoneBookItemObject> phoneBookList;

    //여벌 리스트
    private ArrayList<phoneBookItemObject> arraylist;

    //jsonhelper 선언
    private JsonDataHelper jsonDataHelper;

    //SQLitehelper 선언
    private DataBaseAdapter dataBaseAdapter;

    //XMLhelper 선언
    private XmlDataHelper xmlDataHelper;

    //Prefhelper 선언
    private PrefDataHelper prefDataHelper;

    /**
     * 생성자
     * @param context
     */
    public ListAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

        //각 리스트들을 초기화한다
        this.phoneBookList = new ArrayList<phoneBookItemObject>();
        this.arraylist = new ArrayList<phoneBookItemObject>();

        //phoneBookList에 json 파일로부터 읽어들인 데이터를 가져온다
        jsonDataHelper = new JsonDataHelper(mContext);
        getDataListFromJson();

        //phoneBookList에 sqlite db로부터 읽어들인 데이터를 가져온다
        dataBaseAdapter = new DataBaseAdapter(mContext);
        getDataListFromSQLite();

        //phoneBookList에 sqlite XML로부터 읽어들인 데이터를 가져온다
        xmlDataHelper = new XmlDataHelper(mContext);
        getDataListFromXML();

        //phoneBookList에 sqlite Pref로부터 읽어들인 데이터를 가져온다
        prefDataHelper = new PrefDataHelper(mContext);
        getDataListFromPref();

        //기존의 phoneBookLiST를 여벌의 arraylist에 복사한다.
        this.arraylist.addAll(phoneBookList);
    }

    public ArrayList getAllDataList(){
        return this.arraylist;
    }

    /**
     * 새로 추가되는 jsonobject를 여벌의 list에 담는다
     * @param jObject
     */
    public void addObjectSecondList(JSONObject jObject){
        try {
            int _id = jObject.getInt("_id");
            String name = jObject.getString("name");
            String addr = jObject.getString("addr");
            String tel = jObject.getString("tel");
            String telFromDataHelper = jObject.getString("telFromDataHelper");
            arraylist.add(new phoneBookItemObject(_id,name,addr,tel,telFromDataHelper));
        } catch (JSONException j){
            j.printStackTrace();
        }
    }

    //두번째 여벌의 arraylist에 삽입
    private void addObjectSecondList(int _id, String name, String addr, String tel, String telFromDataHelper){
        arraylist.add(new phoneBookItemObject(_id,name,addr,tel,telFromDataHelper));
    }
    //두번째 여벌의 arraylist에 삭제
    private void removeObjectSecondList(phoneBookItemObject itemObject){
        arraylist.remove(itemObject);
    }
    /**
     * dataArray(제이슨 어레이로부터 제이슨 오브젝트를 하나하나 리스트에 add를 한다
     */
    public void addJsonObject(JSONObject jObject){

        //읽어들인 jsonaraay에 새로운 jsonobject를 담고 json 파일을 저장한다
        jsonDataHelper.addJsonObjectatArray(jObject);

        //기존 phonebooklist를 클리어 하고
        phoneBookList.clear();

        //새롭게 phoneBookList를 json으로부터 불러온다
        getDataListFromJson();

        //새롭게 phoneBookList를 sql로부터 불러온다
        getDataListFromSQLite();

        //새롭게  phoneBookList를 xml로부터 불러온다
        getDataListFromXML();

        //새롭게 phonecBookList를 pref로부터 불러온다
        getDataListFromPref();

        //클리어 후 여벌 리스트에 복사
        arraylist.clear();
        arraylist.addAll(phoneBookList);
        //데이터 구성의 변경이 있음을 아답터에게 알리고 리스트뷰를 갱신하도록 한다
        notifyDataSetChanged();
    }

    public void refreshData(){

        phoneBookList.clear();
        arraylist.clear();

        //새롭게 phoneBookList를 json으로부터 불러온다
        getDataListFromJson();

        //새롭게 phoneBookList를 sql로부터 불러온다
        getDataListFromSQLite();

        //새롭게  phoneBookList를 xml로부터 불러온다
        getDataListFromXML();

        //새롭게 phonecBookList를 pref로부터 불러온다
        getDataListFromPref();

        arraylist.addAll(phoneBookList);

        //데이터 구성의 변경이 있음을 아답터에게 알리고 리스트뷰를 갱신하도록 한다
        notifyDataSetChanged();
    }

    /**
     * 읽어들인 jsonArray로부터 개별적인 object를 꺼내어서 phonebooklist에 담는다
     */
    public void getDataListFromJson(){
        //json 데이터 처리 부분

        JSONArray dataArray = new JsonDataHelper(mContext).getJsonData();
        //json end
        // 테스트 데이터
        // dataArray.put(jsonDataHelper.addJsonFile("고남길","부산","010"));
        for(int i =0; i<dataArray.length(); i++){
            try {
                JSONObject jObject = dataArray.getJSONObject(i);
                int _id = jObject.getInt("_id");
                String name = jObject.getString("name");
                String addr = jObject.getString("addr");
                String tel = jObject.getString("tel");
                String telFromDataHelper = jObject.getString("telFromDataHelper");

                phoneBookList.add(new phoneBookItemObject(_id,name,tel,addr,telFromDataHelper));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * sqlite로 부터 데이터 가져와서 phonebooklist에 담기
     */
    private void getDataListFromSQLite(){
        //database 열기
        dataBaseAdapter.createDateBase();
        //database 오픈
        dataBaseAdapter.open();

        Cursor getCursorFromSQLite = dataBaseAdapter.selectPhoneBookData();

        while (getCursorFromSQLite.moveToNext()){
            //db 필드 담아와서 각 변수에 대입
            int _id = getCursorFromSQLite.getInt(0);
            String telName = getCursorFromSQLite.getString(1);
            String telNumber = getCursorFromSQLite.getString(2);
            String telAddress = getCursorFromSQLite.getString(3);
            String telFromDataHelper = getCursorFromSQLite.getString(4);

            phoneBookList.add(new phoneBookItemObject(_id,telName,telNumber,telAddress,telFromDataHelper));
        }
        dataBaseAdapter.close();
    }

    /**
     * xml로 파일로 부터 데이터 가져와서 phonebookList에 담기
     */
    public void getDataListFromXML(){
        ArrayList<phoneBookItemObject> xmlDataFromXML = xmlDataHelper.getXmlData();
        phoneBookList.addAll(xmlDataFromXML);
    }

    /**
     * Pref로 부터 데이터 가져와서 phoneBookList에 담기
     */
    private void getDataListFromPref(){
        ArrayList<phoneBookItemObject> prefDataFromPref = prefDataHelper.getJsonFileFromPref();
        phoneBookList.addAll(prefDataFromPref);
    }

    /**
     *
     * @return phoneBookList의 개수를 구한다
     */
    @Override
    public int getCount() {
        return phoneBookList.size();
    }

    /**
     *
     * @param position
     * @return phoneBookList의 순서를 읽어들여서 해당 아이템 하나를 가져온다
     */
    @Override
    public Object getItem(int position) {
        return phoneBookList.get(position);
    }

    /**
     *
     * @param position
     * @return 해당 아이템의 순서를 구한다
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return 하나의 getView를 반환한다
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_data,null);

            viewHolder.tvName = (TextView)v.findViewById(R.id.tvName);
            viewHolder.tvAddr = (TextView)v.findViewById(R.id.tvAddr);
            viewHolder.tvTel = (TextView)v.findViewById(R.id.tvTel);
            viewHolder.tvStore = (TextView)v.findViewById(R.id.tvStore) ;

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)v.getTag();
        }
        phoneBookItemObject phoneItem = phoneBookList.get(position);
        viewHolder.tvName.setText(phoneItem.getTelName());
        viewHolder.tvAddr.setText(phoneItem.getTelAddress());
        viewHolder.tvTel.setText(phoneItem.getTelNumber());
        viewHolder.tvStore.setText(phoneItem.getTelFromDataHelper());

        return v;
    }

    /**
     * 뷰 홀더
     */
    class ViewHolder{
        public TextView tvName = null;
        public TextView tvAddr = null;
        public TextView tvTel = null;
        public TextView tvStore = null;

    }

    /**
     * phoneBookList에 직접적으로 add하는 메서드이다
     * @param name
     * @param addr
     * @param tel
     */
    public void add(int _id, String name, String addr, String tel, String dataStore){
        addObjectSecondList(_id, name, addr, tel, dataStore);
        phoneBookList.add(new phoneBookItemObject(_id,name,addr,tel,dataStore));
        notifyDataSetChanged();
    }

    public void remove(phoneBookItemObject item){
        removeObjectSecondList(item);
        phoneBookList.remove(item);
        notifyDataSetChanged();
    }


    /**
     * editText 필터링 하는 기능이다
     * 맨 처음 phoneBookList를 비우고 입력부분이 없으면 여벌의 arraylist를 집어 넣는다
     * 입력부분이 있으면 여벌의 arraylist에서 검색한 아이템이 존재하면 phoneBookList에 해당 아이템만 집어 넣는다
     * @param charText
     */
    public void filter(String charText) {
        phoneBookList.clear();
        if (charText.length() == 0) {
            phoneBookList.addAll(arraylist);
//            notifyDataSetChanged();
        } else {
            for (phoneBookItemObject item : arraylist) {

                if (item.getTelName().contains(charText)) {
                    phoneBookList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
