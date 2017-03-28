package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    void play(View view)
    {
        Button button = (Button)view;
        String text = (String)button.getText();
        Uri myUri = Uri.parse(text);
        Intent X = new Intent(this, game.class);
        X.setData(myUri);
        startActivity(X);
    }
}
