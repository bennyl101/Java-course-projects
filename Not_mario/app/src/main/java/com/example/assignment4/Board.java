package com.example.assignment4;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

class Board implements View.OnTouchListener {
    Level lv;
    public int scorecount = 0;
    public int lives = 3;

    int[][] placement = new int[25][125];
    GamePanel mv;
    public int frameHeight = 16;
    public int frameWidth = 16;
    public int scaleFactor = 3;
    RectF mariodst;
    public boolean flag;

    final static int MAX_TILE_Y = 20;
    final static int MAX_TILE_X = 125;

    private int framelength = frameHeight * scaleFactor;

    private int xScreen, yScreen;

    public Board(GamePanel mv) {
        lv = new Level(mv.level, mv);
        lv.levelBuild(placement);
        lv.loadObjects(placement, mv.getContext());
        mariodst = mv.mario.hitbox;
        this.mv = mv;
        flag = false;

    }

    public void start() {
        Canvas s = mv.getHolder().lockCanvas();
        xScreen = s.getWidth();
        yScreen = s.getHeight();
        System.out.println(s);
        int ground_row = 0;
        int ground_col = 0;
        for (int row = 0; row < MAX_TILE_Y; row++) {
            for (int col = 0; col < MAX_TILE_X; col++) {
                switch (placement[row][col]) {
                    case 1:
                        ground_col++;
                        break;
                    case 2:
                        mv.blockmanager.add(lv.brickArray[row][col]);
                        lv.brickArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        lv.brickArray[row][col].draw(s);
                        break;
                    case 3:
                        mv.questionBlocksMangager.add(lv.questionBlockArray[row][col]);
                        lv.questionBlockArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        lv.questionBlockArray[row][col].draw(s);
                        break;
                    case 4:
                        mv.coinManager.add(lv.coinsArray[row][col]);
                        lv.coinsArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        lv.coinsArray[row][col].draw(s);
                        break;
                    case 5:
                        mv.goombaManager.add(lv.goombasArray[row][col]);
                        lv.goombasArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        lv.goombasArray[row][col].draw(s);
                        break;
                    case 6:
                        mv.door = lv.door;
                        lv.door.whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength * 3, framelength * row + framelength * 3);
                        lv.door.draw(s);
                        break;
                    case 7:
                        mv.plantManager.add(lv.pipesArray[row][col]);
                        lv.pipesArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength * 2, framelength * row + framelength * 2);
                        lv.pipesArray[row][col].draw(s);
                        PirhanaPlant plant = new PirhanaPlant(mv, lv.pipesArray[row][col]);
                        lv.pipesArray[row][col].setPlant(plant);
                        plant.whereToDraw.set(30 + framelength * col, framelength * row, 30 + framelength * col + framelength, framelength * row + framelength * 2);
                        break;
                }
            }
            if (placement[row][0] == 1) {
                ground_row++;
            }
        }
        mv.background.drawGround(s, ground_row, ground_col);
        mv.getHolder().unlockCanvasAndPost(s);
    }

    private void loadNext() {
        ArrayList<RegularBlock> blockmanager = new ArrayList<>();
        ArrayList<QuestionBlock> questionBlocksMangager = new ArrayList<>();

        ArrayList<Coin> coinManager = new ArrayList<>();
        ArrayList<Star> starManager = new ArrayList<>();
        ArrayList<Mushroom> mushroomManager = new ArrayList<>();

        ArrayList<Goomba> goombaManager = new ArrayList<>();
        ArrayList<Pipe> plantManager = new ArrayList<>();


        for (int row = 0; row < MAX_TILE_Y; row++) {
            for (int col = 0; col < MAX_TILE_X; col++) {
                switch (placement[row][col]) {
                    case 1:
                        break;
                    case 2:
                        blockmanager.add(lv.brickArray[row][col]);
                        lv.brickArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        break;
                    case 3:
                        questionBlocksMangager.add(lv.questionBlockArray[row][col]);
                        lv.questionBlockArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        break;
                    case 4:
                        coinManager.add(lv.coinsArray[row][col]);
                        lv.coinsArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        break;
                    case 5:
                        goombaManager.add(lv.goombasArray[row][col]);
                        lv.goombasArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength, framelength * row + framelength);
                        break;
                    case 6:
                        mv.door = lv.door;
                        lv.door.whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength * 3, framelength * row + framelength * 3);
                        break;
                    case 7:
                        plantManager.add(lv.pipesArray[row][col]);
                        lv.pipesArray[row][col].whereToDraw.set(framelength * col, framelength * row, framelength * col + framelength * 2, framelength * row + framelength * 2);
                        PirhanaPlant plant = new PirhanaPlant(mv, lv.pipesArray[row][col]);
                        lv.pipesArray[row][col].setPlant(plant);
                        plant.whereToDraw.set(30 + (framelength * col), framelength * row, 30 + (framelength * col) + framelength, framelength * row + framelength * 2);
                        break;
                }
            }
        }

        mv.questionBlocksMangager = questionBlocksMangager;
        mv.blockmanager = blockmanager;
        mv.coinManager = coinManager;
        mv.mushroomManager = mushroomManager;
        mv.goombaManager = goombaManager;
        mv.starManager = starManager;
        mv.plantManager = plantManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void draw(Canvas c) {
        Paint score = new Paint();
        Paint livesPaint = new Paint();
        score.setColor(Color.WHITE);
        score.setTextSize(70);
        score.setTypeface(Typeface.DEFAULT_BOLD);
        score.setAntiAlias(true);
        c.drawText("Score: " + scorecount, 20, 60, score);
        livesPaint.setColor(Color.WHITE);
        livesPaint.setTextSize(50);
        livesPaint.setTypeface(Typeface.DEFAULT_BOLD);
        livesPaint.setAntiAlias(true);
        c.drawText("Lives: " + lives, 450, 60, livesPaint);
        int ground_row = 0;
        int ground_col = 0;
        for (int row = 0; row < MAX_TILE_Y; row++) {
            for (int col = 0; col < MAX_TILE_X; col++) {
                switch (placement[row][col]) {
                    case 1:
                        ground_col++;
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
            if (placement[row][0] == 1) {
                ground_row++;
            }
        }
        for (RegularBlock a : mv.blockmanager) {
            a.draw(c);
        }
        for (QuestionBlock a : mv.questionBlocksMangager) {
            a.draw(c);
        }
        for (Coin a : mv.coinManager) {
            a.draw(c);
        }
        for (Mushroom a : mv.mushroomManager) {
            a.draw(c);
        }
        for (Goomba a : mv.goombaManager) {
            a.draw(c);
        }
        for (Pipe a : mv.plantManager) {
            a.getPlant().draw(c);
            a.draw(c);
        }
        for (Star a : mv.starManager) {
            a.draw(c);
        }
        mv.door.draw(c);

        mv.background.drawGround(c, ground_row, ground_col);

    }

    public void updateWorld(RectF mariodst, boolean endOfScreen) {
        synchronized (this) {

        }
        if (mv.move && !mv.dead) {
            if (mv.touchx > (xScreen / 4) * 3) {
                mv.moveRight = true;
                mv.moveLeft = false;
                mv.mario.goRight(mv);
            }
            if (mv.touchx < (xScreen / 4)) {
                mv.moveRight = false;
                mv.moveLeft = true;
                mv.mario.goLeft(mv);
            }
            if ((mv.touchx > (xScreen / 4)) && (mv.touchx < (xScreen / 4) * 3)) {
                if (mv.canMarioJump(mv.mario)) {
                    mv.jump = true;
                    mv.fall = false;
                    mv.mario.jump(mv);
                }
            }
        }


        if (!endOfScreen) {
            if (mariodst.right >= 950) {
                moveObjects(mariodst, "left");
            }
            if (mariodst.left <= 400) {
                moveObjects(mariodst, "right");
            }
        }

    }

    public void moveObjects(RectF mariodst, String direction) {
        switch (direction) {
            case "left":
                for (RegularBlock a : mv.blockmanager) {
                    a.moveLeft();
                }
                for (QuestionBlock a : mv.questionBlocksMangager) {
                    a.moveLeft();
                }

                for (Coin a : mv.coinManager) {
                    a.moveLeft();
                }
                for (Star a : mv.starManager) {
                    a.moveLeft();
                }
                for (Mushroom a : mv.mushroomManager) {
                    a.moveLeft();
                }
                for (Goomba a : mv.goombaManager) {
                    a.moveLeft();
                }
                if (mv.door != null) {
                    mv.door.moveLeft();
                }
                for (Pipe a : mv.plantManager) {
                    a.getPlant().moveLeft();
                    a.moveLeft();
                }

                break;
            case "right":
                for (RegularBlock a : mv.blockmanager) {
                    a.moveRight();
                }

                for (QuestionBlock a : mv.questionBlocksMangager) {
                    a.moveRight();
                }
                for (Coin a : mv.coinManager) {
                    a.moveRight();
                }
                for (Star a : mv.starManager) {
                    a.moveRight();
                }
                for (Mushroom a : mv.mushroomManager) {
                    a.moveRight();
                }
                for (Goomba a : mv.goombaManager) {
                    a.moveRight();
                }
                if (mv.door != null) {
                    mv.door.moveRight();
                }
                for (Pipe a : mv.plantManager) {
                    a.getPlant().moveRight();
                    a.moveRight();
                }

                break;
        }
    }

    public void change_level() {
        flag = false;
        lv = new Level(mv.level, mv);
        placement = new int[25][125];
        lv.levelBuild(placement);
        lv.loadObjects(placement, mv.getContext());
        loadNext();
    }

    public void clearObjects() {
        for (RegularBlock a : mv.blockmanager) {
            a.whereToDraw.set(0, 0, 0, 0);
        }
        for (QuestionBlock a : mv.questionBlocksMangager) {
            a.whereToDraw.set(0, 0, 0, 0);
        }
        for (Coin a : mv.coinManager) {
            a.whereToDraw.set(0, 0, 0, 0);
        }
        for (Mushroom a : mv.mushroomManager) {
            a.whereToDraw.set(0, 0, 0, 0);
        }
        for (Goomba a : mv.goombaManager) {
            a.whereToDraw.set(0, 0, 0, 0);
        }
        for (Pipe a : mv.plantManager) {
            a.getPlant().whereToDraw.set(0, 0, 0, 0);
            a.whereToDraw.set(0, 0, 0, 0);
        }
    }
}