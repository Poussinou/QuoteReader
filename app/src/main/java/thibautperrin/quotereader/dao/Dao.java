package thibautperrin.quotereader.dao;

import android.database.sqlite.SQLiteDatabase;

public abstract class Dao {
    SQLiteDatabase db;

    public void close() {
        db.close();
    }

    public void startTransaction() {
        db.beginTransaction();
    }

    public void commitTransaction() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void rollbackTransaction() {
        db.endTransaction();
    }

}
