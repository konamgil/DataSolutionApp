package com.dsa.total.datasolutionapp.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //디버깅에 쓰일 Tag name이다
    private final String TAG = getClass().getSimpleName();

    private ListView dataList;
    private EditText etName;

    private Context context = this;
    private ListAdapter mListAdapter;

    private Button btnSuccess;
    private Button btnCancel;

    //프리퍼런스로 보낼 jsonArray 이다
//    private JSONArray addedNewJsonDataArray = new JSONArray();
    private JSONObject addedNewJsonData = new JSONObject();



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

                //선택된 스피너



                // 스피너 end

                final AlertDialog alertDialog = alert.create();

                btnCancel = (Button)innerView.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

//                final String etname = etNameInput.getText().toString();
//                final String etPhone = etPhoneInput.getText().toString();
//                final String etAddr = etAddrInput.getText().toString();

                btnSuccess = (Button)innerView.findViewById(R.id.btnSuccess);

                btnSuccess.setOnClickListener(new View.OnClickListener() {
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
    //새로운 json 오브젝트를 어레이에 넣는다
    private void addJsonObect(JSONObject jsonObject){
//        addedNewJsonData.put(jsonObject);
        //임시
        PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
        mPrefDataHelper.saveJsonFileInPref(jsonObject);
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

    //일시중지 시점
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"--------------------------- onPause -----------------------");
//        PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
//        mPrefDataHelper.saveJsonFileInPref(addedNewJsonDataArray);
    }

    //중지상태에서 풀려난 상태
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"--------------------------- onResume -----------------------");
        PrefDataHelper mPrefDataHelper = new PrefDataHelper(context);
//        mListAdapter.addJsonObject(mPrefDataHelper.getJsonFileFromPref());
    }
}

