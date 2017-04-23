package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class create extends AppCompatActivity {

    Context context;
    SharedPreferences sharedPref;
    String create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        create = sharedPref.getString("create", "game");
        setTitle("CREATE " + create.toUpperCase());
    }

    void back(View view)
    {
        Intent X = new Intent(this, create2.class);
        startActivity(X);
    }

    void select(View view)
    {
        Button b = (Button)view;
        Intent X;
        if(create.equals("game"))
        {
            X = new Intent(this, select.class);
        }
        else
        {
            X = new Intent(this, roomRecord.class);
        }

        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("slot", (String)b.getText());
        editor.commit();
        startActivity(X);
    }
}
