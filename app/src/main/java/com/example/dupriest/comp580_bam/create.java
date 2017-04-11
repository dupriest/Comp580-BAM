package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class create extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setTitle("CREATE");
    }

    void back(View view)
    {
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }

    void select(View view)
    {
        Button b = (Button)view;
        Uri myUri = Uri.parse((String)b.getText());
        Intent X = new Intent(this, select.class);
        X.setData(myUri);
        startActivity(X);
    }
}
