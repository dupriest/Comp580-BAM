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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class edit extends AppCompatActivity {

    int roomNum;
    String roomString;
    String originalRoomString;

    TextView textNum;
    TextView textName;

    TableLayout mainMenu;
    TableLayout addMenu;

    Context context;
    SharedPreferences sharedPref;
    ArrayList<String> introQueue;
    ArrayList<String> queue;
    String[] sorts = {"ALL PREMADE", "ORIGINAL", "A TO G PREMADE", "H TO M PREMADE", "N TO S PREMADE", "T TO Z PREMADE", "USER MADE"};
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
        currentSort = -1;

        textNum = (TextView)findViewById(R.id.roomNum);
        textName = (TextView)findViewById(R.id.roomName);

        mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        addMenu = (TableLayout)findViewById(R.id.addMenu);

        introQueue = new ArrayList<>();
        queue = new ArrayList<>();
        choices = new ArrayList<>();

        isPlaying = false;

        context = getApplicationContext();
        if(slot.equals("EDIT SLOT 1"))
        {
            sharedPref = context.getSharedPreferences(getString(R.string.slot1_file_key), Context.MODE_PRIVATE);
        }
        else if(slot.equals("EDIT SLOT 2"))
        {
            sharedPref = context.getSharedPreferences(getString(R.string.slot2_file_key), Context.MODE_PRIVATE);
        }
        else
        {
            sharedPref = context.getSharedPreferences(getString(R.string.slot3_file_key), Context.MODE_PRIVATE);
        }

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
        roomString = queue.get(roomNum);
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
            play(findViewById(R.id.play));
        }
        if(roomNum > 0)
        {
            roomNum = roomNum - 1;
            setTextNum();
            setTextName();
            view.announceForAccessibility("Room " + (roomNum+1) + ", " + getRoomName(queue.get(roomNum)));
        }
    }

    public void nextRoom(View view)
    {
        if(isPlaying)
        {
            play(findViewById(R.id.play));
        }
        if(roomNum < 19)
        {
            roomNum = roomNum + 1;
            setTextNum();
            setTextName();
            view.announceForAccessibility("Room " + (roomNum+1) + ", " + getRoomName(queue.get(roomNum)));
        }
    }

    public void remove(View view)
    {
        if(isPlaying)
        {
            play(findViewById(R.id.play));
        }
        roomString = "empty";
        queue.set(roomNum, roomString);
        introQueue.set(roomNum, roomString);
        setTextName();
        view.announceForAccessibility("Room " + (roomNum+1) + " is now empty");
    }

    public void add(View view)
    {
        // TODO: add interface which allows the user to select which room to put in
        // Pause the mediaPlayer if playing, stop it if it changes
        if(isPlaying)
        {
            play(findViewById(R.id.play));
        }
        currentChoice = 0;
        currentSort = -1;
        originalRoomString = roomString;
        mainMenu.setVisibility(mainMenu.INVISIBLE);
        addMenu.setVisibility(addMenu.VISIBLE);
        view.announceForAccessibility("Select which room to add");
        sort(findViewById(R.id.sort));
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
            b.setText("play");
        }
        else
        {
            if(!getRoomIntroString(roomString).equals("empty"))
            {
                Button b = (Button)view;
                b.setText("pause");
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
                                    b2.setText("play");
                                }
                                isPlaying = false;
                            }
                        });
                    }
                });
            }
            else
            {
                view.announceForAccessibility("room empty. no audio.");
            }
        }
    }

    public void save(View view)
    {
        // TODO: uses SharedPref to save values in queue and introQueue to the file
        if(isPlaying)
        {
            play(findViewById(R.id.play));
        }
        Button back = (Button)findViewById(R.id.back);
        back.setClickable(false);
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
        back.setClickable(true);
        Context context = getApplicationContext();
        CharSequence text = "GAME SAVED!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void back(View view)
    {
        // TODO: Add question that asks if the user would like to save before exiting
        if(isPlaying)
        {
            play(findViewById(R.id.play));
        }
        TableLayout mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        TableLayout exitMenu = (TableLayout)findViewById(R.id.exitMenu);
        mainMenu.setVisibility(mainMenu.INVISIBLE);
        exitMenu.setVisibility(exitMenu.VISIBLE);
        view.announceForAccessibility("Would you like to save before exiting?");
    }

    public void sort(View view)
    {
        //TODO: create sort function
        if(isPlaying)
        {
            play(findViewById(R.id.sound));
        }
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
        if(sorts[currentSort].equals("ORIGINAL"))
        {
            Button b = (Button)view;
            b.setText("sort\noriginal");
            view.announceForAccessibility("sort original");
            choices.add(originalRoomString);
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
        else if(sorts[currentSort].equals("USER MADE"))
        {
            Button b = (Button)view;
            b.setText("sort\nuser made");
            view.announceForAccessibility("sort user made");
            for (int i = 0; i < fields.length - 1; i++)
            {
                String name = fields[i].getName();
                if(name.length() >= 5 && name.substring(0,5).equals("uroom"))
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
        if(isPlaying)
        {
            play(findViewById(R.id.sound));
        }
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

    public void closeAddMenu(View view)
    {
        //TODO: create closeAddMenu function
        if(isPlaying)
        {
            play(findViewById(R.id.sound));
        }
        queue.set(roomNum, roomString);
        introQueue.set(roomNum, getRoomIntroString(roomString));
        setTextName();
        view.announceForAccessibility("Room " + (roomNum+1) + "is now " + getRoomName(roomString));
        addMenu.setVisibility(addMenu.INVISIBLE);
        mainMenu.setVisibility(mainMenu.VISIBLE);
    }

    public void yes(View view)
    {
        save(findViewById(R.id.save));
        Intent X = new Intent(this, select.class);
        startActivity(X);
    }

    public void no(View view)
    {
        Intent X = new Intent(this, select.class);
        startActivity(X);
    }
}
