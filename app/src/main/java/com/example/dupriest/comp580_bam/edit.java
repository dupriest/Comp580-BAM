package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class edit extends AppCompatActivity {

    int roomNum;
    String roomName;

    TextView textNum;
    TextView textName;

    Context context;
    SharedPreferences sharedPref;
    ArrayList<String> introQueue;
    ArrayList<String> queue;

    boolean isPlaying;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        roomNum = 0; // display roomNum + 1
        textNum = (TextView)findViewById(R.id.roomNum);
        textName = (TextView)findViewById(R.id.roomName);

        introQueue = new ArrayList<>();
        queue = new ArrayList<>();

        isPlaying = false;

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.slot1_file_key), Context.MODE_PRIVATE);

        String key;
        for(int i = 0; i < 20; i++)
        {
            key = String.valueOf(i);
            queue.add(sharedPref.getString(key, "empty"));
            key = key + "i";
            introQueue.add(sharedPref.getString(key, "empty"));
        }
        setTextNum();
        setTextName();
    }

    public void setTextNum()
    {
        textNum.setText("ROOM " + String.valueOf(roomNum+1));
    }

    public void setTextName()
    {
        roomName = queue.get(0);
        if(!queue.get(0).equals("empty"))
        {
            roomName = queue.get(0).split("_")[1];
        }
        textName.setText(roomName.toUpperCase());
    }

    public void prevRoom(View view)
    {
        if(isPlaying)
        {
            mediaPlayer.stop();
            isPlaying = false;
        }
        if(roomNum > 0)
        {
            roomNum = roomNum - 1;
            setTextNum();
            setTextName();
        }
    }

    public void nextRoom(View view)
    {
        if(isPlaying)
        {
            mediaPlayer.stop();
            isPlaying = false;
        }
        if(roomNum < 19)
        {
            roomNum = roomNum + 1;
            setTextNum();
            setTextName();
        }
    }

    public void remove(View view)
    {
        if(isPlaying)
        {
            mediaPlayer.stop();
            isPlaying = false;
        }
        roomName = "empty";
        queue.set(roomNum, roomName);
        introQueue.set(roomNum, roomName);
        setTextName();
    }

    public void add(View view)
    {
        // TODO: add interface which allows the user to select which room to put in
        // Pause the mediaPlayer if playing, stop it if it changes

    }

    public void play(View view)
    {
        // TODO: plays the appropriate music when clicked and changes to pause
        if(!introQueue.get(roomNum).equals("empty"))
        {
            Button b = (Button)view;
            b.setText("pause");
            int id = getResources().getIdentifier(introQueue.get(roomNum), "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    int id = getResources().getIdentifier(queue.get(roomNum), "raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Button b = (Button)findViewById(R.id.play);
                            b.setText("play");
                            isPlaying = false;
                        }
                    });
                }
            });
        }
    }

    public void save(View view)
    {
        // TODO: uses SharedPref to save values in queue and introQueue to the file
        SharedPreferences.Editor editor = sharedPref.edit();
        String key;
        for(int i = 0; i < 20; i++)
        {
            key = String.valueOf(i);
            editor.putString(key, queue.get(i));
            key = key + "i";
            editor.putString(key, introQueue.get(i));
        }
        editor.commit();
    }

    public void back(View view)
    {
        // TODO: Add question that asks if the user would like to save before exiting
        if(isPlaying)
        {
            mediaPlayer.stop();
            isPlaying = false;
        }
        Intent X = new Intent(this, select.class);
        startActivity(X);
    }
}
