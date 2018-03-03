package com.example.android.quakereport;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SSJdini on 27/02/18.
 */

public class Earthquake {

    Double mMag;

    String mPlace;

    long mDate;

    String mURL;

    public Earthquake(Double mMag, String mPlace, long mDate, String mURL) {
        this.mMag = mMag;
        this.mPlace = mPlace;
        this.mDate = mDate;
        this.mURL = mURL;
    }

    public Double getMag() {
        return mMag;
    }

    public String getPlace() {
        return mPlace;
    }

    public long getDate() {
        return mDate;
    }

    public String getURL(){
        return mURL;
    }
}
