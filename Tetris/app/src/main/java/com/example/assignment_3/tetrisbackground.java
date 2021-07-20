package com.example.assignment_3;

import android.graphics.*;

import java.util.*;
import java.util.Random;

public class tetrisbackground {
    private static final int backgroundh = 20;
    private static final int backgroundw = 10;
    private static int[][] tetrisbackground = new int[backgroundh][backgroundw];
    private ArrayList<Piece> pieceQueue = new ArrayList<>();
    public int points = 0;

    public int getHeight() {
        return this.backgroundh;
    }

    public int getWidth() {
        return this.backgroundw;
    }

    public tetrisbackground() {
        pieceQueue.add(new Piece(new Random().nextInt(7) + 1));
        pieceQueue.add(new Piece(new Random().nextInt(7) + 1));
        startPiece(pieceQueue.get(0));
        startPiece(pieceQueue.get(1));
        resetBoard();
    }

    public void resetBoard() {
        for (int x = 0; x < backgroundw; x++) {
            for (int y = 0; y < backgroundh; y++) {
                tetrisbackground[y][x] = -1;
            }
        }
    }

    public void placePiece(Piece piece) {
        tetrisbackground[piece.y1][piece.x1] = piece.colorCode;
        tetrisbackground[piece.y2][piece.x2] = piece.colorCode;
        tetrisbackground[piece.y3][piece.x3] = piece.colorCode;
        tetrisbackground[piece.y4][piece.x4] = piece.colorCode;
    }

    public void startPiece(Piece piece) {
        piece.x1 = piece.x1 + 4;
        piece.x2 = piece.x2 + 4;
        piece.x3 = piece.x3 + 4;
        piece.x4 = piece.x4 + 4;
    }

    public Piece getCurrentPiece() {
        return pieceQueue.get((pieceQueue.size()) - 2);
    }

    public Piece getNextPiece() {
        return pieceQueue.get((pieceQueue.size()) - 1);
    }

    public ArrayList<Piece> getPieceQueue() {
        return pieceQueue;
    }

    public int colorCode(int x, int y) {
        int square = tetrisbackground[x][y];
        switch (square) {
            case -1:
                return Color.parseColor("#FFFF00");// Yellow
            case 1:
                return Color.parseColor("#FF0000");// Red
            case 2:
                return Color.parseColor("#BEBEBE");// Gray
            case 3:
                return Color.parseColor("#ffbf00");// Orange
            case 4:
                return Color.parseColor("#00FFFF");// light Blue
            case 5:
                return Color.parseColor("#0000FF");// dark blue
            case 6:
                return Color.parseColor("#FF00FF");// violet
            case 7:
                return Color.parseColor("#00FF00");// Green
        }
        return -1;
    }

    private void clearLine(int y) {
        for (int x = 0; x < backgroundw; x++) {
            tetrisbackground[y][x] = -1;
            points++;
        }
    }

    public int checkFullLines() {
        int deletedRows = 0;
        for (int y = 0; y < backgroundh; y++) {
            for (int x = 0; x < backgroundw; x++) {
                if (tetrisbackground[y][x] == -1) {
                    break;
                }
                if (x == 9) {
                    deletedRows++;
                }
            }
        }
        return deletedRows;
    }

    public void clearAllLines(int num_of_rows) {
        int index = -1;
        int row_found = 0;
        for (int k = 0; k < num_of_rows; k++) {
            for (int y = 0; y < backgroundh; y++) {
                for (int x = 0; x < backgroundw; x++) {
                    if (tetrisbackground[y][x] == -1) {
                        break;
                    }
                    if (x == 9) {
                        index = y;
                        row_found = 1;
                    }
                }
                if (row_found == 1) {
                    break;
                }
            }
            if (row_found == 1) {
                clearLine(index);
                for (int j = 0; j < index; j++) {
                    for (int l = 0; l < backgroundw; l++) {
                        tetrisbackground[index - j][l] = tetrisbackground[index - j - 1][l];
                    }
                }
                index = -1;
                row_found = 0;
            }
        }
    }

    private boolean canPieceMove(Piece current_piece, int newX, int newY) {
        Piece tmp_piece = new Piece(current_piece);
        tmp_piece.move(newX, newY);
        int[][] copy_tetrisbackground = new int[backgroundh][backgroundw];
        for (int x = 0; x < backgroundh; x++) {
            for (int y = 0; y < backgroundw; y++) {
                copy_tetrisbackground[x][y] = tetrisbackground[x][y];
            }
        }

        ArrayList<Point> coordinate_list = new ArrayList<>();
        Point tmp1 = new Point(tmp_piece.x1, tmp_piece.y1);
        Point tmp2 = new Point(tmp_piece.x2, tmp_piece.y2);
        Point tmp3 = new Point(tmp_piece.x3, tmp_piece.y3);
        Point tmp4 = new Point(tmp_piece.x4, tmp_piece.y4);

        coordinate_list.add(tmp1);
        coordinate_list.add(tmp2);
        coordinate_list.add(tmp3);
        coordinate_list.add(tmp4);
        int flag = 0;
        if(copy_tetrisbackground[current_piece.y1][current_piece.x1] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y2][current_piece.x2] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y3][current_piece.x3] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y4][current_piece.x4] == current_piece.colorCode){
            flag++;
        }
        if(flag == 4) {
            copy_tetrisbackground[current_piece.y1][current_piece.x1] = -1;
            copy_tetrisbackground[current_piece.y2][current_piece.x2] = -1;
            copy_tetrisbackground[current_piece.y3][current_piece.x3] = -1;
            copy_tetrisbackground[current_piece.y4][current_piece.x4] = -1;
        }
        int valid_squares = 0;
        for (Point a : coordinate_list) {
            if (((a.x >= 0) && (a.x <= (backgroundw - 1))) && a.y <= (backgroundh - 1) && copy_tetrisbackground[a.y][a.x] == -1)
                valid_squares++;
        }

        if (valid_squares == 4) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canPieceMoveLeft(Piece current_piece) {
        if (canPieceMove(current_piece, -1, 0)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canPieceMoveDown(Piece current_piece) {
        if (canPieceMove(current_piece, 0, 1)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canPieceMoveRight(Piece current_piece) {
        if (canPieceMove(current_piece, 1, 0)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canPlacePiece(Piece current_piece){
        int flag= 0;
        if(tetrisbackground[current_piece.y1][current_piece.x1] != -1){
            flag++;
        }
        if(tetrisbackground[current_piece.y2][current_piece.x2] != -1){
            flag++;
        }
        if(tetrisbackground[current_piece.y3][current_piece.x3] != -1){
            flag++;
        }
        if(tetrisbackground[current_piece.y4][current_piece.x4] != -1){
            flag++;
        }
        if(flag > 0){
            return false;
        }else{
            return true;
        }
    }


    private boolean canPieceRotate(Piece current_piece) {
        /*
        System.out.println("X1: " + current_piece.x1 + "  Y1: " + current_piece.y1);
        System.out.println("X2: " + current_piece.x2 + "  Y2: " + current_piece.y2);
        System.out.println("X3: " + current_piece.x3 + "  Y3: " + current_piece.y3);
        System.out.println("X4: " + current_piece.x4 + "  Y4: " + current_piece.y4);
        */
        Piece tmp_piece = new Piece(current_piece);
        tmp_piece.rotate();
        int[][] copy_tetrisbackground = new int[backgroundh][backgroundw];
        for (int x = 0; x < backgroundh; x++) {
            for (int y = 0; y < backgroundw; y++) {
                copy_tetrisbackground[x][y] = tetrisbackground[x][y];
            }
        }

        ArrayList<Point> coordinate_list = new ArrayList<>();
        Point tmp1 = new Point(tmp_piece.x1, tmp_piece.y1);
        Point tmp2 = new Point(tmp_piece.x2, tmp_piece.y2);
        Point tmp3 = new Point(tmp_piece.x3, tmp_piece.y3);
        Point tmp4 = new Point(tmp_piece.x4, tmp_piece.y4);
/*
        System.out.println("X1: " + tmp_piece.x1 + "  Y1: " + tmp_piece.y1);
        System.out.println("X2: " + tmp_piece.x2 + "  Y2: " + tmp_piece.y2);
        System.out.println("X3: " + tmp_piece.x3 + "  Y3: " + tmp_piece.y3);
        System.out.println("X4: " + tmp_piece.x4 + "  Y4: " + tmp_piece.y4);
*/
        coordinate_list.add(tmp1);
        coordinate_list.add(tmp2);
        coordinate_list.add(tmp3);
        coordinate_list.add(tmp4);
        int flag = 0;
        if(copy_tetrisbackground[current_piece.y1][current_piece.x1] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y2][current_piece.x2] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y3][current_piece.x3] == current_piece.colorCode){
            flag++;
        }
        if(copy_tetrisbackground[current_piece.y4][current_piece.x4] == current_piece.colorCode){
            flag++;
        }
        if(flag == 4) {
            copy_tetrisbackground[current_piece.y1][current_piece.x1] = -1;
            copy_tetrisbackground[current_piece.y2][current_piece.x2] = -1;
            copy_tetrisbackground[current_piece.y3][current_piece.x3] = -1;
            copy_tetrisbackground[current_piece.y4][current_piece.x4] = -1;
        }

        int valid_squares = 0;
        for (Point a : coordinate_list) {
            if (a.x >= 0 && a.x <= backgroundw - 1 && a.y <= backgroundh - 1 && copy_tetrisbackground[a.y][a.x] == -1)
                valid_squares++;
        }
        if (valid_squares == 4) {
            return true;
        } else {
            return false;
        }
    }

    public void moveRight(Piece current_piece) {
        if (canPieceMoveRight(current_piece)) {
            deletePiece(current_piece);
            current_piece.move(1, 0);
            placePiece(current_piece);
        }

    }

    public void moveDown(Piece current_piece) {
        if (canPieceMoveDown(current_piece)) {
            deletePiece(current_piece);
            current_piece.move(0, 1);
            placePiece(current_piece);
        }

    }

    public void SpeedUp(Piece current_piece) {
        if (getPoints() > 50) {

        }
    }


    public void moveLeft(Piece current_piece) {
        if (canPieceMoveLeft(current_piece)) {
            deletePiece(current_piece);
            current_piece.move(-1, 0);
            placePiece(current_piece);
        }
    }

    public void rotate(Piece current_piece) {
        if (canPieceRotate(current_piece)) {
            deletePiece(current_piece);
            current_piece.rotate();
            placePiece(current_piece);
        }
    }


    public void fastFall(Piece current_piece) {
        deletePiece(current_piece);
        while (canPieceMoveDown(current_piece)) {
            moveDown(current_piece);
        }
        placePiece(current_piece);

    }

    private void deletePiece(Piece current_piece) {
        tetrisbackground[current_piece.y1][current_piece.x1] = -1;
        tetrisbackground[current_piece.y2][current_piece.x2] = -1;
        tetrisbackground[current_piece.y3][current_piece.x3] = -1;
        tetrisbackground[current_piece.y4][current_piece.x4] = -1;
    }

    public boolean checkGameOver(Piece new_piece) {
        if (!canPieceMoveDown(new_piece) && new_piece.minAndMaxXCoordinates()[1] <= 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getPoints() {
        return points;
    }

    public int[][] getTetrisbackground() {
        return tetrisbackground.clone();
    }
}

