package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class endgame extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String type;
    String[] info;
    int lives;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);
        info = getIntent().getData().toString().split(" ");
        type = info[0];
        lives = Integer.parseInt(info[1]);
        time = Integer.parseInt(info[2]);

        if(type.equals("success"))
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
        Uri myUri = Uri.parse(String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, MainMenu.class);
        X.setData(myUri);
        startActivity(X);
    }

    public void playagain(View view)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        Uri myUri = Uri.parse("play" + " " + String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, game.class);
        X.setData(myUri);
        startActivity(X);
    }



}
