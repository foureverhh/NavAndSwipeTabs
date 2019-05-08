package com.nackademin.foureverhh.navandswipetabs;

import android.provider.BaseColumns;

public class HistoryContract {

    private HistoryContract() {
    }

    public static final class HistoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_KEYWORD = "keyword";
        public static final String COLUMN_NAME_RESULT = "result";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                    HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HistoryEntry.COLUMN_NAME_KEYWORD + " TEXT NOT NULL, " +
                    HistoryEntry.COLUMN_NAME_RESULT + " TEXT NOT NULL, " +
                    HistoryEntry.COLUMN_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            " );";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;

}
