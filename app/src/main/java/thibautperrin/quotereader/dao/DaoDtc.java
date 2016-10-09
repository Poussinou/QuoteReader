package thibautperrin.quotereader.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import thibautperrin.quotereader.bean.Dtc;
import thibautperrin.quotereader.bean.Sentence;

public class DaoDtc extends Dao {
    private final SQLiteOpenHelperDtc dbHelper;

    public DaoDtc(Context context) {
        dbHelper = new SQLiteOpenHelperDtc(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void addDtc(Dtc dtc) {
        ContentValues values = new ContentValues();
        values.put(SQLiteOpenHelperDtc.COLUMN_DTC_NO, dtc.getNumber());
        db.insert(SQLiteOpenHelperDtc.DTC_TABLE_NAME, null, values);
        List<Sentence> sentences = dtc.getContent();
        int i = 0;
        for (Sentence sentence : sentences) {
            values = new ContentValues();
            values.put(SQLiteOpenHelperDtc.COLUMN_DTC_NO, dtc.getNumber());
            values.put(SQLiteOpenHelperDtc.COLUMN_SENTENCE_NO, i++);
            values.put(SQLiteOpenHelperDtc.COLUMN_AUTHOR, sentence.getAuthor());
            values.put(SQLiteOpenHelperDtc.COLUMN_CONTENT, sentence.getContent());
            db.insert(SQLiteOpenHelperDtc.SENTENCE_TABLE_NAME, null, values);
        }
    }

    public List<Dtc> getDtc() {
        Cursor cursor = db.query(SQLiteOpenHelperDtc.DTC_TABLE_NAME,
                null, null, null, null, null,
                SQLiteOpenHelperDtc.COLUMN_DTC_NO + " DESC");
        ArrayList<Dtc> dtcs = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            int dtcNo = cursor.getInt(0);
            Cursor cursorSentence = db.query(SQLiteOpenHelperDtc.SENTENCE_TABLE_NAME, null, SQLiteOpenHelperDtc.COLUMN_DTC_NO + " = ?", new String[]{Integer.toString(dtcNo)}, null, null, SQLiteOpenHelperDtc.COLUMN_SENTENCE_NO + " ASC");
            ArrayList<Sentence> sentences = new ArrayList<>(cursorSentence.getCount());
            while (cursorSentence.moveToNext()) {
                String author = cursorSentence.getString(2);
                String content = cursorSentence.getString(3);
                if (author == null) author = "";
                if (content == null) content = "";
                sentences.add(new Sentence(author, content));
            }
            dtcs.add(new Dtc(sentences, dtcNo));
            cursorSentence.close();
        }
        cursor.close();
        return dtcs;
    }

    public int getLastDtcNumber() {
        Cursor cursor = db.query(SQLiteOpenHelperDtc.DTC_TABLE_NAME,
                new String[]{"MAX(" + SQLiteOpenHelperDtc.COLUMN_DTC_NO + ")"},
                null, null, null, null,
                SQLiteOpenHelperDtc.COLUMN_DTC_NO + " DESC");
        if (cursor.moveToNext()) {
            int res = cursor.getInt(0);
            cursor.close();
            return res;
        } else {
            cursor.close();
            return -1;
        }
    }

    public int keepOnlyLastDtcs(int amountKept) {
        Cursor cursor = db.query(SQLiteOpenHelperDtc.DTC_TABLE_NAME,
                new String[] { SQLiteOpenHelperDtc.COLUMN_DTC_NO},
                null,null,null,null, SQLiteOpenHelperDtc.COLUMN_DTC_NO + " DESC", Integer.toString(amountKept));
        int amountDeleted = 0;
        if (cursor.moveToPosition(amountKept -1)) {
            int no = cursor.getInt(0);
            cursor.close();
            String whereClause = SQLiteOpenHelperDtc.COLUMN_DTC_NO + " < ?";
            String[] whereArgs = new String[]{Integer.toString(no)};
            amountDeleted = db.delete(SQLiteOpenHelperDtc.DTC_TABLE_NAME, whereClause, whereArgs);
            db.delete(SQLiteOpenHelperDtc.SENTENCE_TABLE_NAME, whereClause, whereArgs);
        }
        cursor.close();
        return amountDeleted;
    }
}
