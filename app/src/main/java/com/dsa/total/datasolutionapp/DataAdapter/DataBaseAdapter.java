package com.dsa.total.datasolutionapp.DataAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dsa.total.datasolutionapp.DataHelper.DataBaseHelper;
import com.dsa.total.datasolutionapp.DataTransferObject.HelperType;

import java.io.IOException;

/**
 * Created by jisun on 2017-05-19.
 */

public class DataBaseAdapter {

    private final String TAG = getClass().getSimpleName();

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    private final String SQLITE = HelperType.getSQLITE();

    /**
     * 생성자
     * @param context
     */
    public DataBaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    /**
     * 데이터베이스 열기
     * @return
     * @throws SQLException
     */
    public DataBaseAdapter createDateBase() throws SQLException {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    /**
     * 데이터 베이스 오픈
     * @return DataBaseAdapter
     * @throws SQLException
     */
    public DataBaseAdapter open() throws SQLException {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    /**
     * 테이블의 모든 정보들을 담는다
     * @return Cursor
     */
    public Cursor getPhoneBookData() {
        try {

            String sql ="SELECT * FROM phonebook";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insetPhoneBookData(String telName, String telNumber, String telAddress, String telFromDataHelper ){
        if(telFromDataHelper == SQLITE ) {
            try {

                String sql = "INSERT INTO phonebook (telName, telNumber, telAddress,telFromDataHelper) VALUES ('고남길', '01072553466', '서울시 송파구 거여동', 'SQL' );";
//                String sql = "INSERT INTO phonebook (" + telName +"," + telNumber+"," + telAddress+"," + telFromDataHelper +") VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";


            } catch (SQLException mSQLException) {
                Log.e(TAG, "getTestData >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
    }
}
