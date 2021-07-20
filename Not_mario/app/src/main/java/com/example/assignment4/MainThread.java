package com.example.assignment4;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    //tryna commit out here
    private GamePanel gamepanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamepanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamepanel = gamepanel;
    }

    @Override
    public void run() {
        long startTime = 0;
        long timeMillis = 100 / MAX_FPS;
        long waitTime;
        long FrameCount = 0;
        long totalTime = 0;
        long targetTime = 1000 / MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamepanel.update();
                    this.gamepanel.draw(canvas);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime()) - startTime / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            FrameCount++;
            if (FrameCount == MAX_FPS) {
                averageFPS = 1000 / ((totalTime / FrameCount) / 1000000);
                FrameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }
}
