package io.github.rokn.magicapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class MagicItem extends ImageView implements SensorEventListener{

    Point displaySize;
    PointF offset;
    SensorManager sensorManager;
    PointF velocity;
    float maxVelocity;
    Bitmap bitmap;
    float x, y;
    boolean visible;
    boolean dragging;
    float pullThreshold;

    public MagicItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        velocity = new PointF(0.0f, 0.0f);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        offset = new PointF(bitmap.getWidth()/2, bitmap.getHeight()/2);

        x = displaySize.x / 2;
        y = displaySize.y / 2;
        visible = false;
        maxVelocity = 5;
        dragging = false;
        pullThreshold = 45;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            dragging = true;
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float touchX = event.getRawX();
            float touchY = event.getRawY();

            setAbsoluteLocation(touchX, touchY);

            if(x < pullThreshold || x > displaySize.x - pullThreshold || y < pullThreshold || y > displaySize.y - pullThreshold) {
                visible = false;
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            dragging = false;
        }

        return true;
    }

    private void setAbsoluteLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(visible) {
                velocity.x += -event.values[0] / 2;
                velocity.y += event.values[1] / 2;
            }else{
                if(event.values[1] < -4){
                    visible = true;
                    x = displaySize.x/2;
                    y = displaySize.y - offset.y;
                    velocity.y = -maxVelocity;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void update(){
        if(!dragging) {
            constraintVelocity();

            x += velocity.x;
            y += velocity.y;

            constraintPosition();
        }
    }

    private void constraintVelocity(){
        if(velocity.x > maxVelocity){
            velocity.x = maxVelocity;
        }else if(velocity.x < -maxVelocity) {
            velocity.x = -maxVelocity;
        }

        if(velocity.y > maxVelocity) {
            velocity.y = maxVelocity;
        }else if(velocity.y < -maxVelocity) {
            velocity.y = -maxVelocity;
        }
    }

    private void constraintPosition(){
        if(x < offset.x){
            x = offset.x;
            velocity.x = 0;
        }else if(x > displaySize.x - offset.x) {
            x = displaySize.x - offset.x;
            velocity.x = 0;
        }

        if(y < offset.y){
            y = offset.y;
            velocity.y = 0;
        }else if(y > displaySize.y - offset.y) {
            y = displaySize.y - offset.y;
            velocity.y = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(visible) {
            super.draw(canvas);
            canvas.drawBitmap(bitmap, x - offset.x, y - offset.y, null);
        }
    }
}
