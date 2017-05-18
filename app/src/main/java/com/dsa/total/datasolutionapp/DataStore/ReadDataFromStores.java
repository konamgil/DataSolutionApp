package com.dsa.total.datasolutionapp.DataStore;

import android.content.Context;
import android.database.Cursor;

import com.dsa.total.datasolutionapp.DataAdapter.DataBaseAdapter;

/**
 * Created by jisun on 2017-05-19.
 */

public class ReadDataFromStores {
    private Context mContext;
    //sql
    //프리퍼런스
    //xml
    //json

    public ReadDataFromStores(Context context) {
        this.mContext = context;
    }

    public Cursor getSQLiteData(){
        DataBaseAdapter mDataBaseAdapter = new DataBaseAdapter(mContext);
        mDataBaseAdapter.createDateBase();
        mDataBaseAdapter.open();

        // getDataFromSQLiteDataBase : SQLITE에 모든 데이터가 담겨있다.
        Cursor getDataFromSQLiteDataBase = mDataBaseAdapter.getPhoneBookData();


        mDataBaseAdapter.close();

        return getDataFromSQLiteDataBase;
    }
}
