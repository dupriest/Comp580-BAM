package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class edit extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    int roomNum;
    String roomString;
    String originalRoomString;

    TextView textNum;
    TextView textName;

    TableLayout mainMenu;
    TableLayout addMenu;

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences sharedPref1;
    ArrayList<String> introQueue;
    ArrayList<String> queue;
    ArrayList<Boolean> isUserMadeQueue;
    String[] sorts = {"ORIGINAL", "REMOVE", "ALL PREMADE", "A TO G PREMADE", "H TO M PREMADE", "N TO S PREMADE", "T TO Z PREMADE", "USER MADE"};
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
        sharedPref1 = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String slot = sharedPref1.getString("slot", "slot 1");
        String create = sharedPref1.getString("create", "game");
        slot = "EDIT " + slot.toUpperCase();
        String text = "EDIT " + create.toUpperCase() + " " + sharedPref1.getString("slot", "slot 1").toUpperCase() + ". 6 ITEMS ONSCREEN.";
        setTitle(text);

        roomNum = 0; // display roomNum + 1
        currentChoice = 0;
        currentSort = -1;

        textNum = (TextView)findViewById(R.id.roomNum);
        //textName = (TextView)findViewById(R.id.roomName);

        mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        addMenu = (TableLayout)findViewById(R.id.addMenu);

        introQueue = new ArrayList<>();
        queue = new ArrayList<>();
        isUserMadeQueue = new ArrayList<>();
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
        String value;
        for(int i = 0; i < 20; i++)
        {
            key = String.valueOf(i);
            value = sharedPref.getString(key, "empty");

            key = key + "iu";
            isUserMadeQueue.add(sharedPref.getBoolean(key, false));
            if (sharedPref.getBoolean(key, false))
            {
                String newIntro = sharedPref1.getString(sharedPref1.getString("slot", "slot 1") + " intro", "empty");
                String newSlot = sharedPref1.getString(sharedPref1.getString("slot", "slot 1"), "empty");
                queue.add(newSlot);
                introQueue.add(newIntro);

            }
            else
            {
                key = String.valueOf(i);
                queue.add(value);
                key = key + "i";
                introQueue.add(sharedPref.getString(key, "empty"));
            }
        }
        roomString = queue.get(0); // ex: room_lotsofbats_right_left
        setTextNum();
        setTextName();
    }

    public String getRoomName(String roomString)
    {
        if(!roomString.equals("empty"))
        {
            int L = roomString.length();
            if(roomString.substring(L-12, L-8).equals("slot"))
            {
                return roomString.substring(L-12, L-6);
            }
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
            int L = roomString.length();
            if(roomString.substring(L-12, L-8).equals("slot"))
            {
                String s = roomString.substring(L-12, L-6);
                return sharedPref1.getString(s + " intro", "empty");
            }
            return "intro" + roomString.substring(4, roomString.length());
        }
        else
        {
            return "empty";
        }
    }

    public void setTextNum()
    {
        String[] oldText = ((String)textNum.getText()).split("\n");
        textNum.setText("ROOM " + String.valueOf(roomNum+1) + "\n" + oldText[1]);
    }

    public void setTextName()
    {
        roomString = queue.get(roomNum);
        String[] oldText = ((String)textNum.getText()).split("\n");
        if(!roomString.equals("empty"))
        {
            textNum.setText(oldText[0] + " \n" + getRoomName(queue.get(roomNum)).toUpperCase());
        }
        else
        {
            textNum.setText(oldText[0] + " \n" + queue.get(roomNum).toUpperCase());
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
                Button bSort = (Button) findViewById(R.id.sort);
                Log.v("MEGAN DUPRIEST", (String)(bSort.getText().subSequence(5,9)));
                int L = ((Button)findViewById(R.id.select)).getText().length();
                if((addMenu.getVisibility()==addMenu.VISIBLE && sorts[currentSort].equals("USER MADE")) |
                        (mainMenu.getVisibility()==mainMenu.VISIBLE && isUserMadeQueue.get(roomNum)) |
                        (addMenu.getVisibility()==addMenu.VISIBLE && sorts[currentSort].equals("ORIGINAL") &&((Button) findViewById(R.id.select)).getText().subSequence(L-6,L-2).equals("slot")))
                {
                    mediaPlayer = new MediaPlayer();
                    try {
                        Log.v("MEGAN DUPRIEST", getRoomIntroString(roomString));
                        mediaPlayer.setDataSource(getRoomIntroString(roomString));
                        mediaPlayer.prepare();
                    }
                    catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                }
                else
                {
                    int id = getResources().getIdentifier(getRoomIntroString(roomString), "raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                }
                mediaPlayer.start();
                isPlaying = true;
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Button bSort = (Button) findViewById(R.id.sort);
                        int L = ((Button)findViewById(R.id.select)).getText().length();
                        if((addMenu.getVisibility()==addMenu.VISIBLE && sorts[currentSort].equals("USER MADE")) |
                                (mainMenu.getVisibility()==mainMenu.VISIBLE && isUserMadeQueue.get(roomNum)) |
                                (addMenu.getVisibility()==addMenu.VISIBLE && sorts[currentSort].equals("ORIGINAL") &&((Button) findViewById(R.id.select)).getText().subSequence(L-6,L-2).equals("slot")))
                        {
                            mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(roomString);
                                mediaPlayer.prepare();
                            }
                            catch (IOException e) {
                                Log.e(LOG_TAG, "prepare() failed");
                            }
                        }
                        else
                        {
                            int id = getResources().getIdentifier(roomString, "raw", getPackageName());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
                        }
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

    public void save()
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
            key = key + "u";
            editor.putBoolean(key, isUserMadeQueue.get(i));
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
        if(sorts[currentSort].equals("REMOVE"))
        {
            Button b = (Button)view;
            b.setText("sort\nremove");
            view.announceForAccessibility("sort remove");
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
            // TODO: FIX THIS
            Button b = (Button)view;
            b.setText("sort\nuser made");
            view.announceForAccessibility("sort user made");

            for(int i = 1 ; i < 4; i++)
            {
                String slot = "slot " + String.valueOf(i);
                boolean complete = sharedPref1.getBoolean(slot + " complete", false);
                if(complete)
                {
                    choices.add(sharedPref1.getString(slot, "empty"));
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
        if(currentSort==7)
        {
            isUserMadeQueue.set(roomNum, true);
        }
        else
        {
            isUserMadeQueue.set(roomNum, false);
        }
        setTextName();
        view.announceForAccessibility("Room " + (roomNum+1) + "is now " + getRoomName(roomString));
        addMenu.setVisibility(addMenu.INVISIBLE);
        mainMenu.setVisibility(mainMenu.VISIBLE);
    }

    public void yes(View view)
    {
        save();
        Intent X = new Intent(this, select.class);
        startActivity(X);
    }

    public void no(View view)
    {
        Intent X = new Intent(this, select.class);
        startActivity(X);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back(findViewById(R.id.back));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
