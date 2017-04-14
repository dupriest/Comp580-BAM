package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class select extends AppCompatActivity {

    String slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        slot = sharedPref.getString("slot", "slot 1");
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

}
