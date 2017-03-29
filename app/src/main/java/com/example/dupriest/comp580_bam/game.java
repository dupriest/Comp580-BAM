package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;


public class game extends AppCompatActivity {

    String type;
    CountDownTimer timer;
    ArrayList<String> queue;
    String currentRoom;
    String currentKey;
    MediaPlayer mediaPlayer;
    boolean actNow;
    long timeLeft;
    boolean isPaused = false;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        type = getIntent().getData().toString();
        queue = new ArrayList<String>();
        mediaPlayer = new MediaPlayer();
        actNow = false;

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
        if(!queue.isEmpty())
        {
            actNow = false;
            String[] current = queue.remove(0).split(",");
            currentRoom = current[0];
            currentKey = current[1];
            int id = getResources().getIdentifier(currentRoom,"raw", getPackageName());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    isPlaying = false;
                    timer = new CountDownTimer(10000, 100) {

                        public void onTick(long millisUntilFinished) {
                            timeLeft = millisUntilFinished;
                        }

                        public void onFinish() {
                            actNow = false;
                            timeLeft = 0;
                            Log.v("MEGAN DUPRIEST", "TOO LATE!");

                        }
                    }.start();
                    actNow = true;
                }
            });

        }
        else
        {
            Log.v("MEGAN DUPRIEST", "YOU ARE A WINNER! CONGRATS!");
        }
    }

    public void pause(View view)
    {
        // stuff will happen to pause the game itself
        if(isPlaying)
        {
            mediaPlayer.pause();
            isPaused = true;
        }
        if(timeLeft > 0)
        {
            timer.cancel();
        }
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
        if(isPaused)
        {
            mediaPlayer.start();
            isPaused = false;
            isPlaying = true;
        }

        if(timeLeft > 0)
        {
            isPlaying = false;
            timer = new CountDownTimer(timeLeft, 100) {

                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    actNow = false;
                    timeLeft = 0;
                    Log.v("MEGAN DUPRIEST", "TOO LATE!");

                }
            }.start();
            actNow = true;
        }


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
        Button b = (Button)view;
        String text = (String)b.getText();
        if(actNow)
        {
            timer.cancel();
            Log.v("CURRENT_KEY", currentKey);
            Log.v("ButtonClick", text);
            if(currentKey.equals(text))
            {
                Log.v("MEGAN DUPRIEST", "YAY YOU MADE IT!");
                runRoom();
            }
            else
            {
                Log.v("MEGAN DUPRIEST", "OOPS WRONG BUTTON PRESS!");
            }

        }

    }
}
