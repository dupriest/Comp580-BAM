package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class roomRecord3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_record3);
    }

    public void back(View view)
    {
        Intent X = new Intent(this, roomRecord2.class);
        startActivity(X);
    }

    public void next(View view)
    {
        Intent X = new Intent(this, roomRecord4.class);
        startActivity(X);
    }

}
