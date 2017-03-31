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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);
        type = getIntent().getData().toString();
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
        Uri myUri = Uri.parse("play");
        Intent X = new Intent(this, MainMenu.class);
        X.setData(myUri);
        startActivity(X);
    }

    public void playagain(View view)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        Uri myUri = Uri.parse("play");
        Intent X = new Intent(this, game.class);
        X.setData(myUri);
        startActivity(X);
    }



}
