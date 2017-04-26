package com.example.dupriest.comp580_bam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

public class roomRecord extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer   mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    String state;

    Context context;
    SharedPreferences sharedPref;

    String slot;
    int cycle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record);

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        cycle = sharedPref.getInt("cycle", 1);

        if(cycle==2)
        {
            setTitle("CHOOSE TO RECORD OR SELECT PREMADE ROOM FOR ROOM SOUND EFFECT");
        }
        else
        {
            setTitle("CHOOSE TO RECORD OR SELECT PREMADE ROOM FOR ROOM DESCRIPTION");
        }
        state = "mainMenu";
        // Record to the external cache directory for visibility
        mFileName = getFilesDir().getAbsolutePath();
        mFileName = mFileName + "/" + slot + ".3gp"; // would change this to be unique to each

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    Button b;
                    if(state.equals("mainMenu"))
                    {
                        b = (Button)findViewById(R.id.play);
                    }
                    else if(state.equals("recordMenu"))
                    {
                        Log.v("MEGAN DUPRIEST", "not");
                        b = (Button)findViewById(R.id.play3);
                    }
                    else
                    {
                        b = (Button)findViewById(R.id.play2);
                    }
                    b.setText("play");
                    mStartPlaying = !mStartPlaying;
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void record(View view)
    {
        Button b = (Button)view;
        onRecord(mStartRecording);
        if (mStartRecording) {
            b.setText("Stop recording");
        } else {
            b.setText("Start recording");
        }
        mStartRecording = !mStartRecording;
    }

    public void play(View view)
    {
        Button b = (Button)view;
        onPlay(mStartPlaying);
        if (mStartPlaying) {
            b.setText("stop");
        } else {
            b.setText("play");
        }
        mStartPlaying = !mStartPlaying;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    void back(View view)
    {
        Intent X;
        if(cycle==1)
        {
            X = new Intent(this, create2.class);
        }
        else
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("cycle", 1);
            editor.commit();
            X = new Intent(this, roomRecord3.class);
        }

        startActivity(X);
    }

    void selectRoom(View view)
    {
        LinearLayout mainMenu = (LinearLayout)findViewById(R.id.mainMenu);
        LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
        mainMenu.setVisibility(mainMenu.INVISIBLE);
        choiceMenu.setVisibility(choiceMenu.VISIBLE);
        state = "choiceMenu";
        view.announceForAccessibility("Would you like to select a premade room or record your own?");
    }

    void recordYourOwn(View view)
    {
        LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
        LinearLayout recordMenu = (LinearLayout)findViewById(R.id.recordMenu);

        choiceMenu.setVisibility(choiceMenu.INVISIBLE);
        recordMenu.setVisibility(recordMenu.VISIBLE);
        state = "recordMenu";
        view.announceForAccessibility("Record Menu");
    }

    void selectYourOwn(View view)
    {
        LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
        LinearLayout premadeMenu = (LinearLayout)findViewById(R.id.premadeMenu);

        choiceMenu.setVisibility(choiceMenu.INVISIBLE);
        premadeMenu.setVisibility(premadeMenu.VISIBLE);
        state = "premadeMenu";
        view.announceForAccessibility("Select Premade Room Menu");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if(state.equals("mainMenu"))
            {
                return super.onKeyDown(keyCode, event);
            }
            else if(state.equals("choiceMenu"))
            {
                LinearLayout mainMenu = (LinearLayout)findViewById(R.id.mainMenu);
                LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
                mainMenu.setVisibility(mainMenu.VISIBLE);
                choiceMenu.setVisibility(choiceMenu.INVISIBLE);
                state = "mainMenu";
                findViewById(R.id.mainMenu).announceForAccessibility("SELECT OR RECORD ROOM DESCRIPTION");
                return true;
            }
            else if(state.equals("recordMenu"))
            {
                LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
                LinearLayout recordMenu = (LinearLayout)findViewById(R.id.recordMenu);

                choiceMenu.setVisibility(choiceMenu.VISIBLE);
                recordMenu.setVisibility(recordMenu.INVISIBLE);
                state = "choiceMenu";
                findViewById(R.id.choiceMenu).announceForAccessibility("Would you like to select a premade room or record your own?");
                return true;
            }
            else if(state.equals("premadeMenu"))
            {
                LinearLayout choiceMenu = (LinearLayout)findViewById(R.id.choiceMenu);
                LinearLayout premadeMenu = (LinearLayout)findViewById(R.id.premadeMenu);

                choiceMenu.setVisibility(choiceMenu.VISIBLE);
                premadeMenu.setVisibility(premadeMenu.INVISIBLE);
                state = "choiceMenu";
                findViewById(R.id.choiceMenu).announceForAccessibility("Would you like to select a premade room or record your own?");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void selectRecord(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("roomRecordMethod", "record");
        editor.commit();
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }

    public void selectPremade(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("roomRecordMethod", "premade");
        editor.commit();
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }
}
