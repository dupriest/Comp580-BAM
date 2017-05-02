package com.example.dupriest.comp580_bam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class tutorials extends AppCompatActivity {

    int page;
    TextView textView;
    String[] tutorialTexts = {"Welcome to the tutorials for Over Hear!" +
            " a thrilling sound adventure game.  Below, we'll list the different" +
            " tutorial sections in order of appearance. Use next and back to navigate" +
            " through them and main menu to exit the tutorials. Their text will appear in this area of the screen.\nTutorials" +
            "\n1 Basic Navigation Tips\n2 Play Game\n3 Difficulty Settings\n4 Test Controls" +
            "\n5 Create Game\n6 Create Room\n7 Using Created Rooms in Create Game",
    "Over Hear! was designed with low vision or no vision users in mind. It's interface reflects this. Over Hear! is compatible" +
            " with Android's TalkBack.  Each screen has either four or six buttons in rows of two and either two or three columns " +
            "respectively. TalkBack will even alert you to how many items there are on a screen. The back button, which returns you " +
            "to the previous screen, is always in the bottom right corner. The next button, which takes you to the next screen, is " +
            "always in the top right corner. Keep these in mind when navigating the menus.",
    "Over Hear! is a sound adventure game which randomizes the ordering of twenty rooms that the player must navigate through to find " +
            "the treasure and win the game. Each room is structured like this: first, a description of the room is played, then, when the " +
            "sound effect starts playing, the user must react by pressing the correct button (or tilting the phone, in screen tilt mode) in " +
            "order to successfully escape the room. The action button is pressed when the sound effect comes out of both headphones. But left " +
            "and right are trickier. Listen to the room description to decide if you should move towards the sound or away from the sound. For " +
            "example, if the room describes a mean dog and the barking comes from the left, its safe to say you'd want to move right to avoid it. And be" +
            " warned, some rooms can have sound that comes from the left or the right and you won't know until the sound effect plays. When the game is done," +
            "you have the option to either replay the game or return to the main menu.",
    "There are three difficulty settings which can make Over Hear! challenging for master adventurers or easier for noobies. Lives denotes the number of times " +
            "you can fail a room before it's game over.  There are three options: infinite, five, or one. \nTime denotes the amount of time you have to react to the" +
            " sound effect before you lose a life and the room replays. There are three options: infinite, 10 seconds, or 2 seconds.\nControl is what controls you use" +
            " to react to room sound effects. Buttons provides you with physical buttons on the screen to press. With action in the top left, left in the bottom left, " +
            "right in the bottom right, and the pause menu in the top right. Screen Tilt allows you to use your phone as the controller. Hold the phone parallel to the " +
            "floor and tilt the screen up towards you to select action.  Tilt the left side down to select left.  And finally, tilt the right side down to select right.\n" +
            "The difficulty settings should save automatically when you exit the difficulty menu.",
    "Test Controls is to allow users to interact with the play game controls setup before actually trying the game.  Currently, however, it's not particularly useful for " +
            "Screen Tilt controls.",
    "Create Game is a way for the player to make their own adventure sequence.  Users can select a save slot, one of three, and select edit to start. This allows you to select" +
            " the order in which rooms appear. The menu has six items. The top left and right buttons navigate between each of the twenty rooms. In between them is text that " +
            "tells you which room number you're on and what sound plays in that room. The bottom left button is the add room button.  This changes what sound is played in that " +
            "particular room. This brings up a menu of four items where you can choose sort (which simply makes it easier to find the room sound you're looking for) and select " +
            "(which chooses the room sound).  The play button plays the sounds of the selected room, and confirm puts this sound in the current room and closes the add menu.  " +
            "Similarly, the middle bottom button play also plays the room's sound.  And then finally, the bottom right is back, which also asks you if you'd like to save your" +
            " slot before exiting.",
    "Create Room is a way for the player to assemble their own Over Hear! room. Users can select a save slot, one of three, and choose for the room description and room sound " +
            "effects whether they want to record their own sounds or choose from existing premade rooms.  The user will also be asked to decide the sound effect direction and " +
            "the button to press on success. Note, the sound effect direction allows you to choose from left, right, center, left or right.  The button however, only has the " +
            "option of either towards sound or away from sound.  So for example, if you select sound direction to be left, selecting the button towards sound means the button " +
            "to press is left.  And if you selected away from sound, the button to press would be right.  At the end, you are allowed to listen to the entire room, save and exit.",
    "You can use your own created rooms as a room in your own created games.  To select, open the edit menu of a slot.  Navigate to which room number to add your room to.  Click" +
            "the add button.  Now, choose the user made sort from the sort button.  Your options should be slot 1, slot 2, or slot 3 so long as you have created rooms in each slot."};
    String[] tutorialTitles = {"TUTORIALS INTRODUCTION. 4 ITEMS ON SCREEN.", "1 Basic Navigation Tips", "2 Play Game",
            "3 Difficulty Settings", "4 Test Controls", "5 Create Game", "6 Create Room", "7 Using Created Rooms in Create Game"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);
        textView = (TextView)findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        setTitle("TUTORIALS INTRODUCTION. 4 ITEMS ON SCREEN.");
        page = 0;
    }

    public void mainMenu(View view)
    {
        Intent X = new Intent(this, MainMenu.class);
        startActivity(X);
    }

    public void next(View view)
    {
        if(page < 7)
        {
            page = page + 1;
        }
        else
        {
            page = 0;
        }
        textView.setText(tutorialTexts[page]);
        setTitle(tutorialTitles[page].toUpperCase());
        view.announceForAccessibility(tutorialTitles[page]);
    }

    public void back(View view)
    {
        if(page > 0)
        {
            page = page - 1;
        }
        else
        {
            page = 7;
        }

        textView.setText(tutorialTexts[page]);
        setTitle(tutorialTitles[page].toUpperCase());
        view.announceForAccessibility(tutorialTitles[page]);
    }
}
