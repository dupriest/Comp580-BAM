package com.example.dupriest.comp580_bam;

import android.content.Intent;
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
    Button timeButton;
    Button livesButton;
    HashMap<Integer, String> livesMap;
    HashMap<Integer, String> timeMap;


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

        info = getIntent().getData().toString().split(" ");
        lives = Integer.parseInt(info[0]);
        time = Integer.parseInt(info[1]);
        timeButton = (Button)findViewById(getResources().getIdentifier(timeMap.get(time),"id", getPackageName()));
        livesButton = (Button)findViewById(getResources().getIdentifier(livesMap.get(lives),"id", getPackageName()));
        timeButton.setBackgroundResource(R.color.black);
        livesButton.setBackgroundResource(R.color.black);
    }

    void save(View view)
    {
        Uri myUri = Uri.parse(String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, MainMenu.class);
        X.setData(myUri);
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
        else
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
    }
}
