package com.example.divya.tipstat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.divya.helpers.SQLiteHelper;
import com.example.divya.model.EthicMatch;
import com.example.divya.model.Member;
import com.squareup.picasso.Picasso;

/**
 * Created by Divya on 10/24/2015.
 */
public class ShowProfile extends ActionBarActivity {

    private Toolbar mToolbar;
    private SQLiteHelper db;
    private TextView prof_tv1,prof_date,prof_status,prof_weight,prof_height,prof_drink,prof_veg;
    private ImageView prof_veg_img,prof_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Add notification");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHelper(getApplicationContext());

        Member memb = getIntent().getParcelableExtra("Member");


        prof_tv1 = (TextView) findViewById(R.id.prof_tv1);
        prof_date = (TextView) findViewById(R.id.prof_date);
        prof_status = (TextView) findViewById(R.id.prof_status);
        prof_weight = (TextView) findViewById(R.id.prof_weight);
        prof_height = (TextView) findViewById(R.id.prof_height);
        prof_drink = (TextView) findViewById(R.id.prof_drink);
        prof_veg = (TextView) findViewById(R.id.prof_veg);
        prof_veg_img = (ImageView) findViewById(R.id.prof_veg_img);
        prof_img = (ImageView) findViewById(R.id.prof_img);


        String url = memb.imgurl;

        Picasso.with(getApplicationContext()).load(url).resize(80, 80)
                .centerCrop().into(prof_img);


        prof_tv1.setText(EthicMatch.group[Integer.parseInt(memb.ethnicity)-1]);
        prof_date.setText(memb.dob);
        prof_status.setText(memb.status);
        prof_weight.setText(memb.weight+" kg");
        prof_height.setText(memb.height+" cm");

        if(memb.drink.equals("0"))
            prof_drink.setText("Doesn't Drink");
        else
            prof_drink.setText("Drinks");

        if(memb.is_veg.equals("0"))
        {
            prof_veg.setText("Vegetarian");
            prof_veg_img.setImageResource(R.drawable.ic_action1);
        }
        else
        {
            prof_veg.setText(" Non - Vegetarian");
            prof_veg_img.setImageResource(R.drawable.ic_action2);

        }


    }


}
