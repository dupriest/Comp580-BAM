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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

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

    String roomString = "empty";
    String originalRoomString = "empty";

    String[] sorts = {"ALL PREMADE", "A TO G PREMADE", "H TO M PREMADE", "N TO S PREMADE", "T TO Z PREMADE"};
    ArrayList<String> choices;

    int currentChoice;
    int currentSort;

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
        else
        {
            currentSort = -1;
            currentChoice = 0;
            sort(findViewById(R.id.sort));
        }

        if(cycle==1)
        {
            setTitle("RECORD ROOM DESCRIPTION. 6 ITEMS ON SCREEN.");
            String prev = sharedPref.getString(slot + " intro", "empty");
            mFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_1.3gp";
            pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_2.3gp";
            if(prev.equals(pFileName))
            {
                mFileName = pFileName;
                pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_1.3gp";
            }
        }
        else
        {
            setTitle("RECORD ROOM SOUND EFFECT. 6 ITEMS ON SCREEN.");
            String prev = sharedPref.getString(slot, "empty");
            mFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_3.3gp";
            pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_4.3gp";
            if(prev.equals(pFileName))
            {
                mFileName = pFileName;
                pFileName = getFilesDir().getAbsolutePath() + "/" + slot + "_3.3gp";
            }

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

    public String getRoomName(String roomString)
    {
        if(!roomString.equals("empty"))
        {
            return roomString.split("_")[1];
        }
        else
        {
            return "empty";
        }
    }

    public String getRoomIntroString(String roomString)
    {
        if(!roomString.equals("empty"))
        {
            return "intro" + roomString.substring(4, roomString.length());
        }
        else
        {
            return "empty";
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            if(roomRecordMethod.equals("record"))
            {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
            }
            else
            {
                if(cycle==1)
                {
                    int id = getResources().getIdentifier(getRoomIntroString(roomString),"raw", getPackageName());
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);
                }
                else
                {
                    int id = getResources().getIdentifier(roomString,"raw", getPackageName());
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);
                }
            }
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
        File fileRecord = new File(mFileName);
        boolean isNotEmpty = !roomString.equals("empty");



        SharedPreferences.Editor editor = sharedPref.edit();

        if (roomRecordMethod.equals("record") && (fileRecord.exists()))
        {
            if (cycle == 1)
            {
                editor.putString(slot + " intro", mFileName);
            }
            else
            {
                editor.putString(slot, mFileName);
            }
        }
        else if(roomRecordMethod.equals("premade") && isNotEmpty)
        {
            if(cycle == 1)
            {
                editor.putString(slot + " intro", getRoomIntroString(roomString));
            }
            else
            {
                editor.putString(slot, roomString);
            }
        }

        /*
        if (cycle == 1)
        {
            if (roomRecordMethod.equals("record"))
            {
                editor.putString(slot + " intro", mFileName);
            } else {
                editor.putString(slot + " intro", getRoomIntroString(roomString));
            }

        }
        else if (cycle == 2)
        {
            if (roomRecordMethod.equals("record"))
            {
                editor.putString(slot, mFileName);
            } else
            {
                editor.putString(slot, roomString);
            }
        }
        */
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "PLEASE SELECT OR RECORD SOUND BEFORE CONTINUING";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if((roomRecordMethod.equals("record") && (fileRecord.exists())) | (roomRecordMethod.equals("premade") && isNotEmpty))
        {
            editor.commit();
            Intent X = new Intent(this, roomRecord3.class);
            startActivity(X);
        }
    }

    public void sort(View view)
    {
        //TODO: create sort function
        /*
        if(isPlaying)
        {
            play(findViewById(R.id.sound));
        }*/
        if(currentSort < sorts.length-1)
        {
            currentSort = currentSort + 1;
        }
        else
        {
            currentSort = 0;
        }
        currentChoice = 0;

        Field[] fields = R.raw.class.getFields();
        choices = new ArrayList<>();

        if(sorts[currentSort].equals("ALL PREMADE"))
        {
            Button b = (Button)view;
            if(!b.getText().equals("sort\nall premade"))
            {
                b.setText("sort\nall premade");
                view.announceForAccessibility("sort all premade");
            }
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room"))
                {
                    choices.add(name);
                }
            }
        }
        else if(sorts[currentSort].equals("A TO G PREMADE"))
        {
            Button b = (Button)view;
            b.setText("sort\na to g premade");
            view.announceForAccessibility("sort a to g premade");
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room") && name.substring(5,6).compareTo("h") < 0)
                {
                    choices.add(name);
                }
            }
        }
        else if(sorts[currentSort].equals("H TO M PREMADE"))
        {
            Button b = (Button)view;
            b.setText("sort\nh to m premade");
            view.announceForAccessibility("sort h to m premade");
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room") && name.substring(5,6).compareTo("h") >= 0 && name.substring(5,6).compareTo("n") < 0)
                {
                    choices.add(name);
                }
            }
        }
        else if(sorts[currentSort].equals("N TO S PREMADE"))
        {
            Button b = (Button)view;
            b.setText("sort\nn to s premade");
            view.announceForAccessibility("sort n to s premade");
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room") && name.substring(5,6).compareTo("n") >= 0 && name.substring(5,6).compareTo("t") < 0)
                {
                    choices.add(name);
                }
            }
        }
        else if(sorts[currentSort].equals("T TO Z PREMADE"))
        {
            Button b = (Button)view;
            b.setText("sort\nt to z premade");
            view.announceForAccessibility("sort t to z premade");
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 4 && name.substring(0,4).equals("room") && name.substring(5,6).compareTo("t") >= 0 && name.substring(5,6).compareTo("z") <= 0)
                {
                    choices.add(name);
                }
            }
        }

        Collections.sort(choices);

        if(!choices.isEmpty()) {
            roomString = choices.get(currentChoice);
        }
        else
        {
            roomString = "empty";
        }
        Button b = (Button) findViewById(R.id.select);
        b.setText("select\n" + getRoomName(roomString));

    }

    public void selectRoom(View view)
    {
        //TODO: create selectRoom function
        /*
        if(isPlaying)
        {
            play(findViewById(R.id.sound));
        }*/
        if(currentChoice < choices.size()-1)
        {
            currentChoice = currentChoice + 1;
        }
        else
        {
            currentChoice = 0;
        }
        if(!choices.isEmpty()) {
            roomString = choices.get(currentChoice);
        }
        else
        {
            roomString = "empty";
        }
        Button b = (Button) findViewById(R.id.select);
        b.setText("select\n" + getRoomName(roomString));
        view.announceForAccessibility(getRoomName(roomString));

    }

}
