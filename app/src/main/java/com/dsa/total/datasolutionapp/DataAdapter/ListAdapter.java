package com.dsa.total.datasolutionapp.DataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsa.total.datasolutionapp.DataHelper.JsonDataHelper;
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
    private LayoutInflater inflater = null;
    private ArrayList<phoneBookItemObject> phoneBookList;
    private ViewHolder viewHolder = null;
    private ArrayList<phoneBookItemObject> arraylist;
    private JsonDataHelper jsonDataHelper;
    private JSONArray dataArray;

    /**
     * 생성자
     * @param context
     */
    public ListAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

        //json 데이터 처리 부분
        jsonDataHelper = new JsonDataHelper(mContext);
        dataArray = jsonDataHelper.getJsonData();
        //json 데이터 처리 부분 end

        //각 리스트들을 초기화한다
        this.phoneBookList = new ArrayList<phoneBookItemObject>();
        this.arraylist = new ArrayList<phoneBookItemObject>();

        //phoneBookList에 json 파일로부터 읽어들인 데이터를 가져온다
        getDataList();

        //기존의 phoneBookLiST를 여벌의 arraylist에 복사한다.
        this.arraylist.addAll(phoneBookList);
    }

    /**
     * 새로 추가되는 jsonobject를 여벌의 list에 담는다
     * @param jObject
     */
    public void addObjectSecondList(JSONObject jObject){
        try {
            String name = jObject.getString("name");
            String addr = jObject.getString("addr");
            String tel = jObject.getString("tel");
            arraylist.add(new phoneBookItemObject(name,addr,tel));
        } catch (JSONException j){
            j.printStackTrace();
        }
    }

    /**
     * dataArray(제이슨 어레이로부터 제이슨 오브젝트를 하나하나 리스트에 애드를 한다
     */
    public void addJsonObject(JSONObject jObject){

        //읽어들인 jsonaraay에 새로운 jsonobject를 담는다
        dataArray.put(jObject);

        //여벌의 arraylist에 담는다
        addObjectSecondList(jObject);

        //기존 phonebooklist를 클리어 하고
        phoneBookList.clear();

        //새롭게 phoneBookList를 구성한다
        getDataList();

        //데이터 구성의 변경이 있음을 아답터에게 알리고 리스트뷰를 갱신하도록 한다
        notifyDataSetChanged();
    }

    /**
     * 읽어들인 jsonArray로부터 개별적인 object를 꺼내어서 phonebooklist에 담는다
     */
    public void getDataList(){

        // 테스트 데이터
        // dataArray.put(jsonDataHelper.addJsonFile("고남길","부산","010"));

        for(int i =0; i<dataArray.length(); i++){
            try {
                JSONObject jObject = dataArray.getJSONObject(i);
                String name = jObject.getString("name");
                String addr = jObject.getString("addr");
                String tel = jObject.getString("tel");
                phoneBookList.add(new phoneBookItemObject(name,addr,tel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)v.getTag();
        }
        phoneBookItemObject phoneItem = phoneBookList.get(position);
        viewHolder.tvName.setText(phoneItem.getTelName());
        viewHolder.tvAddr.setText(phoneItem.getTelAddress());
        viewHolder.tvTel.setText(phoneItem.getTelNumber());

        return v;
    }

    /**
     * 뷰 홀더
     */
    class ViewHolder{
        public TextView tvName = null;
        public TextView tvAddr = null;
        public TextView tvTel = null;

    }

    /**
     * phoneBookList에 직접적으로 add하는 메서드이다
     * @param name
     * @param addr
     * @param tel
     */
    public void add(String name, String addr, String tel){
        phoneBookList.add(new phoneBookItemObject(name,addr,tel));
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
