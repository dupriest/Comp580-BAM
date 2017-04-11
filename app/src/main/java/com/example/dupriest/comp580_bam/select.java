package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class select extends AppCompatActivity {

    String slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        slot = getIntent().getData().toString();
        slot = slot.toUpperCase();
        setTitle(slot);
    }

    void back(View view)
    {
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }
}
