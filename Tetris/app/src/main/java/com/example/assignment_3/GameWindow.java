package com.example.assignment_3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends View implements View.OnClickListener {
    private tetrisbackground tetrisbackground;
    private NecksedUp nextPieceWindow;
    private MainActivity main;
    private ArrayList<Piece> pieceQueue;
    public int points;

    private TextView scoreWindow;
    private TextView gameoverwindow;
    private ImageButton rotateArrow;
    private ImageButton rightArrow;
    private ImageButton downArrow;
    private ImageButton leftArrow;
    private Button quitbutton;
    private int downclickflag;

    private int dame_time_period = 600;


    private Timer dame_time = new Timer();
    private Random random = new Random();

    public GameWindow(Context context, tetrisbackground tetrisbackground, NecksedUp nextPieceWindow) {
        super(context);
        this.tetrisbackground = tetrisbackground;
        this.nextPieceWindow = nextPieceWindow;
        this.main = (MainActivity) context;
        this.pieceQueue = tetrisbackground.getPieceQueue();
        this.points = tetrisbackground.getPoints();

        this.scoreWindow = main.scoreWindow;
        this.scoreWindow.setText("Score : 0");
        this.gameoverwindow = main.gameoverwindow;

        rotateArrow = main.getRotateArrow();
        rightArrow = main.getRightArrow();
        downArrow = main.getDownArrow();
        leftArrow = main.getLeftArrow();
        quitbutton = main.getQuitButton();

        rotateArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        downArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        quitbutton.setOnClickListener(this);
        game_thread();
    }

    public void game_thread() {
        dame_time.schedule(new TimerTask() {

            @Override
            public void run() {
                main.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        invalidate();
                        if (!isGameOver()) {
                            tetrisbackground.moveDown(tetrisbackground.getCurrentPiece());
                            if (!tetrisbackground.canPieceMoveDown(tetrisbackground.getCurrentPiece())) {
                                int num_deleted_rows = tetrisbackground.checkFullLines();
                                tetrisbackground.clearAllLines(num_deleted_rows);
                                pieceQueue.remove(tetrisbackground.getCurrentPiece());
                                pieceQueue.add(new Piece(random.nextInt(7) + 1));
                                tetrisbackground.startPiece(tetrisbackground.getNextPiece());
                                if (!tetrisbackground.canPlacePiece(tetrisbackground.getCurrentPiece())) {
                                    System.out.println("********************************");
                                    System.out.println("GAME OVER!!!!!");
                                    System.out.println("********************************");
                                    tetrisbackground.placePiece(tetrisbackground.getCurrentPiece());
                                    dame_time.cancel();
                                    pieceQueue.clear();
                                    StringBuilder gameoverstring = new StringBuilder();
                                    gameoverstring.append("Game Over!");
                                    gameoverwindow.setText(gameoverstring.toString());
                                    gameoverwindow.setTextColor(Color.RED);
                                } else {
                                    tetrisbackground.placePiece(tetrisbackground.getCurrentPiece());
                                    nextPieceWindow.invalidate();
                                }
                                if (num_deleted_rows > 0) {
                                    StringBuilder string = new StringBuilder();
                                    string.append("Score: ");
                                    string.append(tetrisbackground.getPoints());
                                    scoreWindow.setText(string.toString());
                                }
                                if(tetrisbackground.getPoints() >= 100 && tetrisbackground.getPoints() < 200) {
                                    dame_time_period = 400;
                                    dame_time.cancel();
                                    dame_time = new Timer();
                                    game_thread();
                                }
                                if(tetrisbackground.getPoints() >= 200 && tetrisbackground.getPoints() <= 500) {
                                    dame_time_period = 200;
                                    dame_time.cancel();
                                    dame_time = new Timer();
                                    game_thread();
                                }
                                if(tetrisbackground.getPoints() > 500) {
                                    dame_time_period = 100;
                                    dame_time.cancel();
                                    dame_time = new Timer();
                                    game_thread();
                                }
                            }

                            invalidate();
                        }
                    }
                });
            }
        }, 0, speedup());
    }

    @Override
    public void onClick(View v) {
        if(pieceQueue.size() != 0) {
            switch (v.getId()) {
                case R.id.rightArrow:
                    tetrisbackground.moveRight(tetrisbackground.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.downArrow:
                    downclickflag = 1;
                    tetrisbackground.fastFall(tetrisbackground.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.leftArrow:
                    tetrisbackground.moveLeft(tetrisbackground.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.rotateButton:
                    tetrisbackground.rotate(tetrisbackground.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.quitButton:
                    dame_time.cancel();
                    pieceQueue.clear();
                    StringBuilder gameoverstring = new StringBuilder();
                    gameoverstring.append("Game Over!");
                    gameoverwindow.setText(gameoverstring.toString());
                    System.exit(0);
            }
        }
        if(v.getId() == R.id.quitButton){
            dame_time.cancel();
            pieceQueue.clear();
            StringBuilder gameoverstring = new StringBuilder();
            gameoverstring.append("Game Over!");
            gameoverwindow.setText(gameoverstring.toString());
            System.exit(0);
        }
    }

    public boolean isGameOver() {
        if (tetrisbackground.checkGameOver(tetrisbackground.getNextPiece())) {
            dame_time.cancel();
            pieceQueue.clear();
            StringBuilder gameoverstring = new StringBuilder();
            gameoverstring.append("Game Over!");
            gameoverwindow.setText(gameoverstring.toString());
            return true;
        }
        return false;
    }

    public void GameOverScreen() {
        //Intent intent = new Intent(this.getContext(), game_over.class);
        //getContext().startActivity(intent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        int color;
        int bh = tetrisbackground.getHeight();
        int wh = tetrisbackground.getWidth();
        for (int i = bh - 1; i >= 0; i--) {
            for (int j = wh - 1; j >= 0; j--) {
                color = tetrisbackground.colorCode(i, j);
                p.setColor(color);
                canvas.drawRect(j * 70, i * 70, j * 70 + 70, i * 70 + 70, p);
            }
        }

    }

    public tetrisbackground gettetrisbackground() {
        return tetrisbackground;
    }

    public Timer getdame_time() {
        return this.dame_time;
    }


    private int speedup() {
        int tempperiod = dame_time_period;
        if (tetrisbackground.getPoints() > 100) {
            tempperiod = 500;
        }
        if (tetrisbackground.getPoints() > 500) {
            tempperiod = 300;
        }
        if (tetrisbackground.getPoints() > 1000) {
            tempperiod = 200;
        }
        System.out.println(tempperiod);

        return tempperiod;
    }

}
