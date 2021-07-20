package com.example.assignment4;

import android.content.Context;

public class Level {
    private int level;
    final static int MAX_TILE_Y = 25;
    final static int MAX_TILE_X = 125;
    GamePanel mv;

    public RegularBlock[][] brickArray = new RegularBlock[MAX_TILE_Y][MAX_TILE_X];
    public QuestionBlock[][] questionBlockArray = new QuestionBlock[MAX_TILE_Y][MAX_TILE_X];
    public Coin[][] coinsArray = new Coin[MAX_TILE_Y][MAX_TILE_X];
    public Star[][] starArray = new Star[MAX_TILE_Y][MAX_TILE_X];
    public Mushroom[][] mushroomArray = new Mushroom[MAX_TILE_Y][MAX_TILE_X];
    public Goomba[][] goombasArray = new Goomba[MAX_TILE_Y][MAX_TILE_X];
    public Pipe[][] pipesArray = new Pipe[MAX_TILE_Y][MAX_TILE_X];
    public Door door;

    public Level(int level, GamePanel mv) {
        this.level = level;
        this.mv = mv;
    }

    public void levelBuild(int[][] placement) {
        // 1=Ground 2=Breakable 3=Question 4= COIN
        // 5=BAD MUSHROOM 6=Mushroom
        switch (level) {
            case 2:
                for (int row = 0; row < MAX_TILE_Y; row++) {
                    for (int col = 0; col < MAX_TILE_X; col++) {
                        placement[24][col] = 1;
                        placement[23][col] = 1;
                        placement[22][col] = 1;
                        placement[21][col] = 1;
                        if (col >= 12 && col <= 17) {
                            placement[16][col] = 3;
                        }
                        if (col >= 6 && col <= 25)
                            placement[8][col] = 2;

                        if (col >= 59 && col <= 59) {
                            placement[19][col] = 7;
                        }
                        if (col >= 30 && col <= 41) {
                            placement[14][col] = 2;
                        }
                        if (col >= 40 && col <= 40) {
                            placement[19][col] = 5;
                        }
                        if (col >= 80 && col <= 81) {
                            placement[6][col] = 3;
                        }
                        if (col >= 85 && col <= 88) {
                            placement[19][col] = 4;
                        }
                        if (col >= 101 && col <= 101)
                            placement[18][col] = 6;
                    }
                }

                break;
            case 3:
                for (int row = 0; row < MAX_TILE_Y; row++) {
                    for (int col = 0; col < MAX_TILE_X; col++) {
                        placement[24][col] = 1;
                        placement[23][col] = 1;
                        placement[22][col] = 1;
                        placement[21][col] = 1;
                        if (col >= 12 && col <= 17) {
                            placement[16][col] = 3;
                        }
                        if (col >= 6 && col <= 25)
                            placement[8][col] = 2;
                        if (col >= 101 && col <= 101)
                            placement[18][col] = 6;
                        if (col >= 60 && col <= 60) {
                            placement[15][col] = 4;
                        }
                        if (col >= 58 && col <= 62) {
                            placement[14][col] = 2;
                        }
                        if (col >= 40 && col <= 40) {
                            placement[19][col] = 5;
                        }
                        if (col >= 50 && col <= 50) {
                            placement[19][col] = 7;
                        }
                        if (col >= 20 && col <= 20) {
                            placement[19][col] = 7;
                        }
                        if (col >= 69 && col <= 84) {
                            placement[row][col] = 4;
                        }

                    }
                }

                break;
            case 1:
                for (int row = 0; row < MAX_TILE_Y; row++) {
                    for (int col = 0; col < MAX_TILE_X; col++) {
                        placement[24][col] = 1;
                        placement[23][col] = 1;
                        placement[22][col] = 1;
                        placement[21][col] = 1;
                        if (col >= 6 && col <= 9)
                            placement[16][col] = 2;
                        if (col >= 7 && col <= 7) {
                            placement[16][col] = 3;
                        }

                        if (col >= 12 && col <= 14) {
                            placement[16][col] = 3;
                        }

                        if (col >= 15 && col <= 18) {
                            placement[16][col] = 3;
                        }
                        if (col >= 9 && col <= 9)
                            placement[15][col] = 5;
                        if (col >= 17 && col <= 25) {
                            placement[10][col] = 2;
                        }

                        if (col >= 60 && col <= 60) {
                            placement[15][col] = 7;
                        }
                        if (col >= 58 && col <= 62) {
                            placement[17][col] = 2;
                        }
                        if (col >= 40 && col <= 40) {
                            placement[19][col] = 5;
                        }
                        if (col >= 70 && col <= 70) {
                            placement[19][col] = 7;
                        }
                        if (col >= 101 && col <= 101)
                            placement[18][col] = 6;
                    }
                }
                break;
        }
    }


    public void loadObjects(int[][] placement, Context context) {
        for (int row = 0; row < MAX_TILE_Y; row++) {
            for (int col = 0; col < MAX_TILE_X; col++) {
                if (placement[row][col] == 2) {
                    brickArray[row][col] = new RegularBlock(mv);
                }
                if (placement[row][col] == 3) {
                    questionBlockArray[row][col] = new QuestionBlock(mv);
                }
                if (placement[row][col] == 4) {
                    coinsArray[row][col] = new Coin(mv);
                }
                if (placement[row][col] == 5) {
                    goombasArray[row][col] = new Goomba(mv);
                }
                if (placement[row][col] == 6) {
                    door = new Door(mv);
                }
                if (placement[row][col] == 7) {
                    pipesArray[row][col] = new Pipe(mv);
                }
            }
        }
    }

}