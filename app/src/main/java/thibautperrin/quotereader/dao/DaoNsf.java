package thibautperrin.quotereader.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import thibautperrin.quotereader.bean.Nsf;


public class DaoNsf extends Dao {
    private final SQLiteOpenHelperNsf dbHelper;

    public DaoNsf(Context context) {
        dbHelper = new SQLiteOpenHelperNsf(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void addNsf(Nsf nsf) {
        ContentValues values = new ContentValues();
        values.put(SQLiteOpenHelperNsf.COLUMN_NSF_NO, nsf.getNumber());
        values.put(SQLiteOpenHelperNsf.COLUMN_DATE, nsf.getDate());
        values.put(SQLiteOpenHelperNsf.COLUMN_NSF_CONTENT, nsf.getContent());
        db.insert(SQLiteOpenHelperNsf.TABLE_NAME, null, values);
    }

    public List<Nsf> getNsf() {
        Cursor cursor = db.query(SQLiteOpenHelperNsf.TABLE_NAME,
                null, null, null, null, null,
                SQLiteOpenHelperNsf.COLUMN_NSF_NO + " DESC");
        ArrayList<Nsf> res = new ArrayList<>();
        while (cursor.moveToNext()) {
            String content = cursor.getString(2);
            int number = cursor.getInt(0);
            String date = cursor.getString(1);
            if (content == null) content = "";
            if (date == null) date = "";
            res.add(new Nsf(content, number, date));
        }
        cursor.close();
        return res;
    }

    public int getLastNsfNumber() {
        Cursor cursor = db.query(SQLiteOpenHelperNsf.TABLE_NAME,
                new String[]{"MAX(" + SQLiteOpenHelperNsf.COLUMN_NSF_NO + ")"},
                null, null, null, null,
                SQLiteOpenHelperNsf.COLUMN_NSF_NO + " DESC");
        if (cursor.moveToNext()) {
            int res = cursor.getInt(0);
            cursor.close();
            return res;
        } else {
            cursor.close();
            return -1;
        }
    }

    public int keepOnlyLastNsfs(int number) {
        Cursor cursor = db.query(SQLiteOpenHelperNsf.TABLE_NAME,
                new String[] {SQLiteOpenHelperNsf.COLUMN_NSF_NO},
                null, null, null, null, SQLiteOpenHelperNsf.COLUMN_NSF_NO + " DESC", Integer.toString(number));
        boolean bool = cursor.moveToPosition(number-1);
        if (bool) {
            int no = cursor.getInt(0);
            cursor.close();
            return db.delete(SQLiteOpenHelperNsf.TABLE_NAME, SQLiteOpenHelperNsf.COLUMN_NSF_NO + " < ?", new String[]{Integer.toString(no)});
        } else {
            cursor.close();
            return 0;
        }
    }
}
