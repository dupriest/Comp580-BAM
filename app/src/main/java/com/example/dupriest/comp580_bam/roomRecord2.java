package com.example.dupriest.comp580_bam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;

public class roomRecord2 extends AppCompatActivity {


    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    String mFileName = null;
    String pFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    String state;

    Context context;
    SharedPreferences sharedPref;
    String slot;
    String roomRecordMethod;
    int cycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record2);

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        cycle = sharedPref.getInt("cycle", 1);
        roomRecordMethod = sharedPref.getString("roomRecordMethod", "record");

        if(roomRecordMethod.equals("record"))
        {
            TableLayout recordMenu = (TableLayout)findViewById(R.id.recordMenu);
            TableLayout premadeMenu = (TableLayout)findViewById(R.id.premadeMenu);
            recordMenu.setVisibility(recordMenu.VISIBLE);
            premadeMenu.setVisibility(premadeMenu.INVISIBLE);
        }

        if(cycle==1)
        {
            mFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_1.3gp";
            pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_2.3gp";
        }
        else
        {
            mFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_3.3gp";
            pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_4.3gp";
        }

        Log.v("MEGAN DUPRIEST", "mFileName = " + mFileName);
        Log.v("MEGAN DUPRIEST", "pFileName = " + pFileName);

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
                    if(roomRecordMethod.equals("record"))
                    {
                        b = (Button)findViewById(R.id.play);
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
        if(mStartRecording)
        {
            String temp = mFileName;
            mFileName = pFileName;
            pFileName = temp;
        }
        Button b = (Button)view;
        onRecord(mStartRecording);
        if (mStartRecording) {
            b.setText("stop");
        } else {
            b.setText("record");
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

    public void prevRecord(View view)
    {
        String temp = mFileName;
        mFileName = pFileName;
        pFileName = temp;
        Context context = getApplicationContext();
        CharSequence text = "PREVIOUS RECORDING LOADED!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Log.v("MEGAN DUPRIEST", "mFileName = " + mFileName);
        Log.v("MEGAN DUPRIEST", "pFileName = " + pFileName);
    }


    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord.class);
        startActivity(X);
    }

    public void next(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        if(cycle==1)
        {
            editor.putString(slot + " intro", mFileName);
        }
        else if(cycle==2)
        {
            editor.putString(slot, mFileName);
        }
        editor.commit();
        Intent X = new Intent(this, roomRecord3.class);
        startActivity(X);
    }


}
