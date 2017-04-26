package com.example.dupriest.comp580_bam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

public class roomRecord2 extends AppCompatActivity {

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

    }


    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord.class);
        startActivity(X);
    }

    public void next(View view)
    {
        Intent X = new Intent(this, roomRecord3.class);
        startActivity(X);
    }


}
