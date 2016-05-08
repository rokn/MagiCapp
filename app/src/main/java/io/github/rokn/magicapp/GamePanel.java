package io.github.rokn.magicapp;

/**
 * Created by amind on 3/23/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
        import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
import android.view.WindowManager;

public class GamePanel
        extends SurfaceView
        implements SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();

    Point displaySize;
    private MainThread thread;
    MagicItem item;
    Bitmap wallPaper;

    public GamePanel(Context context, AttributeSet attrs) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        item = new MagicItem(context, attrs);
        item.setImageResource(R.drawable.coin);
        item.setX(0.0f);
        item.setY(0.0f);
        wallPaper = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper0);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

        Log.d(TAG, "Thread was shut down cleanly");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        item.onTouchEvent(event);
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Rect dst = new Rect();
        dst.set(0,0,displaySize.x,displaySize.y);
        canvas.drawBitmap(wallPaper,null, dst, null);
        item.draw(canvas);
    }

    public void update() {
        item.update();
    }

    public void stop(){
        if(thread != null){
            thread.setRunning(false);
        }
    }
}
