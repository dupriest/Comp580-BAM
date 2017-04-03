package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class game extends AppCompatActivity {

    String[] info;
    String type;
    CountDownTimer timer;
    ArrayList<String> introQueue;
    ArrayList<String> queue;
    String currentIntro;
    String currentRoom;
    String currentKey;
    String currentSoundDirection;
    MediaPlayer mediaPlayer;
    MediaPlayer menuSound;
    boolean actNow;
    long timeLeft;
    boolean isPaused = false;
    boolean isPlaying = false;
    String state = "game"; // can be game or menu
    int time;
    int lives;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // NOTE: Turn off TalkBack Settings: "Speak Usage Hints"
        // NOTE: Change TalkBack volume to lower (50% or 25%)

        //THOUGHTS: Maybe game beeps for the number of lives that you have
        // Maybe instead of pressing buttons you can tilt the controller??
        // That'd remove the element of button pressing. You could be really fast response.
        // Maybe you'd be able to turn it off and on
        // Scorekeeping
        // Challenge a friend to beat your game / beat your high score?
        // Then that version of the game would also list the user and high score of that user

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        info = getIntent().getData().toString().split(" ");
        type = info[0];
        lives = Integer.parseInt(info[1]);
        time = Integer.parseInt(info[2]);
        queue = new ArrayList<String>();
        introQueue = new ArrayList<String>();
        mediaPlayer = new MediaPlayer();
        menuSound = new MediaPlayer();
        actNow = false;

        if(type.equals("play"))
        {
            Field[] fields = R.raw.class.getFields();

            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room"))
                {
                    queue.add(name);
                }
                else if(name.length() >= 5 && name.substring(0,5).equals("intro"))
                {
                    introQueue.add(name);
                }

            }
            long seed = System.nanoTime();
            Collections.shuffle(queue, new Random(seed));
            Collections.shuffle(introQueue, new Random(seed));
            runRoom();
        }
    }

    public void runRoom()
    {
        // Give a description of the room from NO PARTICULAR DIRECTION
        // Once the description ends, a short soundtrack from one ear or another will play that's sound effect
        // currentRoom = queue.remove(0);
        // currentKey = associated button, LEFT, RIGHT, ACTION
        // get the media, play it
        if(!queue.isEmpty())
        {
            actNow = false;
            currentIntro = introQueue.remove(0);
            currentRoom = queue.remove(0);
            currentKey = currentRoom.split("_")[2];
            currentSoundDirection = currentRoom.split("_")[3];
            if(currentKey.equals("leftright"))
            {
                int num = (int)Math.round(Math.random());
                if(num == 0)
                {
                    currentKey = "left";
                    if(currentSoundDirection.equals("leftright"))
                    {
                        currentSoundDirection = "left";
                    }
                    else if(currentSoundDirection.equals("rightleft"))
                    {
                        currentSoundDirection = "right";
                    }
                }
                else
                {
                    currentKey = "right";

                    if(currentSoundDirection.equals("leftright"))
                    {
                        currentSoundDirection = "right";
                    }
                    else if(currentSoundDirection.equals("rightleft"))
                    {
                        currentSoundDirection = "left";
                    }
                }
            }
            int id = getResources().getIdentifier(currentIntro,"raw", getPackageName());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    int id = getResources().getIdentifier(currentRoom,"raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                    if(currentSoundDirection.equals("left"))
                    {
                        mediaPlayer.setVolume(1,0);
                    }
                    else if(currentSoundDirection.equals("right"))
                    {
                        mediaPlayer.setVolume(0,1);
                    }
                    else
                    {
                        mediaPlayer.setVolume(1,1);
                    }
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    if(time > -1)
                    {
                        timer = new CountDownTimer(time, 100) {

                            public void onTick(long millisUntilFinished) {
                                timeLeft = millisUntilFinished;
                            }

                            public void onFinish() {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                isPlaying = false;
                                actNow = false;
                                timeLeft = 0;
                                Uri myUri = Uri.parse("failure" + " " + String.valueOf(lives) + " " + String.valueOf(time));
                                Intent X = new Intent(game.this, endgame.class);
                                X.setData(myUri);
                                startActivity(X);

                            }
                        }.start();
                    }
                    actNow = true;
                }
            });

        }
        else
        {
            Uri myUri = Uri.parse("success" + " " + String.valueOf(lives) + " " + String.valueOf(time));
            Intent X = new Intent(this, endgame.class);
            X.setData(myUri);
            startActivity(X);
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

        Button left = (Button)findViewById(R.id.left);
        Button right = (Button)findViewById(R.id.right);
        Button action = (Button)(Button)findViewById(R.id.action);

        left.setVisibility(left.INVISIBLE);
        right.setVisibility(right.INVISIBLE);
        action.setVisibility(action.INVISIBLE);
        pause.setVisibility(pause.INVISIBLE);
        menuSound = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        menuSound.start();
        menuSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {

            @Override
            public void onCompletion(MediaPlayer mp)
            {
                Button mainmenu = (Button)findViewById(R.id.mainmenu);
                Button resume = (Button)(Button)findViewById(R.id.resume);
                mainmenu.setVisibility(mainmenu.VISIBLE);
                resume.setVisibility(resume.VISIBLE);
            }
        });


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
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    isPlaying = false;
                    actNow = false;
                    timeLeft = 0;
                    // eventually play unique 'failure' sound for each scenario
                    // also only end game when lives == 0
                    Uri myUri = Uri.parse("failure" + " " + String.valueOf(lives) + " " + String.valueOf(time));
                    Intent X = new Intent(game.this, endgame.class);
                    X.setData(myUri);
                    startActivity(X);

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
        Uri myUri = Uri.parse(String.valueOf(lives) + " " + String.valueOf(time));
        Intent X = new Intent(this, MainMenu.class);
        X.setData(myUri);
        startActivity(X);
    }

    public void buttonClick(View view)
    {
        Button b = (Button)view;
        String text = (String)b.getText();
        if(actNow)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            isPlaying = false;
            if(time > -1)
            {
                timer.cancel();
            }
            timeLeft = 0;
            actNow = false;

            if(currentKey.equals(text))
            {
                // eventually play unique 'short fanfare' for each scenario
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
                // also only end game when lives == 0
                // eventually play unique 'failure' sound for each scenario
                Uri myUri = Uri.parse("failure" + " " + String.valueOf(lives) + " " + String.valueOf(time));
                Intent X = new Intent(this, endgame.class);
                X.setData(myUri);
                startActivity(X);
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
            timer = new CountDownTimer(timeLeft, 100) {

                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                }

                public void onFinish() {
                    mediaPlayer.stop();
                    mediaPlayer.release();
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
