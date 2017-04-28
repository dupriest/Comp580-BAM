package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class roomRecord3 extends AppCompatActivity {

    Context context;
    SharedPreferences sharedPref;
    String slot;
    int cycle;
    String selected;
    Button selectedButton;
    ArrayList<String> buttonChoices;
    int buttonChoicesPointer;
    String soundDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record3);
        buttonChoicesPointer = 0;
        buttonChoices = new ArrayList<>();
        buttonChoices.add("action");
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        cycle = sharedPref.getInt("cycle", 1);
        if(cycle==1)
        {
            buttonChoices.add("left");
            buttonChoices.add("right");
            buttonChoices.add("left or right");
        }
        else
        {
            soundDirection = sharedPref.getString("soundDirection", "action");
            buttonChoices.add("towards sound");
            if(soundDirection.equals("left") | soundDirection.equals("right") | soundDirection.equals("left or right"))
            {
                buttonChoices.add("away from sound");
            }
        }
        selected = "";
    }

    public void select(View view)
    {
        Button b = (Button)view;
        b.setBackgroundResource(R.color.black);

        if(((String)selectedButton.getText()).equals("left"))
        {
            selectedButton.setBackgroundResource(R.color.red);
        }
        else if(((String)selectedButton.getText()).equals("right"))
        {
            selectedButton.setBackgroundResource(R.color.green);
        }
        else if(((String)selectedButton.getText()).equals("action"))
        {
            selectedButton.setBackgroundResource(R.color.blue);
        }

        if(selected.equals((String)b.getText()))
        {
            selected = "";
        }
        else
        {
            selected = (String)b.getText();
        }
    }

    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }

    public void next(View view)
    {
        Intent X;
        if(cycle==1)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("cycle", 2);
            editor.commit();
            X = new Intent(this, roomRecord.class);
        }
        else
        {
            X = new Intent(this, roomRecord4.class);
        }

        startActivity(X);
    }

}
