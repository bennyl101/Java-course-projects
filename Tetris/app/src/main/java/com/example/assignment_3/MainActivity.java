package com.example.assignment_3;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.*;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.graphics.*;

public class MainActivity extends Activity{

    private Button quitButton;
    private ImageButton rightArrow;
    private ImageButton leftArrow;
    private ImageButton downArrow;
    private ImageButton rotateArrow;

    private View tetrisPlayFieldView;
    public TextView scoreWindow;
    public TextView gameoverwindow;
    private NecksedUp nextPiece;
    private tetrisbackground tetrisbackground = new tetrisbackground();

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstansceState) {
        super.onCreate(savedInstansceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_tetris);

        quitButton = findViewById(R.id.quitButton);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrow = findViewById(R.id.leftArrow);
        downArrow = findViewById(R.id.downArrow);
        rotateArrow = findViewById(R.id.rotateButton);

        scoreWindow = findViewById(R.id.scoreWindow);
        gameoverwindow = findViewById(R.id.gameoverwindow);

        ConstraintLayout NextPieceContainer = findViewById(R.id.nextPieceview);
        nextPiece = new NecksedUp(getApplicationContext(), tetrisbackground);
        nextPiece.setBackgroundColor(Color.YELLOW);
        NextPieceContainer.addView(nextPiece, 0);

        tetrisPlayFieldView = new GameWindow(this, tetrisbackground, nextPiece);
        LinearLayout.LayoutParams playfield_settings = new LinearLayout.LayoutParams(700, 1400);
        tetrisPlayFieldView.setLayoutParams(playfield_settings);
        linearLayout = findViewById(R.id.linearLayout);
        tetrisPlayFieldView.setBackgroundColor(Color.YELLOW);
        linearLayout.addView(tetrisPlayFieldView);
    }

    public ImageButton getDownArrow() {
        return downArrow;
    }

    public ImageButton getLeftArrow() {
        return leftArrow;
    }

    public ImageButton getRightArrow() {
        return rightArrow;
    }

    public ImageButton getRotateArrow() {
        return rotateArrow;
    }

    public Button getQuitButton() {
        return quitButton;
    }
}