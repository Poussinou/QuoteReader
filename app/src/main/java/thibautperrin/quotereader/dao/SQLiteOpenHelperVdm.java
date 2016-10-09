package thibautperrin.quotereader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import thibautperrin.quotereader.StaticFields;


class SQLiteOpenHelperVdm extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "vdms";
    static final String COLUMN_VDM_INDEX = "ind";
    static final String COLUMN_VDM_NO = "number";
    static final String COLUMN_VDM_CONTENT = "content";
    static final String COLUMN_VDM_END_URL = "url";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_VDM_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_VDM_NO + " INTEGER UNIQUE, " +
            COLUMN_VDM_CONTENT + " TEXT, " +
            COLUMN_VDM_END_URL + " TEXT);";

    public SQLiteOpenHelperVdm(Context context) {
        super(context, StaticFields.VDMS_DATABASE_FILE, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do currently
    }
}
