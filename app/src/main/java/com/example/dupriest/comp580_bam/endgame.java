package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class endgame extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String endgame;
    int lives;
    int time;
    String control;
    Context context;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        lives = sharedPref.getInt("lives", -1);
        // default = infinity
        time = sharedPref.getInt("time", -1);
        // default = no time limit
        control = sharedPref.getString("control", "buttons");
        // default = use buttons to control game
        endgame = sharedPref.getString("endgame", "success");

        if(endgame.equals("success"))
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fanfare);
        }
        else
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.failure);
        }

        mediaPlayer.start();
    }

    public void mainmenu(View view)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }

    public void playagain(View view)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        Intent X = new Intent(this, game.class);
        startActivity(X);
    }



}
