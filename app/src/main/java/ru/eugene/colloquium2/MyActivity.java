package ru.eugene.colloquium2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Item>> {
    private final static List<String> empty = new ArrayList<String>();
    private List<Item> candidates = null;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<Integer> votes = new ArrayList<Integer>();
    private List<Integer> id = new ArrayList<Integer>();
    private ListView listView;
    private int posOfSelectedEl;
    private Context context;
    private boolean DEBUG = true;
    private String TAG = "MyActivity";
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> emptyArrayAdapter;

    DbHelper db;
    SQLiteDatabase database;
    DataSourceItem sourceItem;

    private static final int CONTEXT_MENU_ADD = 1;
    private static final int CONTEXT_MENU_EDIT = 2;
    private static final int CONTEXT_MENU_DELETE = 3;
    private static final int CONTEXT_MENU_VOTE = 4;
    private static final int CONTEXT_MENU_RESET = 5;
    private static final int CONTEXT_MENU_RESULT = 6;
    private static final int CHANGE_ITEM_ADD = 100;
    private static final int CHANGE_ITEM_EDIT = 200;
    private static final int CHANGE_ITEM_VOTE = 300;
    private static final int LOADER_ID = 1;
    private boolean isVoted = false;
    private ContextMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empty.add("Add new candidate");
        emptyArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, empty); listView = getListView(); context = this;

        db = new DbHelper(this);
        database = db.getWritableDatabase();
        sourceItem = new DataSourceItem(database);
        candidates = sourceItem.read();
        fromCandidatesToLists();

        Log.i("LOG", "in on Create : " + isVoted);
        if (isEmpty()) {
            setListAdapter(emptyArrayAdapter);
        } else {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            setListAdapter(arrayAdapter);
        }

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                registerForContextMenu(listView);
                posOfSelectedEl = position;
                openContextMenu(listView);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (candidates.isEmpty()) {
                    addNewCandidate();
                    arrayAdapter = null;
                } else {
                }
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void fromCandidatesToLists() {
        names.clear();
        votes.clear();
        id.clear();
        for (Item it : candidates) {
            if (it.getId() < 0) {
                isVoted = true;
                break;
            }
            names.add(it.getName());
            votes.add(it.getVote());
            id.add(it.getId());
        }
        if (isVoted)
            candidates.remove(candidates.size() - 1);
    }

    private boolean isEmpty() {
        return candidates == null || candidates.size() == 0;
    }

    void setListAdapter() {
        if (candidates.isEmpty())
            arrayAdapter = emptyArrayAdapter;
        else if (arrayAdapter == null)
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);

        listView.setAdapter(arrayAdapter);
    }

    private void addNewCandidate() {
        Intent changeItem = new Intent(context, ChangeItem.class);
        changeItem.putExtra("target", "add");
        startActivityForResult(changeItem, CHANGE_ITEM_ADD);
    }

    private void setMenu(ContextMenu menu) {
        menu.clear();
        menu.setHeaderTitle("What do you want to do?");
        if (!isVoted) {
            menu.add(0, CONTEXT_MENU_ADD, 0, "add new candidate");
            if (!isEmpty()) {
                menu.add(0, CONTEXT_MENU_VOTE, 0, "start vote");
                menu.add(0, CONTEXT_MENU_EDIT, 0, "edit");
                menu.add(0, CONTEXT_MENU_DELETE, 0, "delete");
            }
        }
        if (!isEmpty()) {
            menu.add(0, CONTEXT_MENU_RESULT, 0, "get results");
            menu.add(0, CONTEXT_MENU_RESET, 0, "reset");
        }
        this.menu = menu;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        setMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_ADD:
                addNewCandidate();
                break;
            case CONTEXT_MENU_DELETE:
                new AlertDialog.Builder(this)
                        .setTitle("Delete candidate")
                        .setMessage("Are you sure you want to delete this candidate?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sourceItem.delete(new Item(id.get(posOfSelectedEl)));
                                candidates.remove(posOfSelectedEl);
                                names.remove(posOfSelectedEl);
                                votes.remove(posOfSelectedEl);
                                id.remove(posOfSelectedEl);
                                setListAdapter();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case CONTEXT_MENU_EDIT:
                Intent changeItem = new Intent(context, ChangeItem.class);
                changeItem.putExtra("target", "edit");
                changeItem.putExtra("candidate", names.get(posOfSelectedEl));
                startActivityForResult(changeItem, CHANGE_ITEM_EDIT);
                break;
            case CONTEXT_MENU_VOTE:
                Intent vote = new Intent(context, Vote.class);
                vote.putStringArrayListExtra("names", names);
                vote.putIntegerArrayListExtra("votes", votes);
                startActivityForResult(vote, CHANGE_ITEM_VOTE);
                boolean result = sourceItem.insert(new Item(-1));
                isVoted = true;
                setMenu(menu);
                break;
            case CONTEXT_MENU_RESET:
                db.destroy(database);
                candidates.clear();
                isVoted = false;
                fromCandidatesToLists();
                setListAdapter();
                break;
            case CONTEXT_MENU_RESULT:
                ArrayList<String> data = new ArrayList<String>();
                int sum = 0;
                for (int it : votes) {
                    sum += it;
                }
                sum = Math.max(sum, 1);

                for (Item it : candidates) {
                    data.add(it.getVote() + " votes (" + String.format("%.2f", (double) it.getVote() / sum) + "%) for " + it.getName());
                }

                Collections.sort(data);
                Collections.reverse(data);
                Intent resultOfVote = new Intent(context, ResultOfVote.class);
                resultOfVote.putStringArrayListExtra("data", data);
                startActivity(resultOfVote);
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case CHANGE_ITEM_ADD:
                    String resCandidate = data.getStringExtra("candidate");
                    Item item = new Item(resCandidate);
                    item.setId(id.isEmpty() ? 0 : id.get(id.size() - 1) + 1);
                    candidates.add(item);
                    names.add(item.getName());
                    votes.add(0);
                    id.add(item.getId());

                    sourceItem.insert(item);
                    break;

                case CHANGE_ITEM_EDIT:
                    String resCandidate2 = data.getStringExtra("candidate");
                    candidates.get(posOfSelectedEl).setName(resCandidate2);
                    names.set(posOfSelectedEl, resCandidate2);
                    sourceItem.update(candidates.get(posOfSelectedEl));
                    break;

                case CHANGE_ITEM_VOTE:
                    ArrayList<Integer> newVotes = data.getIntegerArrayListExtra("votes");
                    votes = newVotes;

                    for (int i = 0; i < votes.size(); i++) {
                        candidates.get(i).setVote(votes.get(i));
                        sourceItem.update(candidates.get(i));
                    }

                    break;
            }
        setListAdapter();
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        SQLiteDataLoader loader = new SQLiteDataLoader(context, sourceItem, null, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        if (data.size() > 0) {
            candidates.clear();
            for (Item item : data) {
                candidates.add(item);
            }
            fromCandidatesToLists();
            if (arrayAdapter == null || arrayAdapter.equals(emptyArrayAdapter)) {
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            }
        } else {
            arrayAdapter = emptyArrayAdapter;
        }

        setListAdapter();
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> listLoader) {
        if (arrayAdapter != null)
            arrayAdapter.clear();
    }

    @Override
    public void onDestroy() {
        Log.i("LOG", "in  destroy");
        super.onDestroy();
        db.close();
        database.close();
        db = null;
        database = null;
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);
    }
}

