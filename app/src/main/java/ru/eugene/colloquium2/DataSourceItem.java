package ru.eugene.colloquium2;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 11/10/14.
 */
public class DataSourceItem extends DataSource<Item> {
    public static final String TABLE_NAME = "rss_feeds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VOTE = "vote";

    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer, "
            + COLUMN_NAME + " text,"
            + COLUMN_VOTE + " integer);";

    public DataSourceItem(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public boolean insert(Item entity) {
        if (entity == null) {
            return false;
        }
        Log.i("LOG", "insert");
        long result = database.insert(TABLE_NAME, null,
                generateContentValuesFromObject(entity));
        Log.i("LOG", "insert");

        return result != -1;
    }

    @Override
    public boolean update(Item entity) {
        if (entity == null) {
            return false;
        }
        Log.i("LOG", "name : " + entity.getName());
        Log.i("LOG", "id : " + entity.getId());
        Log.i("LOG", "vote : " + entity.getVote());
        long result = database.update(TABLE_NAME, generateContentValuesFromObject(entity),
                COLUMN_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    public boolean delete(Item entity) {
        if (entity == null) {
            return false;
        }
        long result = database.delete(TABLE_NAME, COLUMN_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    public List read() {
        Log.i("LOG", "read");
        Cursor cursor = database.query(TABLE_NAME, getAllColumns(), null, null, null, null, null);
        List result = new ArrayList();
        Log.i("LOG", "read");
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    @Override
    public List read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = database.query(TABLE_NAME, getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
        List result = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return result;
    }

    public String[] getAllColumns() {
        return new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_VOTE};
    }

    private ContentValues generateContentValuesFromObject(Item entity) {
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entity.getId());
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_VOTE, entity.getVote());
        return values;
    }

    private Item generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Item result = new Item();
        result.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        result.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        result.setVote(cursor.getInt(cursor.getColumnIndex(COLUMN_VOTE)));
        return result;
    }

}
