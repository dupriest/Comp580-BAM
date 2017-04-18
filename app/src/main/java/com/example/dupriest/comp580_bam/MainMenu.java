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

public class MainMenu extends AppCompatActivity {

    int lives;
    int time;
    String control;
    Context context;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        lives = sharedPref.getInt("lives", -1);
        // default = infinity
        time = sharedPref.getInt("time", -1);
        // default = no time limit
        control = sharedPref.getString("control", "buttons");
        // default = use buttons to control game
        //setDifficultyText();
    }

    /*
    void setDifficultyText()
    {
        Button difficulty = (Button)findViewById(R.id.difficulty);
        String text = "difficulty\n";
        if(lives == -1)
        {
            text = text + "lives = unlimited";
        }
        else
        {
            text = text + "lives = " + String.valueOf(lives);
        }
        text = text + "\n";
        if(time == -1)
        {
            text = text + "time = unlimited";
        }
        else
        {
            text = text + "time = " + String.valueOf(time / 1000) +  " seconds";
        }
        difficulty.setText(text);
    }
    */
    void play(View view)
    {
        Button button = (Button)view;
        String text = (String)button.getText();

        if(text.equals("test controls"))
        {
            text = "test";
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type", text);
        editor.commit();
        Intent X = new Intent(this, game.class);
        startActivity(X);
    }

    void difficulty(View view)
    {
        Intent X = new Intent(this, difficulty.class);
        startActivity(X);
    }

    void create(View view)
    {
        Intent X = new Intent(this, roomRecord.class);
        //Intent X = new Intent(this, create.class);
        startActivity(X);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        lives = sharedPref.getInt("lives", -1);
        // default = infinity
        time = sharedPref.getInt("time", -1);
        // default = no time limit
        control = sharedPref.getString("control", "buttons");
        //setDifficultyText();
    }
}
