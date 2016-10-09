package thibautperrin.quotereader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import thibautperrin.quotereader.StaticFields;


class SQLiteOpenHelperDtc extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DTC_TABLE_NAME = "dtcs";
    static final String COLUMN_DTC_NO = "dtc_number";
    static final String SENTENCE_TABLE_NAME = "sentence";
    static final String COLUMN_SENTENCE_NO = "sentence_number";
    static final String COLUMN_AUTHOR = "author";
    static final String COLUMN_CONTENT = "content";

    private static final String DTC_TABLE_CREATE = "CREATE TABLE " + DTC_TABLE_NAME + " (" +
            COLUMN_DTC_NO + " INTEGER PRIMARY KEY);";

    private static final String SENTENCE_TABLE_CREATE = "CREATE TABLE " + SENTENCE_TABLE_NAME + " (" +
            COLUMN_DTC_NO + " INTEGER, " +
            COLUMN_SENTENCE_NO + " INTEGER, " +
            COLUMN_AUTHOR + " TEXT, " +
            COLUMN_CONTENT + " TEXT, " +
            "PRIMARY KEY (" + COLUMN_DTC_NO + ", " + COLUMN_SENTENCE_NO + "), " +
            "FOREIGN KEY (" + COLUMN_DTC_NO + ") REFERENCES " + DTC_TABLE_NAME + ");";

    public SQLiteOpenHelperDtc(Context context) {
        super(context, StaticFields.DTCS_DATABASE_FILE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DTC_TABLE_CREATE);
        db.execSQL(SENTENCE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do currently
    }
}