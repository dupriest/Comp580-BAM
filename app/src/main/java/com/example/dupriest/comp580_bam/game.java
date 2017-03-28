package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class game extends AppCompatActivity {

    String type;
    Timer timer;
    ArrayList<String> queue;
    String currentRoom;
    String currentKey;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        type = getIntent().getData().toString();
        queue = new ArrayList<String>();
        mediaPlayer = new MediaPlayer();

        queue.add("dogbark,left");
        queue.add("lotsofbats,right");
        queue.add("magicbrew,action");
        runRoom();
        // Randomly add all rooms to the queue
        // Then should add the "final" room

    }

    public void runRoom()
    {
        // currentRoom = queue.remove(0);
        // currentKey = associated button, LEFT, RIGHT, ACTION
        // get the media, play it
        String[] current = queue.remove(0).split(",");
        currentRoom = current[0];
        currentKey = current[1];
        int id = getResources().getIdentifier(currentRoom,"raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp)
            {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // wait five seconds
                        Log.v("MEGAN DUPRIEST", "TIME IS UP!");

                    }
                }, 5000);
            }
        });
    }

    public void pause(View view)
    {
        // stuff will happen to pause the game itself
        Button pause = (Button)view;
        Button mainmenu = (Button)findViewById(R.id.mainmenu);
        Button resume = (Button)(Button)findViewById(R.id.resume);

        mainmenu.setVisibility(mainmenu.VISIBLE);
        resume.setVisibility(resume.VISIBLE);

        Button left = (Button)findViewById(R.id.left);
        Button right = (Button)findViewById(R.id.right);
        Button action = (Button)(Button)findViewById(R.id.action);

        left.setVisibility(left.INVISIBLE);
        right.setVisibility(right.INVISIBLE);
        action.setVisibility(action.INVISIBLE);
        pause.setVisibility(pause.INVISIBLE);




    }

    public void resume(View view)
    {
        Button resume = (Button)view;
        Button mainmenu = (Button)findViewById(R.id.mainmenu);

        mainmenu.setVisibility(mainmenu.INVISIBLE);
        resume.setVisibility(resume.INVISIBLE);

        Button left = (Button)findViewById(R.id.left);
        Button right = (Button)findViewById(R.id.right);
        Button action = (Button)(Button)findViewById(R.id.action);
        Button pause = (Button)(Button)findViewById(R.id.pause);
        left.setVisibility(left.VISIBLE);
        right.setVisibility(right.VISIBLE);
        action.setVisibility(action.VISIBLE);
        pause.setVisibility(pause.VISIBLE);
    }

    public void mainmenu(View view)
    {
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }

    public void buttonClick(View view)
    {
        timer.cancel();
        Log.v("MEGAN DUPRIEST", "YAY YOU MADE IT!");
    }
}
