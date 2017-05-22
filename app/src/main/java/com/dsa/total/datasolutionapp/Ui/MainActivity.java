package com.dsa.total.datasolutionapp.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dsa.total.datasolutionapp.DataAdapter.DataBaseAdapter;
import com.dsa.total.datasolutionapp.DataAdapter.ListAdapter;
import com.dsa.total.datasolutionapp.DataHelper.JsonDataHelper;
import com.dsa.total.datasolutionapp.DataHelper.PrefDataHelper;
import com.dsa.total.datasolutionapp.DataHelper.XmlDataHelper;
import com.dsa.total.datasolutionapp.DataTransferObject.phoneBookItemObject;
import com.dsa.total.datasolutionapp.R;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    //디버깅에 쓰일 Tag name이다
    private final String TAG = getClass().getSimpleName();

    private ListView dataList;
    private EditText etName;

    private Context context = this;
    private ListAdapter mListAdapter;

    private Button btnSuccess;
    private Button btnCancel;

    private ArrayList<phoneBookItemObject> mArrayListForUpdateDelete;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면의 위젯들을 초기화한다
        init();
        // 리스트어댑터를 초기화한다
        mListAdapter = new ListAdapter(context);
        //리스에 어댑터를 연결한다
        dataList.setAdapter(mListAdapter);
        //데이터 변경이 있으면 리스트뷰의 스크롤을 최하단으로 내린다
        dataList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //EditText의 필터 부분에 와쳐를 연결한다
        etName.addTextChangedListener(tw);

        mArrayListForUpdateDelete = mListAdapter.getAllDataList();

        dataList.setOnItemLongClickListener(mOnItemLongClickListener);
        hideSoftKeyboard(etName);
    }

    /**
     * init widget
     */
    public void init() {
        etName = (EditText) findViewById(R.id.etName);
        dataList = (ListView) findViewById(R.id.listView);
//        Button selectBtn = (Button) findViewById(R.id.selectBtn);
        Button addBtn = (Button)findViewById(R.id.addBtn);

        /**
         * 추가 버튼 리스너
         */
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final View innerView = getLayoutInflater().inflate(R.layout.dialog_input, null);

                alert.setTitle("입력").setView(innerView);

                /**
                 * alert에 들어가는 위젯들
                 * 이름 전화번호 주소
                 */
                final EditText etNameInput = (EditText)innerView.findViewById(R.id.etNameInput);
                final EditText etPhoneInput = (EditText)innerView.findViewById(R.id.etPhoneInput);
                final EditText etAddrInput = (EditText)innerView.findViewById(R.id.etAddrInput);

                //스피너 적용
                 final Spinner spinner = (Spinner)innerView.findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.datakinds, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                // 스피너 end

                final AlertDialog alertDialog = alert.create();

                //취소버튼
                btnCancel = (Button)innerView.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                //확인버튼
                btnSuccess = (Button)innerView.findViewById(R.id.btnSuccess);
                btnSuccess.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        //프리퍼런스에 새로운 제이슨 오브젝트를 넣는다
//                        addJsonObect(jObject);
                        String selectdKindsOfDatastored = spinner.getSelectedItem().toString();
                        switch(selectdKindsOfDatastored){
                            case "SQLite":
                                DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                                int uid = makeAutoId();
//                                phoneBookItemObject item = new phoneBookItemObject(uid,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"SQLite");
                                mDataBaseAdapter.insertPhoneBookData(uid,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"SQLite");
                                mListAdapter.add(uid,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"SQLite");
                                Toast.makeText(context,"SQLite",Toast.LENGTH_SHORT).show();
                                break;
                            case "Json":
                                //jsonhelper 열기
                                JsonDataHelper jsonDataHelper = new JsonDataHelper(context);
                                //입력받은 값으로 제이슨 오브젝트를 만든다
                                JSONObject jObject = jsonDataHelper.makeJsonObject(makeAutoId(),etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"Json");
                                mListAdapter.addJsonObject(jObject);
                                Toast.makeText(context,"Json",Toast.LENGTH_SHORT).show();
                                break;
                            case "XML":
                                int xml_id = makeAutoId();
                                XmlDataHelper xmlDataHelper = new XmlDataHelper(context);
                                xmlDataHelper.insertXmlData(xml_id,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString());
                                mListAdapter.add(xml_id,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"XML");
                                Toast.makeText(context,"XML",Toast.LENGTH_SHORT).show();
                                break;
                            case "Preference":
                                int preference_id = makeAutoId();
                                PrefDataHelper prefDataHelper = new PrefDataHelper(context);
                                prefDataHelper.saveJsonFileInPref(preference_id,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString());
                                mListAdapter.add(preference_id,etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString(),"Preference");
                                Toast.makeText(context,"Preference",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                        alertDialog.dismiss();
                        Toast.makeText(context,"추가되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();
            }
        });
        //
    }
    //수정 삭제 ui만들것임
    ListView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            new BottomSheet.Builder(context, R.style.MyBottomSheetStyle)
                    .setSheet(R.menu.bottom_sheet)
                    .setTitle("메뉴")
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NonNull BottomSheet bottomSheet) {}
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                            //버튼 클릭에 따라 다름
                            switch (menuItem.getItemId()){
                                case R.id.editPhoneItem:
                                    break;
                                case R.id.deletePhoneItem:
                                    int _id = mArrayListForUpdateDelete.get(position).get_id();
                                    String kindsOfDatastore = mArrayListForUpdateDelete.get(position).getTelFromDataHelper();
                                    Log.d(TAG,"_id : " + _id + ", " + "kindsOfDatastore : " + kindsOfDatastore);

                                    phoneBookItemObject thisItem = (phoneBookItemObject)mListAdapter.getItem(position);
                                    deleteData(_id,kindsOfDatastore,position);
                                    mArrayListForUpdateDelete.remove(thisItem);
                                    mListAdapter.remove(thisItem);
                                    mListAdapter.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                        }
                        @Override
                        public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {}
                    })
                    .show();
            return true;
        }
    };

    /**
     * 아이템 삭제 메소드
     * @param _id
     * @param kindsOfDatastore
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteData(int _id, String kindsOfDatastore, int position){
        switch (kindsOfDatastore){
            case "SQLite":
                DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                mDataBaseAdapter.deletePhoneBookData(_id);
                break;
            case "Json":
                JsonDataHelper mJsonDataHelper = new JsonDataHelper(context);
                mJsonDataHelper.deleteJsonFile(_id);
                mListAdapter.refreshData();
                break;
            case "XML":
                XmlDataHelper mXmlDataHelper = new XmlDataHelper(context);
                try {
                    mXmlDataHelper.removeName(_id);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                mListAdapter.refreshData();
                break;
            case "Preference":
                PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
                mPrefDataHelper.deleteJsonFileInPref(position);
                break;
            default:
                break;
        }
    }

    /**
     * 아이템 업데이트 메소드
     * @param _id
     * @param name
     * @param addr
     * @param tel
     * @param kindsOfDatastore
     */
    public void updateData(int _id, String name, String addr, String tel,String kindsOfDatastore ){
        switch (kindsOfDatastore){
            case "SQLite":
                DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                mDataBaseAdapter.updatePhoneBookData(_id,name,tel,addr,kindsOfDatastore);
                break;
            case "Json":
                break;
            case "XML":
                break;
            case "Preference":
                break;
            default:
                break;
        }
    }


    public int makeAutoId(){
        //자체 프라이머리 id 만들기
        SharedPreferences mPrefs = context.getSharedPreferences("_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();

        int current_id = mPrefs.getInt("_id",300);

        current_id = current_id + 1;
        editor.putInt("_id",current_id);
        editor.commit();

        int next_id = mPrefs.getInt("_id",300);


        return next_id;
    }


    /**
     * EDIT TEXT 에 대한 와쳐
     */
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            String filterText = etName.getText().toString();
            mListAdapter.filter(filterText);
        }
    };

    /**
     * 키보드 내리기
     * @param view
     */
    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //pause
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"--------------------------- onPause -----------------------");
    }

    //resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"--------------------------- onResume -----------------------");
    }
}

