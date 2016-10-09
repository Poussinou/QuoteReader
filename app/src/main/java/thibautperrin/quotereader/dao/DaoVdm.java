package thibautperrin.quotereader.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import thibautperrin.quotereader.bean.Vdm;


public class DaoVdm extends Dao {
    private final SQLiteOpenHelperVdm dbHelper;

    public DaoVdm(Context context) {
        dbHelper = new SQLiteOpenHelperVdm(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public boolean contains(long vdmNumber) {
        Cursor cursor = db.query(SQLiteOpenHelperVdm.TABLE_NAME, new String[0], SQLiteOpenHelperVdm.COLUMN_VDM_NO + "= ?", new String[]{Long.toString(vdmNumber)}, null, null, null);
        int size = cursor.getCount();
        cursor.close();
        if (size > 1) {
            throw new RuntimeException("There should be at most on VDM with number : " + vdmNumber);
        }
        return size == 1;
    }

    public void addVdm(Vdm vdm) {
        ContentValues values = new ContentValues();
        values.put(SQLiteOpenHelperVdm.COLUMN_VDM_NO, vdm.getNumber());
        values.put(SQLiteOpenHelperVdm.COLUMN_VDM_CONTENT, vdm.getContent());
        values.put(SQLiteOpenHelperVdm.COLUMN_VDM_END_URL, vdm.getEndUrl());
        db.insert(SQLiteOpenHelperVdm.TABLE_NAME, null, values);
    }

    public List<Vdm> getVdm() {
        Cursor cursor = db.query(SQLiteOpenHelperVdm.TABLE_NAME,
                null, null, null, null, null,
                SQLiteOpenHelperVdm.COLUMN_VDM_INDEX + " ASC");
        ArrayList<Vdm> res = new ArrayList<>();
        while (cursor.moveToNext()) {


            int index = cursor.getInt(0);
            String content = cursor.getString(2);
            long number = cursor.getLong(1);
            String endUrl = cursor.getString(3);
            if (content == null) content = "";
            if (endUrl == null) endUrl = "";
            Vdm vdm = new Vdm(index,
                    content,
                    number,
                    endUrl);
            res.add(vdm);
        }
        cursor.close();
        return res;
    }

    public int keepOnlyLastVdms(int number) {
        Cursor cursor = db.query(SQLiteOpenHelperVdm.TABLE_NAME,
                new String[]{SQLiteOpenHelperVdm.COLUMN_VDM_INDEX},
                null, null, null, null, SQLiteOpenHelperVdm.COLUMN_VDM_INDEX + " DESC", Integer.toString(number));

        boolean bool = cursor.moveToPosition(number-1);
        if (bool) {
            int index = cursor.getInt(0);
            cursor.close();
            return db.delete(SQLiteOpenHelperVdm.TABLE_NAME, SQLiteOpenHelperVdm.COLUMN_VDM_INDEX + " < ?", new String[]{Integer.toString(index)});
        } else {
            cursor.close();
            return 0;
        }
    }

    public void free() {
        db.delete(SQLiteOpenHelperVdm.TABLE_NAME, null, null);
    }
}
