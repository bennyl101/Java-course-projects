package com.example.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

abstract class Enemy implements GameObject {
    public int frameHeight = 16;
    public int frameWidth = 16;
    public int scaleFactor = 2;


}

class Goomba extends Enemy {
    private boolean alive;
    Bitmap goomba;
    private int xPosition, yPosition;
    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    RectF whereToDraw = new RectF(xPosition, yPosition, xPosition + frameWidth * scaleFactor, yPosition + frameHeight * scaleFactor);
    private int walkspeed = 1;
    private int scrollSpeed = 10;
    private int fallspeed = 6;

    private int currentFrame = 0;
    private int frameCount = 2;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 800;

    private int walker = 0;

    private int starmanintersect =1;

    private int hit= 0;


    GamePanel mv;

    Goomba(GamePanel mv) {
        alive = true;
        goomba = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.goomba);
        goomba = Bitmap.createScaledBitmap(goomba, frameWidth * 3, frameHeight, false);
        this.mv = mv;
    }

    @Override
    public void draw(Canvas canvas) {
        if (alive) {
            walker++;
            getCurrentFrame();
            if (walker < 150) {
                movementLeft();
            }
            if (walkspeed > 150 && walker < 300) {
               movementRight();
            }
            if (walker > 300) {
                walker = 0;
            }
            canvas.drawBitmap(goomba, frameToDraw, whereToDraw, null);

        } else {
            whereToDraw.set(0, 0, 0, 0);
        }
    }

    @Override
    public void update() {
        gravity(mv);
        RectF temp = new RectF(mv.mario.hitbox);
        if (temp.intersect(whereToDraw)) {
            if (mv.mario.isMarioTouchingBottomVariation(whereToDraw)) {
                mv.board.scorecount += 200;
                alive = false;
            } else {
                switch (mv.mario.getState()) {
                    case 1:

                            mv.mario.changeMarioState(0);
                            hit++;
                        hit++;

                        break;
                    case 0:
                        if(hit == 0) {
                            mv.mario.changeMarioState(-1);
                            mv.mario.hitbox.set(10, 10, 10 + mv.mario.hitbox.right - mv.mario.hitbox.left, 10 + mv.mario.hitbox.bottom - mv.mario.hitbox.top);
                            mv.nextLevel(0);
                        }else{
                            hit++;
                            if(hit > 100){
                                hit = 0;
                            }
                        }
                        break;
                    case 2:
                        starmanintersect = 1;
                        alive = false;
                        break;
                    case 3:
                        alive = false;
                        starmanintersect = 1;
                        break;
                }
            }
        }
    }

    private void getCurrentFrame() {

        long time = System.currentTimeMillis();


        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }

            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;
        }

    }


    private void movementLeft() {
        whereToDraw.set(whereToDraw.left - walkspeed, whereToDraw.top, whereToDraw.right - walkspeed, whereToDraw.bottom);
    }

    private void movementRight() {
        whereToDraw.set(whereToDraw.left + walkspeed, whereToDraw.top, whereToDraw.right + walkspeed, whereToDraw.bottom);
    }


    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }

    public void gravity(GamePanel mv) {
        if (checkGravityCollision() || !canGoombaJump()) {
            RectF temp = new RectF(whereToDraw.left, whereToDraw.top + fallspeed, whereToDraw.right, whereToDraw.bottom + fallspeed);
            if (!mv.checkBlockAndGroundCollisions(temp) || !canGoombaJump()) {
                whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
            }
        }
    }

    private boolean canGoombaJump() {
        if (isMarioTouchingBottomVariation(mv.background.groundcanvasFrame)) {
            return true;
        }
        RegularBlock block = findClosestBrick(mv.blockmanager);
        QuestionBlock questionBlock = findClosestQBrick(mv.questionBlocksMangager);
        float q_distance = findDistance(questionBlock.whereToDraw);
        float block_distance = findDistance(block.whereToDraw);
        if ((block_distance <= 60) || (q_distance <= 60)) {
            if (block_distance < q_distance) {
                if (isMarioTouchingBottomVariation(block.whereToDraw)) {
                    return true;
                }
            } else {
                if (isMarioTouchingBottomVariation(questionBlock.whereToDraw)) {
                    return true;
                }
            }
        }
        return false;
    }

    public RegularBlock findClosestBrick(ArrayList<RegularBlock> blockmanager) {
        RegularBlock brick = blockmanager.get(0);
        float shortest_distance = 30000;
        float distance;
        float mario_xCenter = (whereToDraw.left + whereToDraw.right) / 2;
        float mario_yCenter = (whereToDraw.bottom + whereToDraw.top) / 2;
        float block_xCenter;
        float block_yCenter;

        for (RegularBlock a : blockmanager) {

            block_xCenter = (a.whereToDraw.left + a.whereToDraw.right) / 2;
            block_yCenter = (a.whereToDraw.top + a.whereToDraw.bottom) / 2;
            distance = (float) Math.sqrt(((block_xCenter - mario_xCenter) * (block_xCenter - mario_xCenter)
                    + (block_yCenter - mario_yCenter) * (block_yCenter - mario_yCenter)));
            if (distance < shortest_distance) {
                shortest_distance = distance;
                brick = a;
            }
        }


        return brick;
    }

    public QuestionBlock findClosestQBrick(ArrayList<QuestionBlock> questionBlocksManager) {

        float shortest_distance = 30000;
        float distance;
        float mario_xCenter = (whereToDraw.left + whereToDraw.right) / 2;
        float mario_yCenter = (whereToDraw.bottom + whereToDraw.top) / 2;
        float block_xCenter;
        float block_yCenter;

        QuestionBlock q_brick = questionBlocksManager.get(0);
        for (QuestionBlock a : questionBlocksManager) {
            block_xCenter = (a.whereToDraw.left + a.whereToDraw.right) / 2;
            block_yCenter = (a.whereToDraw.top + a.whereToDraw.bottom) / 2;
            distance = (float) Math.sqrt(((block_xCenter - mario_xCenter) * (block_xCenter - mario_xCenter)
                    + (block_yCenter - mario_yCenter) * (block_yCenter - mario_yCenter)));
            if (distance < shortest_distance) {
                shortest_distance = distance;
                q_brick = a;
            }
        }
        return q_brick;
    }

    public float findDistance(RectF block) {
        float distance;
        float mario_xCenter = (whereToDraw.left + whereToDraw.right) / 2;
        float mario_yCenter = (whereToDraw.bottom + whereToDraw.top) / 2;
        float block_xCenter = (block.left + block.right) / 2;
        float block_yCenter = (block.top + block.bottom) / 2;

        distance = (float) Math.sqrt(((block_xCenter - mario_xCenter) * (block_xCenter - mario_xCenter)
                + (block_yCenter - mario_yCenter) * (block_yCenter - mario_yCenter)));

        return distance;
    }

    private boolean isMarioTouchingBottomVariation(RectF rect) {
        return (Math.abs(whereToDraw.bottom - rect.top) <= 10);
    }

    public boolean checkGravityCollision() {
        /*
        true for possible gravity movement
        false for no possible gravity movement
*/

        for (RegularBlock a : mv.blockmanager) {
            if (isMarioNotTouching(a.whereToDraw)) {
                return false;
            }
        }


        if (isMarioNotTouching(mv.background.groundcanvasFrame)) {
            return true;
        }
        return false;
    }

    private boolean isMarioNotTouching(RectF rect) {
        if (isMarioNotTouchingBottom(rect))
            return true;
        if (isMarioNotTouchingTop(rect))
            return true;
        if (isMarioNotTouchingLeft(rect))
            return true;
        if (isMarioNotTouchingRight(rect))
            return true;
        return false;
    }

    private boolean isMarioNotTouchingBottom(RectF rect) {
        return !((whereToDraw.bottom - rect.top) >= 0);
    }

    private boolean isMarioNotTouchingRight(RectF rect) {
        return !((whereToDraw.right - rect.left) >= 0);
    }

    private boolean isMarioNotTouchingLeft(RectF rect) {
        return !((whereToDraw.left - rect.right) <= 0);
    }

    private boolean isMarioNotTouchingTop(RectF rect) {
        return !((whereToDraw.top - rect.bottom) <= 0);
    }
}


class PirhanaPlant extends Enemy {
    private boolean IsMarioNear;
    private Bitmap plant;
    Pipe pipe;
    private int xPosition = 0;
    private int yPosition = 0;
    GamePanel mv;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight * 2);
    RectF whereToDraw = new RectF(xPosition, yPosition, xPosition + frameWidth * scaleFactor, yPosition + frameHeight * 2 * scaleFactor);

    private int currentFrame = 0;
    private int frameCount = 2;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 200;
    private int scrollSpeed = 10;

    private int frameUPPER = 72;
    private int comeUpSpeed = 3;
    private int counter = 0;

    PirhanaPlant(GamePanel mv, Pipe pipe) {
        IsMarioNear = false;
        plant = BitmapFactory.decodeResource(mv.getContext().getResources(), R.drawable.pirhana_plant);
        plant = Bitmap.createScaledBitmap(plant, frameWidth * 2, frameHeight * 2, false);
        this.mv = mv;
        this.pipe = pipe;
    }

    private void getCurrentFrame() {

        long time = System.currentTimeMillis();


        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }

            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        getCurrentFrame();
        canvas.drawBitmap(plant, frameToDraw, whereToDraw, null);
    }

    @Override
    public void update() {
        if (IsMarioNear) {
            if (whereToDraw.top >= pipe.whereToDraw.top - frameUPPER) {
                moveUp();
            }
            counter++;
            if (counter >= 250) {
                if (whereToDraw.top <= pipe.whereToDraw.top - frameUPPER) {
                    setIsMarioNear(false);
                    counter = 0;
                }
            }
        } else {
            if (whereToDraw.top < pipe.whereToDraw.top) {
                moveDown();
            }
        }
        RectF temp = new RectF(mv.mario.hitbox);
        if (temp.intersect(whereToDraw)) {
            switch (mv.mario.getState()) {
                case 1:
                    mv.mario.changeMarioState(0);
                    break;
                case 0:
                    mv.mario.changeMarioState(-1);
                    mv.mario.hitbox.set(10, 10, 10 + mv.mario.hitbox.right - mv.mario.hitbox.left, 10 + mv.mario.hitbox.bottom - mv.mario.hitbox.top);
                    mv.nextLevel(0);
                    break;
            }
        }
    }

    public void moveRight() {
        whereToDraw.set(whereToDraw.left + scrollSpeed, whereToDraw.top, whereToDraw.right + scrollSpeed, whereToDraw.bottom);
    }

    public void moveLeft() {
        whereToDraw.set(whereToDraw.left - scrollSpeed, whereToDraw.top, whereToDraw.right - scrollSpeed, whereToDraw.bottom);
    }


    private void moveUp() {
        whereToDraw.set(whereToDraw.left, whereToDraw.top - comeUpSpeed, whereToDraw.right, whereToDraw.bottom - comeUpSpeed);
    }

    private void moveDown() {
        whereToDraw.set(whereToDraw.left, whereToDraw.top + comeUpSpeed, whereToDraw.right, whereToDraw.bottom + comeUpSpeed);

    }

    public void setIsMarioNear(boolean state) {
        IsMarioNear = state;
    }
}