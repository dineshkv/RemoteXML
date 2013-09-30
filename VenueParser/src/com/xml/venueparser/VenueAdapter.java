package com.xml.venueparser;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VenueAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<Venue> venues;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public VenueAdapter(Context context, ArrayList<Venue> venues) {
        this.context = context;
        this.venues = venues;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(context.getApplicationContext());
    }

    public int getCount() {
        return venues.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView text=(TextView)vi.findViewById(R.id.textView);;
        ImageView image=(ImageView)vi.findViewById(R.id.imageView);
        Venue venue = venues.get(position);
        text.setText(venue.getVenueName());
        imageLoader.DisplayImage(venue.getImage(), image);
        return vi;
    }
}