package thibautperrin.quotereader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import thibautperrin.quotereader.StaticFields;

class SQLiteOpenHelperNsf extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "nsfs";
    static final String COLUMN_NSF_NO = "number";
    static final String COLUMN_NSF_CONTENT = "content";
    static final String COLUMN_DATE = "date";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NSF_NO + " INTEGER PRIMARY KEY, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_NSF_CONTENT + " TEXT);";

    public SQLiteOpenHelperNsf(Context context) {
        super(context, StaticFields.NSFS_DATABASE_FILE, null, DATABASE_VERSION);
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
