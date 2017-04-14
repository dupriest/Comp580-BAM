package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class edit extends AppCompatActivity {

    int roomNum;
    String roomString;

    TextView textNum;
    TextView textName;

    TableLayout mainMenu;
    TableLayout addMenu;

    Context context;
    SharedPreferences sharedPref;
    ArrayList<String> introQueue;
    ArrayList<String> queue;
    String[] sorts = {"All, A-G, H-M, N-S, T-Z, USER MADE"};
    ArrayList<String> choices;

    int currentChoice;
    int currentSort;

    boolean isPlaying;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Context context = getApplicationContext();
        SharedPreferences sharedPref1 = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String slot = sharedPref1.getString("slot", "slot 1");
        slot = "EDIT " + slot.toUpperCase();
        setTitle(slot);

        roomNum = 0; // display roomNum + 1
        currentChoice = 0;
        currentSort = 0;

        textNum = (TextView)findViewById(R.id.roomNum);
        textName = (TextView)findViewById(R.id.roomName);

        mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        addMenu = (TableLayout)findViewById(R.id.addMenu);

        introQueue = new ArrayList<>();
        queue = new ArrayList<>();
        choices = new ArrayList<>();

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
        roomString = queue.get(0); // ex: room_lotsofbats_right_left
        setTextNum();
        setTextName();
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

    public void setTextNum()
    {
        textNum.setText("ROOM " + String.valueOf(roomNum+1));
    }

    public void setTextName()
    {
        if(!roomString.equals("empty"))
        {
            textName.setText(getRoomName(queue.get(roomNum)).toUpperCase());
        }
        else
        {
            textName.setText(queue.get(roomNum).toUpperCase());
        }
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
        roomString = "empty";
        queue.set(roomNum, roomString);
        introQueue.set(roomNum, roomString);
        setTextName();
    }

    public void add(View view)
    {
        // TODO: add interface which allows the user to select which room to put in
        // Pause the mediaPlayer if playing, stop it if it changes
        mainMenu.setVisibility(mainMenu.INVISIBLE);
        addMenu.setVisibility(addMenu.VISIBLE);
        sort(null);
        Button b = (Button)findViewById(R.id.select);
        roomString = choices.get(currentChoice);
        b.setText("select\n" + getRoomName(roomString));
        //addMenu.getVisibility();
    }

    public void play(View view)
    {
        // TODO: change this to work with the add menu too
        if(isPlaying)
        {
            mediaPlayer.stop();
            isPlaying = false;
            Button b = (Button)view;
            b.setText("play room\nsound");
        }
        else
        {
            if(!getRoomIntroString(roomString).equals("empty"))
            {
                Button b = (Button)view;
                b.setText("pause\n");
                int id = getResources().getIdentifier(getRoomIntroString(roomString), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                mediaPlayer.start();
                isPlaying = true;
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        int id = getResources().getIdentifier(roomString, "raw", getPackageName());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if(mainMenu.getVisibility()==mainMenu.VISIBLE)
                                {
                                    Button b1 = (Button)findViewById(R.id.play);
                                    b1.setText("play");
                                }
                                else
                                {

                                    Button b2 = (Button) findViewById(R.id.sound);
                                    b2.setText("play room\nsound");
                                }
                                isPlaying = false;
                            }
                        });
                    }
                });
            }
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

    public void sort(View view)
    {
        //TODO: create sort function
        if(currentSort < sorts.length-1)
        {
            currentSort = currentSort + 1;
        }
        else
        {
            currentSort = 0;
        }

        Field[] fields = R.raw.class.getFields();

        for (int i = 0; i < fields.length - 1; i++)
        {
            String name = fields[i].getName();
            if(name.length() >= 4 && name.substring(0,4).equals("room"))
            {
                choices.add(name);
            }
        }
        Collections.sort(choices);

    }

    public void selectRoom(View view)
    {
        //TODO: create selectRoom function
        if(currentChoice < choices.size()-1)
        {
            currentChoice = currentChoice + 1;
        }
        else
        {
            currentChoice = 0;
        }
        roomString = choices.get(currentChoice);
        Button b = (Button)findViewById(R.id.select);
        b.setText("select\n" + getRoomName(roomString));
    }

    public void closeAddMenu(View view)
    {
        //TODO: create closeAddMenu function
        queue.set(roomNum, roomString);
        introQueue.set(roomNum, getRoomIntroString(roomString));
        setTextName();
        addMenu.setVisibility(addMenu.INVISIBLE);
        mainMenu.setVisibility(mainMenu.VISIBLE);
    }
}
