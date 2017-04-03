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
        Log.v("MEGAN DUPRIEST", String.valueOf(uri==null));
        if(!(uri==null))
        {
            info = uri.toString().split(" ");
            Log.v("MEGAN DUPRIEST", info[0] + ", " + info[1]);
            lives = Integer.parseInt(info[0]);
            time = Integer.parseInt(info[1]);
        }
        Button difficulty = (Button)findViewById(R.id.difficulty);
        String text = "difficulty\n";
        if(lives == -1)
        {
            text = text + "lives = unlimited";
        }
        else if(lives == 5)
        {
            text = text + "lives = 5";
        }
        else if(lives == 1)
        {
            text = text + "lives = 1";
        }
        text = text + "\n";
        if(time == -1)
        {
            text = text + "time = unlimited";
        }
        if(time == 10000)
        {
            text = text + "time = 10 seconds";
        }
        if(time == 2000)
        {
            text = text + "time = 2 seconds";
        }
        difficulty.setText(text);
        Log.v("MEGAN DUPRIEST", "lives = " + String.valueOf(lives) + ", time = " + String.valueOf(time));
    }

    void play(View view)
    {
        Button button = (Button)view;
        String text = (String)button.getText();
        Uri myUri = Uri.parse(text + " " + String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, game.class);
        X.setData(myUri);
        startActivity(X);
    }

    void difficulty(View view)
    {
        Log.v("BOYFRIEND", "Hey baby, as you are working just know you are beautiful.  Love you :)");
        Uri myUri = Uri.parse(String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, difficulty.class);
        X.setData(myUri);
        startActivity(X);
    }
}
