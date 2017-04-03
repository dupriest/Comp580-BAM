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
