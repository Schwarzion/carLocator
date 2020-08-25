package com.schwarzion.carLocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *
 */
public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] titles;
    String[] times;
    String[] longs;
    String[] lats;

    public ItemAdapter(Context c, String[] title, String[] time, String[] lo, String[] la) {
        titles = title;
        lats = la;
        longs = lo;
        times = time;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.my_location_list, null);
        TextView locTextView = (TextView) v.findViewById(R.id.locText);
        TextView timeTextView = (TextView) v.findViewById(R.id.timeText);
        TextView titleTextView = (TextView) v.findViewById(R.id.titleText);

        String titleDisplay = titles[position];
        String timeDisplay = times[position];
        String coords = "Lat : "+lats[position]+" Long : "+longs[position];

        titleTextView.setText(titleDisplay);
        timeTextView.setText(timeDisplay);
        locTextView.setText(coords);
        return v;
    }
}

