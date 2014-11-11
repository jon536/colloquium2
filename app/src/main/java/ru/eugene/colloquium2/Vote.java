package ru.eugene.colloquium2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.eugene.colloquium2.R;

public class Vote extends Activity {
    ArrayList<Integer> votes;
    ArrayList<String> names;
//    VoteAdapter voteAdapter;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        listView = (ListView) findViewById(R.id.listViewNames);
        votes = this.getIntent().getIntegerArrayListExtra("votes");
        names = this.getIntent().getStringArrayListExtra("names");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);
//        voteAdapter = new VoteAdapter(this, R.layout.vote, names, votes);
//        listView.setAdapter(voteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                votes.set(position, votes.get(position) + 1);
//                listView.setAdapter(voteAdapter);
            }
        });

        finish = (Button) findViewById(R.id.finishVote);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putIntegerArrayListExtra("votes", votes);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    void finishVote() {
        finish();
    }

}
