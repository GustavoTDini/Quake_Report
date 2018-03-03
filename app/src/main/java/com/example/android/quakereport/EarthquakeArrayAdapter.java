package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by SSJdini on 27/02/18.
 */

public class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeArrayAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super( context, 0, earthquakes );
    }

    private static String LOCATION_SEPARATOR = " of ";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        View earthquakeList = convertView;

        if (earthquakeList == null){
            earthquakeList = LayoutInflater.from(getContext()).inflate( R.layout.earthquake_list, parent, false);
        }

        Earthquake thisQuake = getItem( position );
        assert thisQuake != null;

        TextView magTextView = earthquakeList.findViewById(R.id.mag_text_view);

        DecimalFormat magFormat = new DecimalFormat("0.0");
        String magString = magFormat.format(thisQuake.getMag());

        // Configure a cor de fundo apropriada no círculo de magnitude.
        // Busque o fundo do TextView, que é um GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Obtenha a cor de fundo apropriada, baseada na magnitude do terremoto atual
        int magnitudeColor = getMagnitudeColor(thisQuake.getMag());

        // Configure a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor);

        magTextView.setText(magString);

        String placeTotalString = thisQuake.getPlace();

        TextView deslocationTextView = earthquakeList.findViewById( R.id.deslocation_text_view );
        TextView placeTextView = earthquakeList.findViewById( R.id.place_text_view );

        if (placeTotalString.contains( LOCATION_SEPARATOR )){
            String[] placeStringArray = placeTotalString.split(LOCATION_SEPARATOR);


            String deslocation = placeStringArray[0] + LOCATION_SEPARATOR;
            deslocationTextView.setText(deslocation);


            String place = placeStringArray[1];
            placeTextView.setText( place );

        } else{
            deslocationTextView.setText( R.string.near_the );

            placeTextView.setText( placeTotalString );
        }


        long unixTime = thisQuake.getDate();

        Date quakeDateAndTime = new Date(unixTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String quakeDateFormat = dateFormat.format( quakeDateAndTime );

        TextView dateTextView = earthquakeList.findViewById( R.id.date_text_view );
        dateTextView.setText( quakeDateFormat );

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String quakeTimeFormat = timeFormat.format(quakeDateAndTime );

        TextView timeTextView = earthquakeList.findViewById( R.id.time_text_view );
        timeTextView.setText(quakeTimeFormat );


        return earthquakeList;
    }

    private int getMagnitudeColor(Double magDouble){
        int magInt = magDouble.intValue();
        int colorResource;

        switch (magInt){
            case 10:
                colorResource = R.color.magnitude10plus;
                break;
            case 9:
                colorResource = R.color.magnitude9;
                break;
            case 8:
                colorResource = R.color.magnitude8;
                break;
            case 7:
                colorResource = R.color.magnitude7;
                break;
            case 6:
                colorResource = R.color.magnitude6;
                break;
            case 5:
                colorResource = R.color.magnitude5;
                break;
            case 4:
                colorResource = R.color.magnitude4;
                break;
            case 3:
                colorResource = R.color.magnitude3;
                break;
            case 2:
                colorResource = R.color.magnitude2;
                break;
            default:
                colorResource = R.color.magnitude1;
                break;
        }

        return ContextCompat.getColor(getContext(), colorResource );

    }

}
