package com.example.homework1.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.homework1.R;

import java.util.ArrayList;

public class RecordListAdapter extends ArrayAdapter<Record> {
    private final Context mContext;
    private final int mResource;
    private int lastPosition = -1;

    static class ViewHolder {
        TextView distance;
        TextView date;
        TextView score;
        TextView rank;
    }

    public RecordListAdapter(Context context, int resource, ArrayList<Record> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int distance = getItem(position).getDistance();
        long date = getItem(position).getDate();
        int score = getItem(position).getScore();
        final View result; // create view result for showing the animation
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.distance = convertView.findViewById(R.id.textView3);
            holder.date = convertView.findViewById(R.id.textView2);
            holder.score = convertView.findViewById(R.id.textView1);
            holder.rank = convertView.findViewById(R.id.textView0);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;
        String dateFormat = DateFormat.format("dd.MM.yy\nHH:mm:ss", date).toString();
        holder.distance.setText("Distance: " + distance + " m");
        holder.date.setText("Date: " + dateFormat);
        holder.score.setText("Score: " + score);
        holder.rank.setText((position + 1) + ".");
        return convertView;
    }
}
