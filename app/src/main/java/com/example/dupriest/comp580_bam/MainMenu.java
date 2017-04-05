package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    String[] info;
    int lives;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        lives = -1; // infinity
        time = -1; // no time limit
        Uri uri = getIntent().getData();
        if(!(uri==null))
        {
            info = uri.toString().split(" ");

            lives = Integer.parseInt(info[0]);
            time = Integer.parseInt(info[1]);
        }
        setDifficultyText();
    }

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

    void play(View view)
    {
        Button button = (Button)view;
        String text = (String)button.getText();
        if(text.equals("test controls"))
        {
            text = "test";
        }
        Uri myUri = Uri.parse(text + " " + String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, game.class);
        X.setData(myUri);
        startActivity(X);
    }

    void difficulty(View view)
    {
        Uri myUri = Uri.parse(String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, difficulty.class);
        X.setData(myUri);
        startActivity(X);
    }
}
