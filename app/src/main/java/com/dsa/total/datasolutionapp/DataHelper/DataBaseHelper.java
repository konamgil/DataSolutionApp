package com.dsa.total.datasolutionapp.DataHelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by jisun on 2017-05-19.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    //디버그에 쓰임임
    private String TAG = getClass().getSimpleName(); //Logcat에 출력할 태그이름

    private final Context mContext;
    private static String DB_NAME ="phonebook"; // 데이터베이스 이름
    private static int version = 1;

    private SQLiteDatabase mDataBase;
    private static String DB_PATH = "";


    /**
     * DataBaseHelper 생성자
     * @param context
     */
    public DataBaseHelper(Context context){
        super(context, DB_NAME, null, version);// 1은 데이터베이스 버젼
        this.mContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            this.DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }

    /**
     * 데이터베이스 열기, 기존 데이터베이스 없으면 자동으로 onCreate 호출
     * @throws IOException
     */
    public void createDataBase() throws IOException
    {
        this.getReadableDatabase();
        this.close();
        Log.e(TAG, "createDatabase database created");
    }

    /**
     * 데이터베이스를 열어서 쿼리를 쓸수있게만든다.
     * @return
     * @throws SQLException
     */
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    /**
     * 데이터베이스 종료
     */
    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
        this.DB_NAME = name;
        this.version = version;


    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

        this.mContext = context;
        this.DB_NAME = name;
        this.version = version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 만들기
        db.execSQL("CREATE TABLE phonebook (_id INTEGER PRIMARY KEY NOT NULL," +
                    "telName TEXT,telAddress TEXT,telNumber TEXT,telFromDataHelper  TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //테이블 드랍 후 새로 onCreate 호출
        db.execSQL("DROP TABLE IF EXISTS phonebook");
        onCreate(db);
    }
}
