package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class difficulty extends AppCompatActivity {

    String[] info;
    int lives;
    int time;
    String control;
    Button timeButton;
    Button livesButton;
    Button controlButton;
    HashMap<Integer, String> livesMap;
    HashMap<Integer, String> timeMap;
    HashMap<String, String> controlMap;
    Context context;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        livesMap = new HashMap<Integer, String>();
        livesMap.put(-1, "lives1");
        livesMap.put(5, "lives2");
        livesMap.put(1, "lives3");
        timeMap = new HashMap<Integer, String>();
        timeMap.put(-1, "times1");
        timeMap.put(10000, "times2");
        timeMap.put(2000, "times3");
        controlMap = new HashMap<String, String>();
        controlMap.put("buttons", "control1");
        controlMap.put("screentilt", "control2");

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        lives = sharedPref.getInt("lives", -1);
        // default = infinity
        time = sharedPref.getInt("time", -1);
        // default = no time limit
        control = sharedPref.getString("control", "buttons");
        // default = use buttons to control game

        timeButton = (Button)findViewById(getResources().getIdentifier(timeMap.get(time),"id", getPackageName()));
        livesButton = (Button)findViewById(getResources().getIdentifier(livesMap.get(lives),"id", getPackageName()));
        controlButton = (Button)findViewById(getResources().getIdentifier(controlMap.get(control),"id", getPackageName()));
        timeButton.setBackgroundResource(R.color.black);
        livesButton.setBackgroundResource(R.color.black);
        controlButton.setBackgroundResource(R.color.black);
    }

    void save(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("lives", lives);
        editor.putInt("time", time);
        editor.putString("control", control);
        editor.commit();
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }

    void change(View view)
    {
        Button b = (Button)view;
        String id = getResources().getResourceName(b.getId());
        int start = id.length()-6;
        int end = id.length()-1;
        if(id.substring(start,end).equals("lives"))
        {
            livesButton.setBackgroundResource(R.color.red);
            livesButton = b;
            livesButton.setBackgroundResource(R.color.black);

            if(id.substring(end,id.length()).equals("1"))
            {
                lives = -1;
            }

            if(id.substring(end,id.length()).equals("2"))
            {
                lives = 5;
            }

            if(id.substring(end,id.length()).equals("3"))
            {
                lives = 1;
            }
        }
        else if(id.substring(start, end).equals("times"))
        {
            timeButton.setBackgroundResource(R.color.blue);
            timeButton = b;
            timeButton.setBackgroundResource(R.color.black);

            if(id.substring(end,id.length()).equals("1"))
            {
                time = -1;
            }

            if(id.substring(end,id.length()).equals("2"))
            {
                time = 10000;
            }

            if(id.substring(end,id.length()).equals("3"))
            {
                time = 2000;
            }
        }
        else
        {
            controlButton.setBackgroundResource(R.color.green);
            controlButton = b;
            controlButton.setBackgroundResource(R.color.black);
            if(id.substring(end, id.length()).equals("1"))
            {
                control = "buttons";
            }
            else
            {
                control = "screentilt";
            }
        }
    }
}
