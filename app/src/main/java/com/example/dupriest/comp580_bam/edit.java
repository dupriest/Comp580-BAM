package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class edit extends AppCompatActivity {

    int roomNum;
    Context context;
    SharedPreferences sharedPref;
    ArrayList<String> introQueue;
    ArrayList<String> queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        roomNum = 0; // display roomNum + 1

        introQueue = new ArrayList<>();
        queue = new ArrayList<>();

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.slot1_file_key), Context.MODE_PRIVATE);

        String key;
        for(int i = 0; i < 20; i++)
        {
            key = String.valueOf(i);
            queue.add(sharedPref.getString(key, "null"));
            key = key + "i";
            introQueue.add(sharedPref.getString(key, "null"));
        }



    }

    public void prevRoom(View view)
    {

    }

    public void nextRoom(View view)
    {

    }
}
