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
import android.telephony.PhoneNumberFormattingTextWatcher;
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
//        dataList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        dataList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //EditText의 필터 부분에 와쳐를 연결한다
        etName.addTextChangedListener(tw);

        mArrayListForUpdateDelete = mListAdapter.getAllDataLiatR();
//        mArrayListForUpdateDelete = mListAdapter.getAllDataList();

        dataList.setOnItemClickListener(mOnItemClickListener);
        hideSoftKeyboard(etName);
    }

    /**
     * init widget
     */
    public void init() {
        etName = (EditText) findViewById(R.id.etName);
        dataList = (ListView) findViewById(R.id.listView);
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
                etPhoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
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
                    @Override
                    public void onClick(View v) {

                        String name = etNameInput.getText().toString();
                        String addr = etAddrInput.getText().toString();
                        String phone = etPhoneInput.getText().toString();

                        String selectdKindsOfDatastored = spinner.getSelectedItem().toString();
                        switch(selectdKindsOfDatastored){
                            case "SQLite":
                                DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                                int uid = makeAutoId();
                                mDataBaseAdapter.insertPhoneBookData(uid,name,addr,phone,"SQLite");
                                mListAdapter.add(uid,name,addr,phone,"SQLite");
                                Toast.makeText(context,"SQLite",Toast.LENGTH_SHORT).show();
                                break;
                            case "Json":
                                //jsonhelper 열기
                                JsonDataHelper jsonDataHelper = new JsonDataHelper(context);
                                int json_id = makeAutoId();
                                jsonDataHelper.insertJsonData(json_id,name,addr,phone,"Json");
                                mListAdapter.add(json_id,name,addr,phone,"Json");
                                Toast.makeText(context,"Json",Toast.LENGTH_SHORT).show();
                                break;
                            case "XML":
                                int xml_id = makeAutoId();
                                XmlDataHelper xmlDataHelper = new XmlDataHelper(context);
                                xmlDataHelper.insertXmlData(xml_id,name,addr,phone,"XML");
                                mListAdapter.add(xml_id,name,addr,phone,"XML");
                                Toast.makeText(context,"XML",Toast.LENGTH_SHORT).show();
                                break;
                            case "Preference":
                                int preference_id = makeAutoId();
                                PrefDataHelper prefDataHelper = new PrefDataHelper(context);
                                prefDataHelper.saveJsonFileInPref(preference_id,name,addr,phone,"Preference");
                                mListAdapter.add(preference_id,name,addr,phone,"Preference");
                                Toast.makeText(context,"Preference",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                        alertDialog.dismiss();
                        Toast.makeText(context,"추가되었습니다",Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard(etName);
                    }
                });
                alertDialog.show();
            }
        });
        //
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //수정 삭제 ui만들것임
    ListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            BottomSheet.Builder mBottomSheet = new BottomSheet.Builder(context, R.style.MyBottomSheetStyle);
                mBottomSheet
                    .setSheet(R.menu.bottom_sheet)
                    .setTitle("메뉴")
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NonNull BottomSheet bottomSheet) {}
                        @Override
                        public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                            //버튼 클릭에 따라 다름
                            switch (menuItem.getItemId()){
                                case R.id.editPhoneItem:

                                    int _id_edit = mArrayListForUpdateDelete.get(position).get_id();
                                    String name_edit = mArrayListForUpdateDelete.get(position).getTelName();
                                    String addr_edit = mArrayListForUpdateDelete.get(position).getTelAddress();
                                    String tel_edit = mArrayListForUpdateDelete.get(position).getTelNumber();
                                    String kindsOfDatastore_edit = mArrayListForUpdateDelete.get(position).getTelFromDataHelper();

                                    update(_id_edit, name_edit, addr_edit, tel_edit, kindsOfDatastore_edit, position);

                                    break;
                                case R.id.deletePhoneItem:
                                    int _id = mArrayListForUpdateDelete.get(position).get_id();
                                    String kindsOfDatastore = mArrayListForUpdateDelete.get(position).getTelFromDataHelper();

                                    Log.d(TAG,"_id : " + _id + ", " + "kindsOfDatastore : " + kindsOfDatastore);

                                    phoneBookItemObject thisItem = (phoneBookItemObject)mListAdapter.getItem(position);

                                    deleteData(_id,kindsOfDatastore);

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
        }
    };

    /**
     * 아이템 삭제 메소드
     * @param _id
     * @param kindsOfDatastore
     */
    public void deleteData(int _id, String kindsOfDatastore){
        switch (kindsOfDatastore){
            case "SQLite":
                DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                mDataBaseAdapter.deletePhoneBookData(_id);
//                mListAdapter.refreshData();
                break;
            case "Json":
                JsonDataHelper mJsonDataHelper = new JsonDataHelper(context);
                mJsonDataHelper.deleteJsonFile(_id);
//                mListAdapter.refreshData();
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
//                mListAdapter.refreshData();
                break;
            case "Preference":
                PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
                mPrefDataHelper.deleteItemFromInPref(_id);
                break;
            default:
                break;
        }
    }

    /**
     * 업데이트 메소드
     * @param _id_edit
     * @param name_edit
     * @param addr_edit
     * @param tel_edit
     * @param kindsOfDatastore_edit
     * @param position
     */
    public void update(final int _id_edit, final String name_edit, final String addr_edit, final String tel_edit, final String kindsOfDatastore_edit, final int position){

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_input, null);

        alert.setTitle("수정").setView(innerView);

        /**
         * alert에 들어가는 위젯들
         * 이름 전화번호 주소
         */
        final EditText etNameInput = (EditText)innerView.findViewById(R.id.etNameInput);
        final EditText etPhoneInput = (EditText)innerView.findViewById(R.id.etPhoneInput);
        etPhoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText etAddrInput = (EditText)innerView.findViewById(R.id.etAddrInput);

        //스피너 적용
        final Spinner spinner = (Spinner)innerView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.datakinds, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setEnabled(false);
        // 스피너 end

        //스퍼 설정부분
        int setDataNum = 0;
        switch (kindsOfDatastore_edit){
            case "SQLite":
                setDataNum = 0;
                break;
            case "Json":
                setDataNum = 1;
                break;
            case "XML":
                setDataNum = 2;
                break;
            case "Preference":
                setDataNum = 3;
                break;
            default:
                break;
        }
        /// 수정된 문자열이 들어갈 입력창의 위젯 부분
        etNameInput.setText(name_edit);
        etAddrInput.setText(addr_edit);
        etPhoneInput.setText(tel_edit);
        spinner.setSelection(setDataNum);

        //스피너 선택된 문자열 ex ) SQLite
        final String selectedDataStore = spinner.getSelectedItem().toString();


        final AlertDialog alertDialog = alert.create();

        //취소버튼
        btnCancel = (Button)innerView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //수정 확인버튼
        btnSuccess = (Button)innerView.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String getName = etNameInput.getText().toString();
                String getAddr = etAddrInput.getText().toString();
                String getTell = etPhoneInput.getText().toString();
                switch (selectedDataStore){
                    case "SQLite":
                        DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(context);
                        mDataBaseAdapter.updatePhoneBookData(_id_edit, getName, getAddr, getTell, selectedDataStore);
                        break;
                    case "Json":
                        JsonDataHelper mJsonDataHelper = new JsonDataHelper(context);
                        mJsonDataHelper.updateJsonFile(_id_edit, getName, getAddr, getTell, selectedDataStore);
                        break;
                    case "XML":
                        XmlDataHelper mXmlDataHelper = new XmlDataHelper(context);
                        try {
                            mXmlDataHelper.updateXmlData(_id_edit, getName, getAddr, getTell, selectedDataStore);
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Preference":
                        PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
                        mPrefDataHelper.updateItemFromInPref(_id_edit, getName, getAddr, getTell, selectedDataStore);
                        break;
                    default:
                        break;
                }
                phoneBookItemObject setItem = new phoneBookItemObject(_id_edit, getName, getAddr, getTell, selectedDataStore);
                mArrayListForUpdateDelete.set(position,setItem);
                mListAdapter.set(position, setItem);
                mListAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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

