package com.example.assignment_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

public class NecksedUp extends View {
    private tetrisbackground tetrisboard;
    private ArrayList<Piece> pieceQueue;


    private Bitmap square = BitmapFactory.decodeResource(getResources(), R.drawable.square_piece);
    private Bitmap resized_square = Bitmap.createScaledBitmap(square, (int) (square.getWidth()*.3), (int) (square.getHeight()*.3) ,true);
    private Bitmap t = BitmapFactory.decodeResource(getResources(), R.drawable.t_piece);
    private Bitmap resized_t = Bitmap.createScaledBitmap(t, (int) (t.getWidth()*.3), (int) (t.getHeight()*.3) ,true);
    private Bitmap z = BitmapFactory.decodeResource(getResources(), R.drawable.z_piece);
    private Bitmap resized_z = Bitmap.createScaledBitmap(z, (int) (z.getWidth()*.3), (int) (z.getHeight()*.3) ,true);
    private Bitmap reverseZ = BitmapFactory.decodeResource(getResources(), R.drawable.reverse_z_piece);
    private Bitmap resized_reversez = Bitmap.createScaledBitmap(reverseZ, (int) (reverseZ.getWidth()*.3), (int) (reverseZ.getHeight()*.3) ,true);
    private Bitmap l = BitmapFactory.decodeResource(getResources(), R.drawable.l_piece);
    private Bitmap resized_l = Bitmap.createScaledBitmap(l, (int) (l.getWidth()*.3), (int) (l.getHeight()*.3) ,true);
    private Bitmap reversel = BitmapFactory.decodeResource(getResources(), R.drawable.reverse_l_piece);
    private Bitmap resized_reversel = Bitmap.createScaledBitmap(reversel, (int) (reversel.getWidth()*.3), (int) (reversel.getHeight()*.3) ,true);
    private Bitmap stick = BitmapFactory.decodeResource(getResources(), R.drawable.stick_piece);
    private Bitmap resized_stick = Bitmap.createScaledBitmap(stick, (int) (stick.getWidth()*.3), (int) (stick.getHeight()*.3) ,true);



    public NecksedUp(Context context, tetrisbackground tetrisboard) {
        super(context);
        this.tetrisboard = tetrisboard;
        pieceQueue = tetrisboard.getPieceQueue();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        if(pieceQueue.size() > 0){
            switch(tetrisboard.getNextPiece().colorCode){
                case 1://Square
                    canvas.drawBitmap(resized_square, 0, 0, p);
                    break;
                case 2://Stick
                    canvas.drawBitmap(resized_stick, 0, 0, p);
                    break;
                case 3://ReverseL
                    canvas.drawBitmap(resized_reversel, 0, 0, p);
                    break;
                case 4://L
                    canvas.drawBitmap(resized_l, 0, 0, p);
                    break;
                case 5://Z
                    canvas.drawBitmap(resized_z, 0, 0, p);
                    break;
                case 6://Reverse Z
                    canvas.drawBitmap(resized_reversez, 0, 0, p);
                    break;
                case 7:// T piece
                    canvas.drawBitmap(resized_t, 0, 0, p);
                    break;
            }
        }
    }
}
