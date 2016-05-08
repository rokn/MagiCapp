package io.github.rokn.magicapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author Niko
 * View Container to place the view wanted to be dragged
 */
public class DragDropView extends FrameLayout {

    /**
     * Default Constructor
     * @param context
     */
    public DragDropView(Context context) {
        super(context);
    }

    /**
     * Default Constructor
     * @param context
     * @param attrs
     */
    public DragDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Default Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DragDropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /** Adding draggable object to the dragView
     * @param draggableObject - object to be dragged
     * @param - x horizontal position of the view
     * @param - y vertical position of the view
     * @param - width width of the view
     * @param - height height of the view
     */
    public void AddDraggableView(View draggableObject, int x, int y, int width, int height) {
        LayoutParams lpDraggableView = new LayoutParams(width, height);
        lpDraggableView.gravity = Gravity.TOP;
        lpDraggableView.leftMargin = x;
        lpDraggableView.topMargin = y;
        if(draggableObject instanceof ImageView) {
            ImageView ivDrag = (ImageView) draggableObject;
            ivDrag.setLayoutParams(lpDraggableView);
            ivDrag.setOnTouchListener(OnTouchToDrag);
            this.addView(ivDrag);
        }
    }

    /**
     * Draggable object on touch listener
     * Handle the movement of the object when dragged and dropped
     */
    private View.OnTouchListener OnTouchToDrag = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            FrameLayout.LayoutParams dragParam = (LayoutParams) v.getLayoutParams();
            switch(event.getAction())
            {
                case MotionEvent.ACTION_MOVE:
                {
                    dragParam.topMargin = (int)event.getRawY() - (v.getHeight());
                    dragParam.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
                    v.setLayoutParams(dragParam);
                    break;
                }
                case MotionEvent.ACTION_UP:
                {
                    //	dragParam.height = 150;
                    //	dragParam.width = 150;
                    dragParam.height = v.getHeight();
                    dragParam.width = v.getWidth();
                    dragParam.topMargin = (int)event.getRawY() - (v.getHeight());
                    dragParam.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
                    v.setLayoutParams(dragParam);
                    break;
                }
                case MotionEvent.ACTION_DOWN:
                {
                    //	dragParam.height = 100;
                    //	dragParam.width = 100;
                    dragParam.height = v.getHeight();
                    dragParam.width = v.getWidth();
                    v.setLayoutParams(dragParam);
                    break;
                }
            }
            return true;
        }

    };

}
