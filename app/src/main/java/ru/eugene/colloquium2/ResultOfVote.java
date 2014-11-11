package ru.eugene.colloquium2;

import android.app.ListActivity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.eugene.colloquium2.R;

public class ResultOfVote extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> data = this.getIntent().getStringArrayListExtra("data");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View result = super.getView(position, convertView, parent);
                if (position == 0)
                    ((TextView) result).setTextColor(Color.RED);
                return result;
            }
        };
        setListAdapter(arrayAdapter);
    }
}
