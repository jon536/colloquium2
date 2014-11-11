package ru.eugene.colloquium2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 11/11/14.
 */
public class VoteAdapter extends ArrayAdapter<String> {
    List<String> names;
    List<Integer> votes;
    Context context;
    int resource;

    public VoteAdapter(Context context, int resource, List<String> names, List<Integer> votes) {
        super(context, resource, names);
        this.names = names;
        this.votes = votes;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder;
        if (row == null) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            row = layoutInflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.name = (TextView) row.findViewById(R.id.nameCandidate);
            holder.vote = (TextView) row.findViewById(R.id.voteCandidate);

            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        holder.name.setText(names.get(position));
        holder.vote.setText(votes.get(position) + "");
        return row;
    }

    private class Holder {
        TextView name;
        TextView vote;
    }
}
