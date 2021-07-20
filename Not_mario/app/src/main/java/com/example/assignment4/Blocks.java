package com.example.assignment4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

abstract class Blocks implements GameObject {
    public int frameHeight = 16;
    public int frameWidth = 16;
    public int scaleFactor = 3;
    public int scrollSpeed = 10;

}

class QuestionBlock extends Blocks {

    Bitmap brick;
    Bitmap dead_brick;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    GamePanel mv;
    Mario mario;
    boolean alive;
    private volatile int hit;

    QuestionBlock(GamePanel mv) {
        brick = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.block_question);
        brick = Bitmap.createScaledBitmap(brick, frameWidth, frameHeight, false);
        dead_brick = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.block_unbreakable);
        dead_brick = Bitmap.createScaledBitmap(dead_brick, frameWidth, frameHeight, false);
        alive = true;
        this.mario = mv.mario;
        this.mv = mv;
    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(brick, frameToDraw, whereToDraw, null);
        } else {
            canvas.drawBitmap(dead_brick, frameToDraw, whereToDraw, null);
        }
    }

    @Override
    public void update() {

        float distance;
        if (mario.getState() == 3 || mario.getState() == 0) {
            distance = mario.findDistance(whereToDraw);
            if (distance < 60) {
                if (alive) {
                    if (mario.isMarioTouchingTopVariation(whereToDraw)) {
                        alive = false;
                        mv.board.scorecount += 10;
                        int max = 3;
                        int min = 1;
                        Random random = new Random();
                        int randomNum = random.nextInt((max - min) + 1) + min;
                        if (hit == 0) {
                            switch (randomNum) {
                                case 1:
                                    hit++;
                                    Mushroom mushroom = new Mushroom(mv);
                                    mv.mushroomManager.add(mushroom);
                                    mushroom.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                    break;
                                case 2:
                                    hit++;
                                    Star star = new Star(mv);
                                    mv.starManager.add(star);
                                    star.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                    break;
                                case 3:
                                    hit++;
                                    Coin coin = new Coin(mv);
                                    mv.coinManager.add(coin);
                                    coin.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                    break;
                            }
                        }
                    }

                }
            }
        } else if (mario.getState() == 1 || mario.getState() == 2) {
            distance = mario.findBigDistance(whereToDraw);
            if ((distance <= 100) && (Math.abs(whereToDraw.left - mario.hitbox.left) <= 22 || (Math.abs(mario.hitbox.right - whereToDraw.right) <= 22))) {
                if (mario.isMarioTouchingTopVariation(whereToDraw)) {
                    alive = false;
                    mv.board.scorecount += 10;
                    int max = 3;
                    int min = 1;
                    Random random = new Random();
                    int randomNum = random.nextInt((max - min) + 1) + min;
                    if (hit == 0) {
                        switch (randomNum) {
                            case 1:
                                hit++;
                                Mushroom mushroom = new Mushroom(mv);
                                mv.mushroomManager.add(mushroom);
                                mushroom.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                break;
                            case 2:
                                hit++;
                                Star star = new Star(mv);
                                mv.starManager.add(star);
                                star.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                break;
                            case 3:
                                hit++;
                                Coin coin = new Coin(mv);
                                mv.coinManager.add(coin);
                                coin.whereToDraw.set(whereToDraw.left, whereToDraw.top - frameHeight * scaleFactor, whereToDraw.right, whereToDraw.bottom - frameHeight * scaleFactor);
                                break;
                        }
                    }
                }
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


class RegularBlock extends Blocks {

    private Bitmap brick;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    private boolean alive;

    RegularBlock(GamePanel mv) {
        brick = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.block_breakable);
        brick = Bitmap.createScaledBitmap(brick, frameWidth, frameHeight, false);
        this.mario = mv.mario;
        this.mv = mv;
        alive = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(brick, frameToDraw, whereToDraw, null);
        } else {
            whereToDraw.set(0, 0, 0, 0);
        }
    }

    @Override
    public void update() {
        if (mario.getState() != 0) {
            float distance = mario.findBigDistance(whereToDraw);
            if ((distance <= 100) && (Math.abs(whereToDraw.left - mario.hitbox.left) <= 18 || (Math.abs(mario.hitbox.right - whereToDraw.right) <= 18))) {
                if (alive) {
                    if (mario.isMarioTouchingTopVariation(whereToDraw)) {
                        alive = false;
                        mv.board.scorecount += 10;
                    }
                }
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

class Pipe extends Blocks {
    private Bitmap pipe;
    private PirhanaPlant plant;
    GamePanel mv;
    Mario mario;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(600, 800, 600 + frameWidth * scaleFactor, frameHeight * scaleFactor + 800);
    private boolean isMarioComing;

    Pipe(GamePanel mv) {
        pipe = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.pipe);
        pipe = Bitmap.createScaledBitmap(pipe, frameWidth, frameHeight, false);
        this.mario = mv.mario;
        this.mv = mv;
        isMarioComing = true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pipe, frameToDraw, whereToDraw, null);
    }

    @Override
    public void update() {
        float distance;
        distance = mv.mario.findDistance(whereToDraw);
        if (distance < 300 && isMarioComing) {
            plant.setIsMarioNear(true);
            isMarioComing = false;
        } else {
            if (distance > 800 && !isMarioComing) {
                plant.setIsMarioNear(false);
            }
        }
    }

    public PirhanaPlant getPlant() {
        return plant;
    }

    public void setPlant(PirhanaPlant plant) {
        this.plant = plant;
    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }
}
