package com.example.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import static com.example.assignment4.MainThread.canvas;

class Background implements GameObject {
    GamePanel mv;

    Bitmap level_1;
    Bitmap level_2;
    Bitmap level_3;
    Bitmap ground;
    Bitmap current;

    private int levelHeightOffset;
    private int levelWidthOffset;

    private float Height;
    private float Width;

    private int speed = 8;

    public boolean endOfScreen;
    public boolean isScreenMoving;

    RectF groundcanvasFrame = new RectF(0, 16 * 3 * 25 - 16 * 3 * 4, 16 * 3 * 125, 16 * 3 * 25);

    Background(Context context, GamePanel mv) {
        level_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.level_1);
        level_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        level_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.level_3);
        ground = BitmapFactory.decodeResource(context.getResources(), R.drawable.ground);
        this.mv = mv;
    }

    @Override
    public void draw(Canvas canvas) {
        Height = canvas.getHeight();
        Width = canvas.getWidth();
        int ScaledHeight = current.getScaledHeight(canvas);
        int ScaledWidth = current.getScaledWidth(canvas);
        Rect boardFrame = new Rect(0, 0, (int) Width, (int) (Height));
        Rect levelFrame = new Rect(levelWidthOffset, levelHeightOffset, (int) (levelWidthOffset + Width), (int) ScaledHeight);
        canvas.drawBitmap(current, levelFrame, boardFrame, null);
    }

    public void drawGround(Canvas canvas, int row, int col) {
        Width = canvas.getWidth();
        Height = canvas.getHeight();
        Rect groundFrame = new Rect(0, 0, ground.getWidth(), ground.getHeight());
        canvas.drawBitmap(ground, groundFrame, groundcanvasFrame, null);
    }

    @Override
    public void update() {

    }

    public void update(Mario mario) {
        int level_width = current.getWidth();
        if (((levelWidthOffset + (int) Width) < (level_width)) && (mario.hitbox.right >= 950) && mv.moveRight) {
            levelWidthOffset += speed;

            endOfScreen = false;
            isScreenMoving = true;
        } else {
            isScreenMoving = false;
            endOfScreen = true;


        }

        if (mario.hitbox.left <= 400 && mv.moveLeft) {
            if ((levelWidthOffset > 0)) {
                levelWidthOffset -= speed;
                endOfScreen = false;
                isScreenMoving = true;
            } else {
                isScreenMoving = false;
                endOfScreen = true;
            }
        }

    }

    public void changeCurrentLevel(int x) {
        switch (x) {
            case 1:
                current = level_1;
                levelWidthOffset = 0;
                break;
            case 2:
                current = level_2;
                levelWidthOffset = 0;

                break;
            case 3:
                current = level_3;
                levelWidthOffset = 0;
                break;
        }
    }

    public int getEndGame() {
        return levelWidthOffset;
    }
}
