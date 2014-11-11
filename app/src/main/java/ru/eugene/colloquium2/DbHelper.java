package ru.eugene.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eugene on 11/11/14.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "candidats.db";
    private static int DATABASE_VERSION = 18;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataSourceItem.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataSourceItem.TABLE_NAME);
        onCreate(db);
    }

    public void destroy(SQLiteDatabase db) {
        onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION + 1);
    }


}
