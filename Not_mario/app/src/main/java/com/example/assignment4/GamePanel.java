package com.example.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    public Mario mario;
    public Background background;
    public Board board;
    public Door door;

    public ArrayList<RegularBlock> blockmanager = new ArrayList<>();
    public ArrayList<QuestionBlock> questionBlocksMangager = new ArrayList<>();

    public ArrayList<Coin> coinManager = new ArrayList<>();
    public ArrayList <Star> starManager = new ArrayList<>();
    public ArrayList <Mushroom> mushroomManager = new ArrayList<>();

    public ArrayList<Goomba> goombaManager = new ArrayList<>();
    public ArrayList<Pipe> plantManager = new ArrayList<>();



    public int level = 1;
    private int width;

    public int touchx;
    public int touchy;
    public boolean move;
    public boolean fall;
    public boolean jump;
    public boolean dead;
    public boolean moveRight, moveLeft;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        mario = new Mario(context, this);
        background = new Background(context, this);
        background.changeCurrentLevel(level);
        board = new Board(this);
        move = false;
        fall = true;
        dead = false;
        moveRight = false;
        moveLeft = false;
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        board.start();
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(board.lives > 0) {
            switch (event.getAction()) {
            /*
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                marioPoint.set((int) event.getX(), (int) event.getY());
*/
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                    synchronized (this) {
                        touchx = (int) event.getX();
                        touchy = (int) event.getY();
                        move = true;
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    move = false;
                    moveLeft = false;
                    moveRight = false;
                    return true;
            }
        }
        return false;
    }


    public void update() {
        if(board.lives > 0) {
            if (fall && !jump) {
                mario.gravity(this);
            }
            if (jump && !fall) {
                mario.jump(this);
            }

            //  mario.update(marioPoint, blockmanager);
            background.update(mario);
            board.updateWorld(mario.hitbox, background.endOfScreen);
            for (RegularBlock a : blockmanager) {
                a.update();
            }
            for (QuestionBlock a : questionBlocksMangager) {
                a.update();
            }
            for (Coin a : coinManager) {
                a.update();
            }
            for (Mushroom a : mushroomManager) {
                a.update();
            }
            for (Goomba a : goombaManager) {
                a.update();
            }
            for(Pipe a: plantManager){
                a.getPlant().update();
                a.update();
            }
            for(Star a: starManager){
                a.update();
            }
            door.update();
            mario.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(board.lives > 0) {
            synchronized (this) {
                width = getWidth();
                super.draw(canvas);
                background.draw(canvas);
                background.drawGround(canvas, 4, 125);
                board.draw(canvas);
                mario.draw(canvas);
            }
        }else{

            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setTextSize(200);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("GAME OVER" , 450 , 600, p);
        }
    }



    public boolean checkBlockAndGroundCollisions(RectF tempdst) {
        if(!(blockmanager.size() == 0)) {
            for (RegularBlock a : blockmanager) {
                if (tempdst.intersect(a.whereToDraw)) {
                    return true;
                }
            }
        }
        if(!(questionBlocksMangager.size() == 0)) {

            for (QuestionBlock a : questionBlocksMangager) {
                if (tempdst.intersect(a.whereToDraw)) {
                    return true;
                }
            }
        }
        if(!(plantManager.size() == 0)) {
            for (Pipe a : plantManager) {
                if (tempdst.intersect(a.whereToDraw)) {
                    return true;
                }
            }
        }

        if (tempdst.intersect(background.groundcanvasFrame)) {
            return true;
        }
        return false;
    }

    public boolean checkGravityCollision() {


        for (RegularBlock a : blockmanager) {
            if (!mario.isMarioNotTouching(a.whereToDraw)) {
                return false;
            }
        }


        if (mario.isMarioNotTouching(background.groundcanvasFrame)) {
            return true;
        }
        return false;
    }

    public boolean canMarioJump(Mario mario) {
        if (mario.isMarioTouchingBottomVariation(background.groundcanvasFrame)) {
            return true;
        }
        RegularBlock block = mario.findClosestBrick(blockmanager);
        QuestionBlock questionBlock = mario.findClosestQBrick(questionBlocksMangager);
        float q_distance = mario.findDistance(questionBlock.whereToDraw);
        float block_distance = mario.findDistance(block.whereToDraw);
        if( (block_distance <= 60) || (q_distance <= 60)) {
            if (block_distance < q_distance) {
                if (mario.isMarioTouchingBottomVariation(block.whereToDraw)) {
                    return true;
                }
            } else {
                if (mario.isMarioTouchingBottomVariation(questionBlock.whereToDraw)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void nextLevel(int x){
        if(x == 1) {
            background.changeCurrentLevel(++level);
        }else{
            background .changeCurrentLevel(level + 1);
            background .changeCurrentLevel(level);
        }
        board.flag = true;
        board.change_level();
       /// mario.whereToDraw.set(10, 10 , 10 + mario.whereToDraw.width(), 10 + mario.whereToDraw.height());
    }
}