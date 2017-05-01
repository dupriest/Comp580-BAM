package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class game extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = "AudioRecordTest";
    String type;
    CountDownTimer timer;
    ArrayList<String> introQueue;
    ArrayList<String> queue;
    ArrayList<Boolean> isUserMadeQueue;
    boolean isUserMade;
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
    String control;
    private SensorManager sm;
    private Sensor s;
    private List<Sensor> l;
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;


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
        setTitle("TEST CONTROLS. 5 ITEMS ON SCREEN.");

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        type = sharedPref.getString("type", "play");
        lives = sharedPref.getInt("lives", -1);
        // default = infinity
        time = sharedPref.getInt("time", -1);
        // default = no time limit
        control = sharedPref.getString("control", "buttons");
        // default = use buttons to control game

        if(lives >= 0)
        {
            TextView t = (TextView)findViewById(R.id.lives);
            t.setText("LIVES = " + String.valueOf(lives));
        }

        queue = new ArrayList<String>();
        introQueue = new ArrayList<String>();
        isUserMadeQueue = new ArrayList<Boolean>();

        mediaPlayer = new MediaPlayer();
        menuSound = new MediaPlayer();
        actNow = false;

        if(control.equals("screentilt"))
        {
            Button left = (Button)findViewById(R.id.left);
            Button right = (Button)findViewById(R.id.right);
            Button action = (Button)(Button)findViewById(R.id.action);

            left.setVisibility(left.INVISIBLE);
            right.setVisibility(right.INVISIBLE);
            action.setVisibility(action.INVISIBLE);
        }

        if(type.equals("play"))
        {
            setTitle("PLAY");
            if(control.equals("screentilt"))
            {
                sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                l = sm.getSensorList(Sensor.TYPE_ALL);
                s = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                sm.registerListener(this, s, 1000000);
            }

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

        if(type.equals("slot"))
        {
            // TODO: ADD THIS IN
            String slot = sharedPref.getString("slot", "slot 1").toUpperCase();
            setTitle("PLAY " + slot);
            context = getApplicationContext();
            if(slot.equals("SLOT 1"))
            {
                sharedPref2 = context.getSharedPreferences(getString(R.string.slot1_file_key), Context.MODE_PRIVATE);
            }
            else if(slot.equals("SLOT 2"))
            {
                sharedPref2 = context.getSharedPreferences(getString(R.string.slot2_file_key), Context.MODE_PRIVATE);
            }
            else
            {
                sharedPref2 = context.getSharedPreferences(getString(R.string.slot3_file_key), Context.MODE_PRIVATE);
            }

            String key;
            String value;
            for(int i = 0; i < 20; i++)
            {
                key = String.valueOf(i);
                value = sharedPref2.getString(key, "empty");
                if(!value.equals("empty"))
                {
                    queue.add(value);
                    key = key + "i";
                    introQueue.add(sharedPref2.getString(key, "empty"));
                    key = key + "u";
                    isUserMadeQueue.add(sharedPref2.getBoolean(key, false));
                }
            }
            runRoom();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null)
        {
            Log.v("MEGAN DUPRIEST", "SENSOR NOT AVAILABLE!!");
        }
        else
        {
            int tolerance = 4;
            View view = null;
            if(event.values[0] < (tolerance*-1))
            {
                Log.v("MEGAN DUPRIEST", "LEFT!");
                view = findViewById(R.id.left);

            }
            else if(event.values[0] > tolerance)
            {
                Log.v("MEGAN DUPRIEST", "RIGHT!");
                view = findViewById(R.id.right);

            }
            else if(event.values[1] < (tolerance*-1))
            {
                Log.v("MEGAN DUPRIEST", "ACTION!");
                view = findViewById(R.id.action);
            }
            if(view != null)
            {
                buttonClick(view);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // do nothing
    }

    public void runRoom()
    {
        // Give a description of the room from NO PARTICULAR DIRECTION
        // Once the description ends, a short soundtrack from one ear or another will play that's sound effect
        // currentRoom = queue.remove(0);
        // currentKey = associated button, LEFT, RIGHT, ACTION
        // get the media, play it
        if(!(queue.isEmpty()))
        {
            actNow = false;
            currentIntro = introQueue.remove(0);
            currentRoom = queue.remove(0);
            if(type.equals("slot"))
            {
                isUserMade = isUserMadeQueue.remove(0);
            }
            else
            {
                isUserMade = false;
            }

            if(isUserMade)
            {
                Log.v("MEGAN DUPRIEST", "currentRoom = " + currentRoom);
                currentKey = sharedPref.getString(currentRoom + " button", "empty");
                currentSoundDirection = sharedPref.getString(currentRoom +  " soundDirection", "empty");
                currentIntro = sharedPref.getString(currentRoom +  " intro", "empty");
                currentRoom = sharedPref.getString(currentRoom, "empty");
            }
            else
            {
                currentKey = currentRoom.split("_")[2];
                currentSoundDirection = currentRoom.split("_")[3];
            }

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
            Log.v("MEGAN DUPRIEST", currentIntro);
            if(currentIntro.substring(currentIntro.length()-4,currentIntro.length()-3).equals("."))
            {
                mediaPlayer = new MediaPlayer();
                try {
                    Log.v("MEGAN DUPRIEST", currentIntro);
                    mediaPlayer.setDataSource(currentIntro);
                    mediaPlayer.prepare();
                }
                catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
            else
            {
                int id = getResources().getIdentifier(currentIntro,"raw", getPackageName());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            }
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    if(currentRoom.substring(currentRoom.length()-4,currentRoom.length()-3).equals("."))
                    {
                        mediaPlayer = new MediaPlayer();
                        try {
                            Log.v("MEGAN DUPRIEST", currentRoom);
                            mediaPlayer.setDataSource(currentRoom);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e) {
                            Log.e(LOG_TAG, "prepare() failed");
                        }
                    }
                    else
                    {
                        int id = getResources().getIdentifier(currentRoom,"raw", getPackageName());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                    }
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
                        timer = new CountDownTimer(time, 100)
                        {

                            public void onTick(long millisUntilFinished)
                            {
                                timeLeft = millisUntilFinished;
                            }

                            public void onFinish()
                            {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                isPlaying = false;
                                actNow = false;
                                timeLeft = 0;
                                if(lives > 0)
                                {
                                    lives = lives - 1;
                                }

                                if(lives >= 0)
                                {
                                    TextView t = (TextView)findViewById(R.id.lives);
                                    t.setText("LIVES = " + String.valueOf(lives));
                                    int id = getResources().getIdentifier("shortfailure","raw", getPackageName());
                                    mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                                    mediaPlayer.start();
                                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                                    {

                                        @Override
                                        public void onCompletion(MediaPlayer mp)
                                        {
                                            View view = findViewById(R.id.activity_main_menu);
                                            view.announceForAccessibility("LIVES = " + String.valueOf(lives));
                                            introQueue.add(0, currentIntro);
                                            queue.add(0, currentRoom);
                                            if (type.equals("slot"))
                                            {
                                                isUserMadeQueue.add(0, isUserMade);
                                            }
                                            runRoom();
                                        }
                                    });
                                }
                                if(lives == 0)
                                {
                                    // endgame failure
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("endgame", "failure");
                                    editor.commit();
                                    Intent X = new Intent(game.this, endgame.class);
                                    startActivity(X);
                                }
                            }
                        }.start();
                    }
                    actNow = true;
                }
            });

        }
        else
        {
            // endgame success
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("endgame", "success");
            editor.commit();
            Intent X = new Intent(this, endgame.class);
            startActivity(X);
        }
    }

    public void pause(View view)
    {
        // stuff will happen to pause the game itself
        state = "menu";
        setTitle("PAUSE MENU. 2 ITEMS ON SCREEN.");
        if(control.equals("screentilt") && type.equals("play"))
        {
            sm.unregisterListener(this);
        }
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

        TableLayout mainMenu = (TableLayout)findViewById(R.id.activityMenu);
        mainMenu.setVisibility(mainMenu.INVISIBLE);
        /**
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
         **/
        view.announceForAccessibility("Pause menu.  2 items on screen.");
        TableLayout pauseMenu = (TableLayout)findViewById(R.id.pauseMenu);
        pauseMenu.setVisibility(pauseMenu.VISIBLE);
    }

    public void resume(View view)
    {
        state = "game";
        setTitle("PLAY");
        if(control.equals("screentilt") && type.equals("play"))
        {
            sm.registerListener(this, s, 1000000);
        }
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
                    if(lives > 0)
                    {
                        lives = lives - 1;
                    }

                    if(lives >= 0)
                    {
                        TextView t = (TextView)findViewById(R.id.lives);
                        t.setText("LIVES = " + String.valueOf(lives));
                        int id = getResources().getIdentifier("shortfailure","raw", getPackageName());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {

                            @Override
                            public void onCompletion(MediaPlayer mp)
                            {
                                View view = findViewById(R.id.activity_main_menu);
                                view.announceForAccessibility("LIVES = " + String.valueOf(lives));
                                introQueue.add(0, currentIntro);
                                queue.add(0, currentRoom);
                                if (type.equals("slot"))
                                {
                                    isUserMadeQueue.add(0, isUserMade);
                                }
                                runRoom();
                            }
                        });
                    }
                    if(lives == 0)
                    {
                        // endgame failure
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("endgame", "failure");
                        editor.commit();
                        Intent X = new Intent(game.this, endgame.class);
                        startActivity(X);
                    }

                }
            }.start();
            actNow = true;
        }


        TableLayout pauseMenu = (TableLayout)findViewById(R.id.pauseMenu);
        pauseMenu.setVisibility(pauseMenu.INVISIBLE);
        TableLayout activityMenu = (TableLayout)findViewById(R.id.activityMenu);
        activityMenu.setVisibility(activityMenu.VISIBLE);

        Button pause = (Button)(Button)findViewById(R.id.pause);
        if(control.equals("screentilt"))
        {
            Button left = (Button)findViewById(R.id.left);
            Button right = (Button)findViewById(R.id.right);
            Button action = (Button)(Button)findViewById(R.id.action);

            left.setVisibility(left.INVISIBLE);
            right.setVisibility(right.INVISIBLE);
            action.setVisibility(action.INVISIBLE);
        }
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
                Log.v("MEGAN DUPRIEST", "currentKey = " + currentKey);
                Log.v("MEGAN DUPRIEST", "button text = " + text);
                if(lives > 0)
                {
                    lives = lives - 1;
                }

                if(lives >= 0)
                {
                    TextView t = (TextView)findViewById(R.id.lives);
                    t.setText("LIVES = " + String.valueOf(lives));
                    int id = getResources().getIdentifier("shortfailure","raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                    {

                        @Override
                        public void onCompletion(MediaPlayer mp)
                        {
                            View view = findViewById(R.id.activity_main_menu);
                            view.announceForAccessibility("LIVES = " + String.valueOf(lives));
                            introQueue.add(0, currentIntro);
                            queue.add(0, currentRoom);
                            if (type.equals("slot"))
                            {
                                isUserMadeQueue.add(0, isUserMade);
                            }
                            runRoom();
                        }
                    });
                }
                if(lives == 0)
                {
                    // endgame failure
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("endgame", "failure");
                    editor.commit();
                    Intent X = new Intent(game.this, endgame.class);
                    startActivity(X);
                }
            }

        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(control.equals("screentilt") && type.equals("play"))
        {
            sm.unregisterListener(this);
        }
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
        if(control.equals("screentilt") && type.equals("play"))
        {
            sm.registerListener(this, s, 1000000);
        }
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
                    if(lives > 0)
                    {
                        lives = lives - 1;
                    }

                    if(lives >= 0)
                    {
                        TextView t = (TextView)findViewById(R.id.lives);
                        t.setText("LIVES = " + String.valueOf(lives));
                        int id = getResources().getIdentifier("shortfailure","raw", getPackageName());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {

                            @Override
                            public void onCompletion(MediaPlayer mp)
                            {
                                View view = findViewById(R.id.activity_main_menu);
                                view.announceForAccessibility("LIVES = " + String.valueOf(lives));
                                introQueue.add(0, currentIntro);
                                queue.add(0, currentRoom);
                                if (type.equals("slot"))
                                {
                                    isUserMadeQueue.add(0, isUserMade);
                                }
                                runRoom();
                            }
                        });
                    }
                    if(lives == 0)
                    {
                        // endgame failure
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("endgame", "failure");
                        editor.commit();
                        Intent X = new Intent(game.this, endgame.class);
                        startActivity(X);
                    }

                }
            }.start();
            actNow = true;
        }
    }
}
