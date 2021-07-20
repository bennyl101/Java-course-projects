package com.example.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

abstract class PowerUps implements GameObject {
    public int frameHeight = 16;
    public int frameWidth = 16;
    public int scaleFactor = 2;
    public int scrollSpeed = 10;
}

class Mushroom extends PowerUps {
    Bitmap mushroom;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    private boolean alive;
    private volatile boolean scoreCheck;

    Mushroom(GamePanel mv) {
        this.mv = mv;
        this.mario = mv.mario;
        mushroom = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.mushroom);
        mushroom = Bitmap.createScaledBitmap(mushroom, frameWidth, frameHeight, false);
        alive = true;
        scoreCheck = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(mushroom, frameToDraw, whereToDraw, null);
        } else {
            if (scoreCheck) {
                scoreCheck = false;
                whereToDraw.set(0, 0, 0, 0);
                mv.board.scorecount += 1000;
            }
        }
    }

    @Override
    public void update() {
        RectF temp = new RectF(mario.hitbox);
        if (temp.intersect(whereToDraw)) {
            alive = false;
            if(mario.getState() == 0) {
                mario.changeMarioState(1);
            }
            if(mario.getState() == 3){
                mario.changeMarioState( 4);
            }
        }
    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }
}

class Star extends PowerUps {
    Bitmap star;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    private boolean alive;
    private volatile boolean scoreCheck;
    private int flag;

    Star(GamePanel mv) {
        this.mv = mv;
        this.mario = mv.mario;
        star = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.star);
        star = Bitmap.createScaledBitmap(star, frameWidth, frameHeight, false);
        alive = true;
        scoreCheck = true;

    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(star, frameToDraw, whereToDraw, null);
        } else {
            if (scoreCheck) {
                scoreCheck = false;
                whereToDraw.set(0, 0, 0, 0);
                mv.board.scorecount += 1000;
            }
        }
    }

    @Override
    public void update() {
        RectF temp = new RectF(mario.hitbox);

        int check_state = mario.getState();
        if (temp.intersect(whereToDraw)) {
            alive = false;
            flag = 1;
            if (check_state == 1 && flag == 1) {
                mario.changeMarioState(2);
            }
            if (check_state == 0 && flag == 1) {
                mario.changeMarioState(3);

            }

        }

    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }
}

class Coin extends PowerUps {
    Bitmap coin;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    public boolean alive;

    Coin(GamePanel mv) {
        this.mv = mv;
        this.mario = mv.mario;
        coin = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.coin);
        coin = Bitmap.createScaledBitmap(coin, frameWidth, frameHeight, false);
        alive = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(coin, frameToDraw, whereToDraw, null);
        } else {
            whereToDraw.set(0, 0, 0, 0);
        }
    }

    @Override
    public void update() {
        RectF temp = new RectF(mario.hitbox);
        if (temp.intersect(whereToDraw)) {
            alive = false;
            mv.board.scorecount += 200;
        }
    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }

}

class Door extends PowerUps {
    Bitmap door;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);

    Door(GamePanel mv) {
        this.mv = mv;
        this.mario = mv.mario;
        door = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.door);
        door = Bitmap.createScaledBitmap(door, frameWidth, frameHeight, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(door, frameToDraw, whereToDraw, null);
    }

    @Override
    public void update() {
        RectF temp = new RectF(mario.hitbox);
        if (temp.intersect(whereToDraw)) {
            mv.nextLevel(1);
        }
    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }


}