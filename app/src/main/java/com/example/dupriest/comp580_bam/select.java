package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

public class select extends AppCompatActivity {

    String slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Context context = getApplicationContext();
        SharedPreferences sharedPref1 = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref1.getString("slot", "slot 1");
        slot = slot.toUpperCase();
        setTitle(slot);
    }

    void back(View view)
    {
        Intent X = new Intent(this, create.class);
        startActivity(X);
    }

    void edit(View view)
    {
        Intent X = new Intent(this, edit.class);
        startActivity(X);
    }

    void delete(View view)
    {
        TableLayout mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        TableLayout deleteMenu = (TableLayout)findViewById(R.id.deleteMenu);

        mainMenu.setVisibility(mainMenu.INVISIBLE);
        deleteMenu.setVisibility(deleteMenu.VISIBLE);
        view.announceForAccessibility("Do you really want to delete this saved slot?  Select NO to cancel.");

    }

    public void yes(View view)
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref2;
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

        SharedPreferences.Editor editor = sharedPref2.edit();
        String key;
        for(int i = 0; i < 20; i++)
        {
            key = String.valueOf(i);
            editor.putString(key, "empty");
            key = key + "i";
            editor.putString(key, "empty");
        }
        editor.commit();

        TableLayout mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        TableLayout deleteMenu = (TableLayout)findViewById(R.id.deleteMenu);

        mainMenu.setVisibility(mainMenu.VISIBLE);
        deleteMenu.setVisibility(deleteMenu.INVISIBLE);

        CharSequence text = "SLOT DELETED!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void no(View view)
    {
        TableLayout mainMenu = (TableLayout)findViewById(R.id.mainMenu);
        TableLayout deleteMenu = (TableLayout)findViewById(R.id.deleteMenu);

        mainMenu.setVisibility(mainMenu.VISIBLE);
        deleteMenu.setVisibility(deleteMenu.INVISIBLE);
        view.announceForAccessibility(slot + " not deleted");
    }

}
