package com.dsa.total.datasolutionapp.Ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dsa.total.datasolutionapp.DataAdapter.ListAdapter;
import com.dsa.total.datasolutionapp.DataHelper.JsonDataHelper;
import com.dsa.total.datasolutionapp.DataHelper.PrefDataHelper;
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
                        JsonDataHelper jsonDataHelper = new JsonDataHelper(context);

                        //입력받은 값으로 제이슨 오브젝트를 만든다
                        JSONObject jObject = jsonDataHelper.makeJsonObject(etNameInput.getText().toString(),etAddrInput.getText().toString(),etPhoneInput.getText().toString());

                        mListAdapter.addJsonObject(jObject);

                        //프리퍼런스에 새로운 제이슨 오브젝트를 넣는다
                        addJsonObect(jObject);

                        alertDialog.dismiss();
                        Toast.makeText(context,"추가되었습니다",Toast.LENGTH_SHORT).show();

                    }
                });
                alertDialog.show();
            }
        });
        //
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

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

