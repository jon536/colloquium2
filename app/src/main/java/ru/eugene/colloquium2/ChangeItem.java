package ru.eugene.colloquium2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.eugene.colloquium2.R;


public class ChangeItem extends Activity {
    Button save;
    EditText candidate;
    Context context;
    String target;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_item);
        save = (Button) findViewById(R.id.save);
        candidate = (EditText) findViewById(R.id.candidate);
        context = this;
        target = this.getIntent().getStringExtra("target");
        if (target.equals("edit")) {
            candidate.setText(this.getIntent().getStringExtra("candidate"));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                String candidateS = candidate.getText().toString().trim();
                if (!candidateS.isEmpty()) {
                    resultIntent.putExtra("candidate", candidateS);
                    setResult(Activity.RESULT_OK, resultIntent);
                    Log.i("LOG", "in changeItem");
                    finish();
                } else {
                    Toast.makeText(context, "Please type name of candidate!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
