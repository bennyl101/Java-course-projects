package com.example.assignment4;

import android.graphics.Canvas;
import android.graphics.RectF;

interface GameObject {
    public void draw(Canvas canvas);

    public void update();
}
