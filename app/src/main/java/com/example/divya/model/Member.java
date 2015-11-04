package com.example.divya.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Divya on 10/24/2015.
 */
public class Member implements Parcelable{

    public String id;
    public String dob;
    public String status;
    public String ethnicity;
    public String weight;
    public String height;
    public String is_veg;
    public String drink;
    public String imgurl;

    public Member(String id, String dob,String status,String ethnicity, String weight,
                  String height,String is_veg,String drink,String imgurl)
    {

        this.id = id;
        this.dob = dob;
        this.status = status;
        this.ethnicity = ethnicity;
        this.weight = weight;
        this.height = height;
        this.is_veg = is_veg;
        this.drink = drink;
        this.imgurl = imgurl;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(dob);
        parcel.writeString(status);
        parcel.writeString(ethnicity);
        parcel.writeString(weight);
        parcel.writeString(height);
        parcel.writeString(is_veg);
        parcel.writeString(drink);
        parcel.writeString(imgurl);


    }
    private Member(Parcel in){
        this.id = in.readString();
        this.dob = in.readString();
        this.status = in.readString();
        this.ethnicity = in.readString();
        this.weight = in.readString();
        this.height = in.readString();
        this.is_veg = in.readString();
        this.drink = in.readString();
        this.imgurl = in.readString();
     }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {

        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
