package com.example.divya.tipstat;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.divya.helpers.JSONParser;

import com.example.divya.helpers.ListAdapterMembers;
import com.example.divya.helpers.SQLiteHelper;
import com.example.divya.model.EthicMatch;
import com.example.divya.model.Member;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Divya on 10/24/2015.
 */
public class HomeFragment extends Fragment{
    private ProgressDialog pDialog;

    SharedPreferences sharedpreferences;
    EditText inputSearch;
    JSONParser jParser = new JSONParser();
    ListAdapterMembers adapter;
    TextView mem_hits,api_hits;

    public static String number,hits;
    JSONArray members = null;
    private SQLiteHelper db=null;

    ListView listView1;

    public static List<Member> memberslist = new ArrayList<Member>();
    public static List<Member> memberslist1 = new ArrayList<Member>();


    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.home, container, false);
        listView1 = (ListView)rootView.findViewById(R.id.listView);

        api_hits = (TextView) rootView.findViewById(R.id.apihits);

        mem_hits = (TextView) rootView.findViewById(R.id.members);

        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);

        String s1 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString("API", "n");
        String s2 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString("Members", "n");


        db = new SQLiteHelper(getActivity().getApplicationContext());


        if(db.getRowCount()==0 || s1.equals("n") || s2.equals("n"))
        {
            new LoadAllMembers().execute();
        }
        else
        {
            api_hits.setText("API Hits: "+s1);
            mem_hits.setText("Members: "+s2);

            memberslist = db.getDetails();
            adapter = new ListAdapterMembers(getActivity(),
                    R.layout.row_bg, memberslist);
            listView1.setAdapter(adapter);
        }


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Member memb = memberslist.get(position);

                Intent intent = new Intent(getActivity().getBaseContext(), ShowProfile.class);

                intent.putExtra("Member",memb);

                startActivity(intent);

            }
        });

        Button img1 = (Button) rootView.findViewById(R.id.search_btn);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                memberslist = db.getDetails();
                memberslist1.clear();
                for(Member m: memberslist)
                {
                    if(m.status.toLowerCase(Locale.ENGLISH).contains(inputSearch.getText().toString().toLowerCase(Locale.ENGLISH)) ||
                            m.weight.toLowerCase(Locale.ENGLISH).contains(inputSearch.getText().toString().toLowerCase(Locale.ENGLISH)) ||
                            m.height.toLowerCase(Locale.ENGLISH).contains(inputSearch.getText().toString().toLowerCase(Locale.ENGLISH)))
                    {
                           memberslist1.add(m);
                    }
                }
                adapter = new ListAdapterMembers(getActivity(),
                        R.layout.row_bg, memberslist1);
                listView1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });


        Button b = (Button) rootView.findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(memberslist, new Comparator<Member>()
                {
                    @Override
                    public int compare(Member a, Member b)
                    {
                        Integer p = Integer.parseInt(a.weight);
                        Integer q = Integer.parseInt(b.weight);

                        return p>q?1:0;

                    }});
                adapter = new ListAdapterMembers(getActivity(),
                        R.layout.row_bg, memberslist);
                listView1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        Button b2 = (Button) rootView.findViewById(R.id.button2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(memberslist, new Comparator<Member>()
                {
                    @Override
                    public int compare(Member a, Member b)
                    {
                        Integer p = Integer.parseInt(a.height);
                        Integer q = Integer.parseInt(b.height);

                        return p>q?1:0;

                    }});
                adapter = new ListAdapterMembers(getActivity(),
                        R.layout.row_bg, memberslist);
                listView1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        Spinner spin = (Spinner) rootView.findViewById(R.id.ethnic);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position!=0)
                {
                    memberslist = db.getDetails();
                    memberslist1.clear();
                    for(Member m: memberslist)
                    {
                        if(Integer.parseInt(m.ethnicity) == position)
                            memberslist1.add(m);
                    }

                    adapter = new ListAdapterMembers(getActivity(),
                            R.layout.row_bg, memberslist1);
                    listView1.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return rootView;

    }


    class LoadAllMembers extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading members. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String[] doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

           /* HashMap hms=new HashMap<String, String>();
            hms = db.getUserDetails();*/



            String u[] = new String[2];
            u[0] = "http://tipstat.0x10.info/api/tipstat?type=json&query=list_status";
            u[1] = "http://tipstat.0x10.info/api/tipstat?type=json&query=api_hits";

            JSONObject json = jParser.makeHttpRequest(u[0], "GET");
            JSONObject apihits = jParser.makeHttpRequest(u[1], "GET");

            Log.d("All members: ", json.toString());

            try {


                    members = json.getJSONArray("members");

                    number = String.valueOf(members.length());


                    for (int i = 0; i < members.length(); i++) {

                        JSONObject c = members.getJSONObject(i);
                        String id,dob,status, ethnicity, weight,height, is_veg, drink, imgurl;
                        if(c.getString("id")!=null) {
                            id = c.getString("id");
                        }
                        else
                        id=" ";
                        if(c.getString("dob")!=null) {
                            dob = c.getString("dob");
                        }
                        else
                            dob=" ";
                        if(c.getString("status")!=null) {
                            status = c.getString("status");
                        }
                        else
                            status=" ";
                        if(c.getString("ethnicity")!=null) {
                            ethnicity = c.getString("ethnicity");
                        }
                        else
                            ethnicity=" ";
                        if(c.getString("weight")!=null) {
                            weight = c.getString("weight");
                        }
                        else
                            weight=" ";
                        if(c.getString("height")!=null) {
                            height = c.getString("height");
                        }
                        else
                            height=" ";
                        if(c.getString("is_veg")!=null) {
                            is_veg = c.getString("is_veg");
                        }
                        else
                            is_veg=" ";
                        if(c.getString("drink")!=null) {
                            drink = c.getString("drink");
                        }
                        else
                            drink=" ";

                        if(c.getString("image")!=null) {
                            imgurl = c.getString("image");
                        }
                        else
                            imgurl=" ";

                        //EthicMatch.group[Integer.parseInt(
                        Member mem = new Member(id, dob, status, ethnicity, String.valueOf(Integer.parseInt(weight)/1000),height, is_veg, drink, imgurl);
                        memberslist.add(mem);
                        db.addmember(mem);

                        if(apihits!=null)
                        {
                            hits = apihits.getString("api_hits");
                        }
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return u;
        }

        protected void onPostExecute(String[] file_url) {
            String s1 = file_url[0];
            String s2 = file_url[1];

            pDialog.dismiss();
            if(getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        api_hits.setText("API Hits: "+hits);
                        mem_hits.setText("Members: "+number);
                        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                                .edit().putString("API", hits).putString("Members",number).commit();


                        adapter = new ListAdapterMembers(getActivity(),
                                R.layout.row_bg, memberslist);
                        listView1.setAdapter(adapter);



                    }
                });
            }
        }

    }
}
