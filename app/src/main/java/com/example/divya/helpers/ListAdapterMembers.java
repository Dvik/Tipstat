package com.example.divya.helpers;

/**
 * Created by Divya on 10/24/2015.
 */
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.divya.model.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.divya.model.Member;
import com.example.divya.tipstat.HomeFragment;
import com.example.divya.tipstat.R;
import com.squareup.picasso.Picasso;


public class ListAdapterMembers extends ArrayAdapter<Member> {
    private final Context context;
    private final List<Member> items;


    public ListAdapterMembers(Context context, int resource, List<Member> items) {
        super(context, resource, items);
        this.context=context;
        this.items=items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_bg, parent, false);

        TextView tt1 = (TextView) rowView.findViewById(R.id.txt_row);
        ImageView image = (ImageView) rowView.findViewById(R.id.img);

        tt1.setText(items.get(position).status);
/*
        SquaredImageView view = (SquaredImageView) img;
        if (view == null) {
            view = new SquaredImageView(context);
        }*/
        String url = items.get(position).imgurl;

        Picasso.with(context).load(url).resize(30, 30)
                .centerCrop().into(image);


        return rowView;
    }
    public Member getItem(int position){

        return items.get(position);
    }

}