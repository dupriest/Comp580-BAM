package com.example.dupriest.comp580_bam;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

public class create extends AppCompatActivity {

    Context context;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setTitle("SELECT CREATE GAME OR CREATE ROOM");
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    void createGame(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("create", "game");
        editor.commit();
        Intent X = new Intent(this, create2.class);
        startActivity(X);
    }

    void createRoom(View view)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("create", "room");
        editor.commit();
        Intent X = new Intent(this, create2.class);
        startActivity(X);
    }

    void back(View view)
    {
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }
}