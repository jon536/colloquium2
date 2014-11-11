package ru.eugene.colloquium2;

import android.content.ContentValues;
import android.content.Loader;
import android.os.AsyncTask;

/**
 * Created by eugene on 11/11/14.
 */
public abstract class ContentChangingTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
    Loader<?> loader = null;
    ContentChangingTask(Loader<?> loader) {
        this.loader = loader;
    }

    @Override
    protected void onPostExecute(T3 param) {
        loader.onContentChanged();
    }
}
