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

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        cycle = sharedPref.getInt("cycle", 1);
        if(cycle==1)
        {

            buttonChoices.add("center");
            buttonChoices.add("left");
            buttonChoices.add("right");
            buttonChoices.add("left or right");
            String prev = sharedPref.getString(slot + " soundDirection", "empty");
            if(prev.equals("left"))
            {
                buttonChoicesPointer = 1;
            }
            else if(prev.equals("right"))
            {
                buttonChoicesPointer = 2;
            }
            else if(prev.equals("leftright"))
            {
                buttonChoicesPointer = 3;
            }
        }
        else
        {
            soundDirection = sharedPref.getString(slot + " soundDirection", "action");
            String prev = sharedPref.getString(slot + " button", "empty");
            if(prev.equals("away from sound"))
            {
                buttonChoicesPointer = 1;
            }
            buttonChoices.add("towards sound");
            if(soundDirection.equals("left") | soundDirection.equals("right") | soundDirection.equals("leftright"))
            {
                buttonChoices.add("away from sound");
            }
        }
        selected = "";
        Button b = (Button)findViewById(R.id.select);
        b.setText(buttonChoices.get(buttonChoicesPointer));
    }

    public void select(View view)
    {
        Button b = (Button)view;
        if(buttonChoicesPointer < buttonChoices.size()-1)
        {
            buttonChoicesPointer = buttonChoicesPointer + 1;
        }
        else
        {
            buttonChoicesPointer = 0;
        }
        b.setText(buttonChoices.get(buttonChoicesPointer));
        view.announceForAccessibility(buttonChoices.get(buttonChoicesPointer));
    }

    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }

    public void next(View view)
    {
        Intent X;
        SharedPreferences.Editor editor = sharedPref.edit();
        if(cycle==1)
        {
            String sD = buttonChoices.get(buttonChoicesPointer);
            if(sD.equals("left or right"))
            {
                sD = "leftright";
            }
            editor.putInt("cycle", 2);
            editor.putString(slot + " soundDirection", sD);
            X = new Intent(this, roomRecord.class);
        }
        else
        {
            String sD = sharedPref.getString(slot + " soundDirection", "empty");
            String button = buttonChoices.get(buttonChoicesPointer);

            if(sD.equals("left"))
            {
                if(button.equals("towards sound"))
                {
                    button = "left";
                }
                else
                {
                    button = "right";
                }
            }
            if(sD.equals("right"))
            {
                if(button.equals("towards sound"))
                {
                    button = "right";
                }
                else
                {
                    button = "left";
                }
            }
            if(sD.equals("leftright"))
            {
                if(button.equals("towards sound"))
                {
                    button = "leftright";
                }
                else
                {
                    button = "leftright";
                    sD = "rightleft";
                }
            }
            if(sD.equals("center"))
            {
                button = "action";
            }

            editor.putString(slot + " button", button);
            editor.putString(slot + " soundDirection", sD);
            X = new Intent(this, roomRecord4.class);
        }
        editor.commit();
        startActivity(X);
    }

}
