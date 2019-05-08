package com.nackademin.foureverhh.navandswipetabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {
    private Context mContext;
    private HistoryDBHelper mHistoryDBHelper;
    private SQLiteDatabase mDataBase;

    public DataBaseAdapter(Context mContext, HistoryDBHelper mHistoryDBHelper) {
        this.mContext = mContext;
        this.mHistoryDBHelper = new HistoryDBHelper(mContext);
    }
}
