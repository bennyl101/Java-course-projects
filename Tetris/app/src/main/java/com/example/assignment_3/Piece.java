package com.example.assignment_3;

public class Piece {
    public int colorCode;
    public int x1, y1;
    public int x2, y2;
    public int x3, y3;
    public int x4, y4;
    public Piece piece;
    
    public Piece(Piece piece){
        this.x1 = piece.x1;
        this.x2 = piece.x2;
        this.x3 = piece.x3;
        this.x4 = piece.x4;

        this.y1 = piece.y1;
        this.y2 = piece.y2;
        this.y3 = piece.y3;
        this.y4 = piece.y4;

        this.piece = piece;

    }

    public Piece(int x) {
        switch (x) {
            case 1: //Square
                x1 = 0;
                y1 = 0;

                x2 = 0;
                y2 = 1;

                x3 = 1;
                y3 = 0;

                x4 = 1;
                y4 = 1;
                colorCode = 1;
                break;
            case 2: // Stick piece
                x1 = 0;
                y1 = 0;

                x2 = 0;
                y2 = 1;

                x3 = 0;
                y3 = 2;

                x4 = 0;
                y4 = 3;
                colorCode = 2;
                break;
            case 3: // Reverse L piece
                x1 = 0;
                y1 = 0;

                x2 = 1;
                y2 = 0;

                x3 = 1;
                y3 = 1;

                x4 = 1;
                y4 = 2;
                colorCode = 3;
                break;
            case 4:// L piece
                x1 = 1;
                y1 = 0;

                x2 = 0;
                y2 = 0;

                x3 = 0;
                y3 = 1;

                x4 = 0;
                y4 = 2;
                colorCode = 4;
                break;
            case 5:// Z piece
                x1 = 0;
                y1 = 0;

                x2 = 1;
                y2 = 0;

                x3 = 1;
                y3 = 1;

                x4 = 2;
                y4 = 1;
                colorCode = 5;
                break;
            case 6://Reverse Z piece
                x1 = 0;
                y1 = 1;

                x2 = 1;
                y2 = 1;

                x3 = 1;
                y3 = 0;

                x4 = 2;
                y4 = 0;
                colorCode = 6;
                break;
            case 7://T piece
                x1 = 0;
                y1 = 0;

                x2 = 1;
                y2 = 0;

                x3 = 2;
                y3 = 0;

                x4 = 1;
                y4 = 1;
                colorCode = 7;
                break;
        }
    }


    public void move(int x, int y) {
        x1 = x1 + x;
        x2 = x2 + x;
        x3 = x3 + x;
        x4 = x4 + x;

        y1 = y1 + y;
        y2 = y2 + y;
        y3 = y3 + y;
        y4 = y4 + y;
    }

    public void rotate() {//rotate 90 degrees clockwise around x1, y1 square
        int x2_temp, x3_temp, x4_temp, y2_temp, y3_temp, y4_temp;
        System.out.println("********************");
        System.out.println("X1: " + x1 + "  Y1: " + y1);
        System.out.println("X2: " + x2 + "  Y2: " + y2);
        System.out.println("X3: " + x3 + "  Y3: " + y3);
        System.out.println("X4: " + x4 + "  Y4: " + y4);

        x2_temp = turnX(y2);
        x3_temp = turnX(y3);
        x4_temp = turnX(y4);

        y2_temp = turnY(x2);
        y3_temp = turnY(x3);
        y4_temp = turnY(x4);

        x2 = x2_temp;
        x3 = x3_temp;
        x4 = x4_temp;

        y2 = y2_temp;
        y3 = y3_temp;
        y4 = y4_temp;

        System.out.println("X1: " + x1 + "  Y1: " + y1);
        System.out.println("X2: " + x2 + "  Y2: " + y2);
        System.out.println("X3: " + x3 + "  Y3: " + y3);
        System.out.println("X4: " + x4 + "  Y4: " + y4);
        System.out.println("********************");

    }

    private int turnX(int y) {
        return x1 + y - y1;
    }

    private int turnY(int x) {
        return y1 - x + x1;
    }

    public int[] minAndMaxXCoordinates(){
        int[] x = new int[2];
        x[0] = Math.min(x1, Math.min(x2, Math.min(x3, x4)));
        x[1] = Math.max(x1, Math.max(x2, Math.max(x3, x4)));
        return x;
    }

    public int[] bottomYCoordinates(){
        int check = Math.max(y1,Math.max(y2, Math.max(y3, y4)));
        int[] y = new int[4];
        if( y1 == check){
            y[0] = y1;
        }
        if( y2 == check){
            y[1] = y2;
        }
        if( y3 == check){
            y[2] = y3;
        }
        if (y4 == check){
            y[3] = y4;
        }
        return y;
    }

    public int[] leftXCoordinate(){
        int check = Math.min(x1, Math.min(x2, Math.min(x3, x4)));;
        int[] x = new int[4];
        if(  x1 == check){
             x[0] =  x1;
        }
        if(  x2 == check){
             x[1] =  x2;
        }
        if(  x3 == check){
             x[2] =  x3;
        }
        if ( x4 == check){
             x[3] =  x4;
        }
        return x;
    }
    
    public int[] rightXCoordinate(){
        int check = Math.max(x1, Math.max(x2, Math.max(x3, x4)));;
        int[] x = new int[4];
        if(  x1 == check){
             x[0] =  x1;
        }
        if(  x2 == check){
             x[1] =  x2;
        }
        if(  x3 == check){
             x[2] =  x3;
        }
        if ( x4 == check){
             x[3] =  x4;
        }
        return x;
    }
}
