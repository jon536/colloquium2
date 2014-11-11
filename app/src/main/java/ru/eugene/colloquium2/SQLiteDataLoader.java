package ru.eugene.colloquium2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.Log;

import java.util.List;

/**
 * Created by eugene on 11/11/14.
 */
public class SQLiteDataLoader extends AbstractDataLoader<List<Item>> {
    private DataSource<Item> mDataSource;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public SQLiteDataLoader(Context context, DataSource dataSource, String selection, String[] selectionArgs,
                                String groupBy, String having, String orderBy) {
        super(context);
        mDataSource = dataSource;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

    @Override
    protected List<Item> buildList() {
        List<Item> result = mDataSource.read(mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy);
        return result;
    }

    public void insert(Item entity) {
        new InsertTask(this).execute(entity);
    }

    public void update(Item entity) {
        new UpdateTask(this).execute(entity);
    }

    public void delete(Item entity) {
        new DeleteTask(this).execute(entity);
    }


    private class DeleteTask extends ContentChangingTask<Item, Void, Void> {
        DeleteTask(Loader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Item... params) {
            mDataSource.delete(params[0]);
            return null;
        }
    }

    private class UpdateTask extends ContentChangingTask<Item, Void, Void> {
        UpdateTask(Loader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Item... params) {
            mDataSource.update(params[0]);
            return null;
        }
    }

    private class InsertTask extends ContentChangingTask<Item, Void, Void> {
        InsertTask(Loader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Item... params) {
            mDataSource.insert(params[0]);
            return null;
        }
    }
}
