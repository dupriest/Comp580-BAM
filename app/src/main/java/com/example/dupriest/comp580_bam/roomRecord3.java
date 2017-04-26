package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class roomRecord3 extends AppCompatActivity {

    Context context;
    SharedPreferences sharedPref;
    String slot;
    int cycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record3);
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
        cycle = sharedPref.getInt("cycle", 1);
    }

    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }

    public void next(View view)
    {
        Intent X;
        if(cycle==1)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("cycle", 2);
            editor.commit();
            X = new Intent(this, roomRecord.class);
        }
        else
        {
            X = new Intent(this, roomRecord4.class);
        }

        startActivity(X);
    }

}
