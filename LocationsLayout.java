package com.example.distresstracker;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationsLayout extends LinearLayout {
    TextView location;
    TextView empty1;

    public LocationsLayout(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        location = new TextView(context);
        location.setTextColor(Color.parseColor("#FFFFFF"));
        empty1 = new TextView(context);
        empty1.setText(" ");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        location.setPadding(0, 0, 0, 0);
        addView(location, lp);
        addView(empty1, lp);
    }


    public void populate(JSONObject jo, String key) {
        try {
            location.setText(jo.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void locationIDtoName(){
        if (location.getText().toString().equals("BFV")){
            location.setText("Bedfordview");
        }
        else if (location.getText().toString().equals("BRM")){
            location.setText("Braamfontein");
        }
        else if (location.getText().toString().equals("BYS")){
            location.setText("Bryanston");
        }
        else if (location.getText().toString().equals("EDV")){
            location.setText("Edenvale");
        }
        else if (location.getText().toString().equals("FRW")){
            location.setText("Fourways");
        }
        else if (location.getText().toString().equals("MDR")){
            location.setText("Midrand");
        }
        else if (location.getText().toString().equals("PKH")){
            location.setText("Parkhurst");
        }
        else if (location.getText().toString().equals("RDB")){
            location.setText("Randburg");
        }
        else if (location.getText().toString().equals("STD")){
            location.setText("Sandton");
        }
    }
}
