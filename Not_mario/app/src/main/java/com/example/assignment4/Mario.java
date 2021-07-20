package com.example.assignment4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

class Mario extends MainActivity implements GameObject {

    GamePanel mv;

    private static int jump_counter;

    private int dame_time;
    private int kawahi_time;
    private int currentFrame = 0;
    private int frameCount = 4;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 50;

    private int fallspeed = 10;
    private int walkspeed = 6;

    private int marioHeightCorrection = 10;
    private int marioWidthCorrection = 8;
    private int hitbox_shrink = 1;

    Bitmap bitmap_Mario;
    Bitmap bitmap_baby_mario;
    Bitmap flipped_mario;
    Bitmap flipped_baby_mario;
    Bitmap star_mario_big;
    Bitmap flipped_star_mario_big;
    Bitmap star_mario_baby;
    Bitmap flipped_star_mario_small;
    Bitmap dead_mario;

    private int baby_mario_frame_width = 16;
    private int baby_mario_frame_height = 16;

    private int frameWidth = 16;
    private int frameHeight = 32;
    private int frameWidthOffset = 0;
    private int frameHeightOffset = 0;

    private int scaleFactor = 2;
    private int framesNum = 21;

    private float marioXPosition = 10;
    private float marioYPosition = 10;

    private int state = 0;

    private boolean invincible;
    // A rectangle to define an area of the
    // sprite sheet that represents 1 frame
    private Rect frameToDraw = new Rect(
            frameWidthOffset,
            frameHeightOffset,
            frameWidth + frameWidthOffset,
            frameHeight + frameHeightOffset);

    // A rect that defines an area of the screen
    // on which to draw
    RectF whereToDraw = new RectF(
            marioXPosition,
            marioYPosition,
            marioXPosition + (frameWidth + marioWidthCorrection - 2) * scaleFactor,
            marioYPosition + (frameHeight + marioHeightCorrection + 12) * scaleFactor);

    private Rect starframeToDraw = new Rect(
            frameWidthOffset,
            frameHeightOffset,
            frameWidth + frameWidthOffset,
            frameHeight + frameHeightOffset);

    // A rect that defines an area of the screen
    // on which to draw
    RectF starwhereToDraw = new RectF(
            marioXPosition,
            marioYPosition,
            marioXPosition + (frameWidth + marioWidthCorrection - 2) * scaleFactor,
            marioYPosition + (frameHeight + marioHeightCorrection + 12) * scaleFactor);

    private Rect baby_frameToDraw = new Rect(
            frameWidthOffset,
            frameHeightOffset,
            baby_mario_frame_width + frameWidthOffset,
            baby_mario_frame_height + frameHeightOffset);

    RectF baby_whereToDraw = new RectF(
            marioXPosition,
            marioYPosition,
            marioXPosition + (baby_mario_frame_width + marioWidthCorrection) * scaleFactor,
            marioYPosition + (baby_mario_frame_height + marioHeightCorrection) * scaleFactor);

    private Rect star_frameToDraw = new Rect(
            frameWidthOffset,
            frameHeightOffset,
            baby_mario_frame_width + frameWidthOffset,
            baby_mario_frame_height + frameHeightOffset);

    RectF star_whereToDraw = new RectF(
            marioXPosition,
            marioYPosition,
            marioXPosition + (baby_mario_frame_width + marioWidthCorrection) * scaleFactor,
            marioYPosition + (baby_mario_frame_height + marioHeightCorrection) * scaleFactor);

    /*private Rect starbaby_frametoDraw = new Rect(
            frameWidthOffset,
            frameHeightOffset,
            baby_mario_frame_width + frameWidthOffset,
            baby_mario_frame_height + frameHeightOffset);*/

    RectF hitbox = baby_whereToDraw;


    Mario(Context context, GamePanel mv) {
        bitmap_Mario = BitmapFactory.decodeResource(context.getResources(), R.drawable.mario_sheet);


        bitmap_Mario = Bitmap.createScaledBitmap(bitmap_Mario,
                (frameWidth) * framesNum,
                (frameHeight),
                true);

        star_mario_big = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_mario_big);


        star_mario_big = Bitmap.createScaledBitmap(star_mario_big,
                (frameWidth) * framesNum,
                (frameHeight),
                true);

        bitmap_baby_mario = BitmapFactory.decodeResource(context.getResources(), R.drawable.baby_mario_sheet);

        bitmap_baby_mario = Bitmap.createScaledBitmap(bitmap_baby_mario,
                (baby_mario_frame_width) * 7,
                (baby_mario_frame_height),
                true);

        star_mario_baby = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_small_mario);

        star_mario_baby = Bitmap.createScaledBitmap(star_mario_baby,
                (baby_mario_frame_width) * 7,
                (baby_mario_frame_height),
                true);


        Matrix flipper = new Matrix();
        flipper.preScale(-1, 1);
        flipped_baby_mario = Bitmap.createBitmap(bitmap_baby_mario, 0, 0, bitmap_baby_mario.getWidth(), bitmap_baby_mario.getHeight(), flipper, false);
        flipped_mario = Bitmap.createBitmap(bitmap_Mario, 0, 0, bitmap_Mario.getWidth(), bitmap_Mario.getHeight(), flipper, false);
        flipped_star_mario_small = Bitmap.createBitmap(star_mario_baby, 0, 0, star_mario_baby.getWidth(), star_mario_baby.getHeight(), flipper, false);
        flipped_star_mario_big = Bitmap.createBitmap(star_mario_big, 0, 0, star_mario_big.getWidth(), star_mario_big.getHeight(), flipper, false);


//        dead_mario = Bitmap.createBitmap(bitmap_baby_mario, (bitmap_baby_mario.getWidth() / 7) * 6, 0, bitmap_baby_mario.getWidth(), bitmap_baby_mario.getHeight());

        invincible = false;

        jump_counter = 0;
        this.mv = mv;
    }

    public void getCurrentFrame() {

        long time = System.currentTimeMillis();

        if (mv.moveRight) {// Only animate if bob is moving
            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
            if (state == 0) {
                baby_frameToDraw.left = currentFrame * frameWidth;
                baby_frameToDraw.right = baby_frameToDraw.left + frameWidth;
            }
            if (state == 1) {
                frameToDraw.left = currentFrame * frameWidth;
                frameToDraw.right = frameToDraw.left + frameWidth;
            }
            if (state == 2) {
                starframeToDraw.left = currentFrame * frameWidth;
                starframeToDraw.right = starframeToDraw.left + frameWidth;
            }
            if (state == 3) {
                star_frameToDraw.left = currentFrame * frameWidth;
                star_frameToDraw.right = star_frameToDraw.left + frameWidth;
            }


        } else if (mv.moveLeft) {
            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
            if (state == 0) {
                baby_frameToDraw.left = (frameCount + 2 - currentFrame) * frameWidth;
                baby_frameToDraw.right = baby_frameToDraw.left + frameWidth;
            }
            if (state == 1) {
                frameToDraw.left = (frameCount + 16 - currentFrame) * frameWidth;
                frameToDraw.right = frameToDraw.left + frameWidth;
            }
            if (state == 2) {
                starframeToDraw.left = (frameCount + 16 - currentFrame) * frameWidth;
                starframeToDraw.right = starframeToDraw.left + frameWidth;
            }
            if (state == 3) {
                star_frameToDraw.left = (frameCount + 2 - currentFrame) * frameWidth;
                star_frameToDraw.right = star_frameToDraw.left + frameWidth;
            }
        } else {
            currentFrame = 0;
            baby_frameToDraw.left = currentFrame * frameWidth;
            baby_frameToDraw.right = baby_frameToDraw.left + frameWidth;
            star_frameToDraw.left = currentFrame * frameWidth;
            star_frameToDraw.right = star_frameToDraw.left + frameWidth;
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;
            starframeToDraw.left = currentFrame * frameWidth;
            starframeToDraw.right = starframeToDraw.left + frameWidth;
        }

        //update the left and right values of the source of
        //the next frame on the spritesheet


    }

    public void draw(Canvas canvas) {

        getCurrentFrame();
        if (state == 0) {
            if (mv.moveRight) {
                canvas.drawBitmap(bitmap_baby_mario,
                        baby_frameToDraw,
                        baby_whereToDraw,
                        null);
            } else if (mv.moveLeft) {
                canvas.drawBitmap(flipped_baby_mario,
                        baby_frameToDraw,
                        baby_whereToDraw,
                        null);
            } else {
                canvas.drawBitmap(bitmap_baby_mario,
                        baby_frameToDraw,
                        baby_whereToDraw,
                        null);
            }

        }

        if (state == 1) {
            if (mv.moveRight) {
                canvas.drawBitmap(bitmap_Mario,
                        frameToDraw,
                        whereToDraw,
                        null);
            } else if (mv.moveLeft) {
                canvas.drawBitmap(flipped_mario,
                        frameToDraw,
                        whereToDraw,
                        null);
            } else {
                canvas.drawBitmap(bitmap_Mario,
                        frameToDraw,
                        whereToDraw,
                        null);
            }
        }
        if (state == 2) {
            dame_time++;
            getCurrentFrame();
            if (mv.moveRight) {
                canvas.drawBitmap(star_mario_big,
                        starframeToDraw,
                        starwhereToDraw,
                        null);
            } else if (mv.moveLeft) {
                canvas.drawBitmap(flipped_star_mario_big,
                        starframeToDraw,
                        starwhereToDraw,
                        null);
            } else {
                canvas.drawBitmap(star_mario_big,
                        starframeToDraw,
                        starwhereToDraw,
                        null);
            }
            if (dame_time > 250) {
                changeMarioState(1);
                dame_time = 0;
                whereToDraw.set(starwhereToDraw.left, starwhereToDraw.top, starwhereToDraw.right, starwhereToDraw.bottom);
                hitbox = whereToDraw;

                kawahi_time = 0;
            }
        }
        if (state == 3) {
            kawahi_time++;
            getCurrentFrame();
            if (mv.moveRight) {
                canvas.drawBitmap(star_mario_baby,
                        star_frameToDraw,
                        star_whereToDraw,
                        null);
            } else if (mv.moveLeft) {
                canvas.drawBitmap(flipped_star_mario_small,
                        star_frameToDraw,
                        star_whereToDraw,
                        null);
            } else {
                canvas.drawBitmap(star_mario_baby,
                        star_frameToDraw,
                        star_whereToDraw,
                        null);
            }
            if (kawahi_time > 250) {
                baby_whereToDraw.set(star_whereToDraw.left, star_whereToDraw.top, star_whereToDraw.right, star_whereToDraw.bottom);
                hitbox = baby_whereToDraw;
                changeMarioState(0);
                kawahi_time = 0;
            }

        }
    }

    public void update() {
        /*
        if (state == 0) {
            baby_whereToDraw.set(baby_whereToDraw.left, baby_whereToDraw.bottom - + (baby_mario_frame_height + marioHeightCorrection) * scaleFactor, baby_whereToDraw.left + (baby_mario_frame_width + marioWidthCorrection) * scaleFactor, baby_whereToDraw.bottom );
        }
        if (state == 1) {

        }
        */
    }


    public void gravity(GamePanel mv) {
        if (mv.checkGravityCollision() && !mv.canMarioJump(this)) {
            RectF temp = new RectF(hitbox.left, hitbox.top + fallspeed, hitbox.right, hitbox.bottom + fallspeed);
            if (!mv.checkBlockAndGroundCollisions(temp) && !mv.canMarioJump(this)) {
                if (state == 1) {
                    whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = whereToDraw;
                }
                if (state == 2) {
                    starwhereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = starwhereToDraw;
                }
                if (state == 0) {
                    baby_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = baby_whereToDraw;

                }
                if (state == 3) {
                    star_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = star_whereToDraw;

                }
            }
        }
    }


    public boolean isMarioNotTouchingBottom(RectF rect) {
        return !((hitbox.bottom - rect.top) >= 0);
    }

    private boolean isMarioNotTouchingRight(RectF rect) {
        return !((hitbox.right - rect.left) >= 0);
    }

    private boolean isMarioNotTouchingLeft(RectF rect) {
        return !((hitbox.left - rect.right) <= 0);
    }

    public boolean isMarioNotTouchingTop(RectF rect) {
        return !((hitbox.top - rect.bottom) <= 0);
    }

    public boolean isMarioNotTouching(RectF rect) {
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

    private boolean isTempMarioNotTouchingBottom(RectF rect, RectF temp) {

        return !((temp.bottom - rect.top) >= 0);

    }

    private boolean isTempMarioNotTouchingRight(RectF rect, RectF temp) {
        return !((temp.right - rect.left) >= 0);
    }

    private boolean isTempMarioNotTouchingLeft(RectF rect, RectF temp) {
        return !((temp.left - rect.right) <= 0);
    }

    public boolean isTempMarioNotTouchingTop(RectF rect, RectF temp) {
        return !((temp.top - rect.bottom) <= 0);
    }

    public boolean isTempMarioNotTouching(RectF rect, RectF temp) {
        if (isTempMarioNotTouchingBottom(rect, temp)) {
            return true;
        }
        if (isTempMarioNotTouchingTop(rect, temp)) {
            return true;
        }
        if (isTempMarioNotTouchingLeft(rect, temp)) {
            return true;
        }
        if (isTempMarioNotTouchingRight(rect, temp)) {
            return true;
        }
        return false;
    }

    public boolean isMarioTouchingBottomVariation(RectF rect) {
        return (Math.abs(hitbox.bottom - rect.top) <= 10);
    }

    public boolean isMarioTouchingTopVariation(RectF rect) {
        return (Math.abs(hitbox.top - rect.bottom) <= 15);
    }

    public void changeMarioState(int x) {
        if (state != 0) {
            if(state == 3){
                state = 0;
                baby_whereToDraw.set(hitbox.left, hitbox.bottom - (baby_mario_frame_width + marioWidthCorrection) * scaleFactor , hitbox.right, hitbox.bottom);
                hitbox = baby_whereToDraw;
            }else
            if (x == 0) {
                state = 0;
                baby_whereToDraw.set(hitbox.left, hitbox.top + 25 * scaleFactor, hitbox.right + 3 * scaleFactor, hitbox.bottom);
                hitbox = baby_whereToDraw;
                //Baby Mario
            }


        }
        if (state != 1) {
            if(state == 2){
                state = 1;
                whereToDraw.set(hitbox.left, hitbox.top , hitbox.right , hitbox.bottom);
                hitbox = whereToDraw;
            }else
            if (x == 1) {
                state = 1;
                whereToDraw.set(hitbox.left, hitbox.top - 25 * scaleFactor, hitbox.right - 3 * scaleFactor, hitbox.bottom);
                hitbox = whereToDraw;

                //Big Mario
            }

        }
        if (state != 2) {
            if (x == 2) {
                state = 2;
                starwhereToDraw.set(hitbox.left, hitbox.top, hitbox.right, hitbox.bottom);
                hitbox = starwhereToDraw;
                //GOLD MARIO
            }
        }

        if (state != 3) {
            if (x == 3) {
                state = 3;
                star_whereToDraw.set(hitbox.left, hitbox.top, hitbox.right, hitbox.bottom);
                hitbox = star_whereToDraw;

                //Baby Mario
            }
        }
        if (state != 4) {
            if (x == 4) {
                state = 2;
                starwhereToDraw.set(hitbox.left, hitbox.top - 25 * scaleFactor, hitbox.right - 3 * scaleFactor, hitbox.bottom);
                hitbox = starwhereToDraw;
            }
        }

        if (state != -1) {
            if (x == -1) {
                changeMarioState(0);
                mv.board.lives -= 1;
            }
        }

    }


    public RegularBlock findClosestBrick(ArrayList<RegularBlock> blockmanager) {
        RegularBlock brick = blockmanager.get(0);
        float shortest_distance = 30000;
        float distance;
        float mario_xCenter = (hitbox.left + hitbox.right) / 2;
        float mario_yCenter = (hitbox.bottom + hitbox.top) / 2;
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
        float mario_xCenter = (hitbox.left + hitbox.right) / 2;
        float mario_yCenter = (hitbox.bottom + hitbox.top) / 2;
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
        float mario_xCenter = (hitbox.left + hitbox.right) / 2;
        float mario_yCenter = (hitbox.bottom + hitbox.top) / 2;
        float block_xCenter = (block.left + block.right) / 2;
        float block_yCenter = (block.top + block.bottom) / 2;

        distance = (float) Math.sqrt(((block_xCenter - mario_xCenter) * (block_xCenter - mario_xCenter)
                + (block_yCenter - mario_yCenter) * (block_yCenter - mario_yCenter)));

        return distance;
    }

    public float findBigDistance(RectF block) {
        float distance;
        float mario_xCenter = (hitbox.left + hitbox.right) / 2;
        float mario_yCenter = ((hitbox.top) + hitbox.top) / 2;
        float block_xCenter = (block.left + block.right) / 2;
        float block_yCenter = (block.top + block.bottom) / 2;

        distance = (float) Math.sqrt(((block_xCenter - mario_xCenter) * (block_xCenter - mario_xCenter)
                + (block_yCenter - mario_yCenter) * (block_yCenter - mario_yCenter)));

        return distance;
    }

    public void goRight(GamePanel mv) {
        RectF temp = new RectF(hitbox.left + walkspeed, hitbox.top, hitbox.right + walkspeed, hitbox.bottom);
        if (!mv.background.isScreenMoving) {
            if (!mv.checkBlockAndGroundCollisions(temp)) {
                if (state == 1) {
                    whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = whereToDraw;
                }
                if (state == 0) {
                    baby_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = baby_whereToDraw;
                }
                if (state == 2) {
                    starwhereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = starwhereToDraw;
                }
                if (state == 3) {
                    star_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = star_whereToDraw;
                }

            }
        }
    }

    public void goLeft(GamePanel mv) {
        RectF temp = new RectF(hitbox.left - walkspeed, hitbox.top, hitbox.right - walkspeed, hitbox.bottom);
        if (!mv.background.isScreenMoving) {
            if (!mv.checkBlockAndGroundCollisions(temp)) {
                if (state == 1) {
                    whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = whereToDraw;
                }
                if (state == 0) {
                    baby_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = baby_whereToDraw;
                }
                if (state == 2) {
                    starwhereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = starwhereToDraw;
                }
                if (state == 3) {
                    star_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                    hitbox = star_whereToDraw;
                }
            }
        }
    }

    public void jump(GamePanel mv) {
        if (jump_counter == walkspeed * walkspeed) {
            jump_counter = 0;
            mv.fall = true;
            mv.jump = false;
            return;
        }
        RectF temp = new RectF(hitbox.left, hitbox.top - walkspeed * 4 + jump_counter, hitbox.right, hitbox.bottom - walkspeed * 4 + jump_counter);
        if (!mv.checkBlockAndGroundCollisions(temp)) {
            jump_counter++;
            if (state == 1) {
                whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                hitbox = whereToDraw;
            }
            if (state == 0) {
                baby_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                hitbox = baby_whereToDraw;
            }
            if (state == 2) {
                starwhereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                hitbox = starwhereToDraw;
            }
            if (state == 3) {
                star_whereToDraw.set(temp.left, temp.top, temp.right, temp.bottom);
                hitbox = star_whereToDraw;
            }
        } else {
            jump_counter = 0;
            mv.fall = true;
            mv.jump = false;
        }

    }

    public int getState() {
        return state;
    }
}
