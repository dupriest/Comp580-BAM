package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Field;
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
    String state = "game"; // can be game or menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // NOTE: Turn off TalkBack Settings: "Speak Usage Hints"
        // NOTE: Change TalkBack volume to lower (50% or 25%)

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        type = getIntent().getData().toString();
        queue = new ArrayList<String>();
        mediaPlayer = new MediaPlayer();
        actNow = false;

        if(type.equals("play"))
        {
            Field[] fields = R.raw.class.getFields();

            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.substring(0,4).equals("room"))
                {
                    queue.add(name);
                }

            }
            //queue.add("room_dogbark_left,left");
            //queue.add("room_lotsofbats_right,right");
            //queue.add("room_magicbrew_action,action");
            runRoom();
        }

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
            currentRoom = queue.remove(0);
            currentKey = currentRoom.split("_")[2];
            int id = getResources().getIdentifier(currentRoom,"raw", getPackageName());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            if(currentKey.equals("left"))
            {
                mediaPlayer.setVolume(1,0);
            }
            else if(currentKey.equals("right"))
            {
                mediaPlayer.setVolume(0,1);
            }
            else
            {
                mediaPlayer.setVolume(1,1);
            }
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                    mediaPlayer.setVolume(1,1);
                    mediaPlayer.start();
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
                                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.failure);
                                    mediaPlayer.setVolume(1,1);
                                    mediaPlayer.start();

                                }
                            }.start();
                            actNow = true;
                        }
                    });
                }
            });

        }
        else
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fanfare);
            mediaPlayer.setVolume(1,1);
            mediaPlayer.start();
        }
    }

    public void pause(View view)
    {
        // stuff will happen to pause the game itself
        state = "menu";
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
        state = "game";
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
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.failure);
                    mediaPlayer.setVolume(1,1);
                    mediaPlayer.start();

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
            timeLeft = 0;
            actNow = false;

            if(currentKey.equals(text))
            {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.shortfanfare);
                mediaPlayer.setVolume(1,1);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        runRoom();
                    }
                });

            }
            else
            {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.failure);
                mediaPlayer.setVolume(1,1);
                mediaPlayer.start();
            }

        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(isPlaying && !isPaused)
        {
            mediaPlayer.pause();
            isPaused = true;
        }
        if(timeLeft > 0)
        {
            timer.cancel();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(isPaused && state.equals("game"))
        {
            mediaPlayer.start();
            isPaused = false;
            isPlaying = true;
        }

        if(timeLeft > 0 && state.equals("game"))
        {
            isPlaying = false;
            timer = new CountDownTimer(timeLeft, 100) {

                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    actNow = false;
                    timeLeft = 0;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.failure);
                    mediaPlayer.setVolume(1,1);
                    mediaPlayer.start();

                }
            }.start();
            actNow = true;
        }
    }
}
