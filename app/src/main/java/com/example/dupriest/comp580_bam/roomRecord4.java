package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class roomRecord4 extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    Context context;
    SharedPreferences sharedPref;
    String slot;
    MediaPlayer mPlayer;

    String introRoom;
    String room;
    String soundDirection;
    String button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record4);
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        introRoom = sharedPref.getString(slot + " intro", "empty");
        room = sharedPref.getString(slot, "empty");
        soundDirection = sharedPref.getString(slot + " soundDirection", "empty");
        button = sharedPref.getString(slot + " button", "empty");
    }

    public void playRoom(View view)
    {
        if(!introRoom.equals("empty") && !room.equals("empty") && !soundDirection.equals("empty") && !button.equals("empty"))
        {
            Log.v("MEGAN DUPRIEST", introRoom + ", " + room + ", " + soundDirection + ", " + button);
            startPlaying();

        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(introRoom);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mPlayer = new MediaPlayer();
                    if(soundDirection.equals("left"))
                    {
                        mPlayer.setVolume(1,0);
                    }
                    else if(soundDirection.equals("right"))
                    {
                        mPlayer.setVolume(0,1);
                    }
                    else
                    {
                        mPlayer.setVolume(1,1);
                    }
                    try {
                        mPlayer.setDataSource(room);
                        mPlayer.prepare();
                        mPlayer.start();
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {

                            @Override
                            public void onCompletion(MediaPlayer mp)
                            {
                                Log.v("MEGAN DUPRIEST", "DONE!");
                            }
                        });
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord3.class);
        startActivity(X);
    }

    public void finish(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(slot + " complete", true); // true or false
        editor.commit();
        Intent X = new Intent(this, create2.class);
        startActivity(X);
    }

}
