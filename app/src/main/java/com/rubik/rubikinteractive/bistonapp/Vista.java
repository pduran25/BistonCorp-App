package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.nio.file.Paths;


/**
 * Created by RubikInteractive on 4/30/19.
 */

class Vista extends View {
    float x=50;
    float y=50;
    float yact = 0;
    String accion = "accion";
    Canvas canvas;
    Paint paint, canvaspaint;
    public static String TAG = "BSTC";
    public Bitmap canvasBitmap;

    Path path;

    public Vista(Context context, float x, float y){
        super(context);
        this.x = x;
        this.y = y;
        this.yact = y;

        setupDrawing();
    }




    private void setupDrawing() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        canvaspaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(canvasBitmap);

    }

    public void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap,0,0, canvaspaint);
        canvas.drawPath(path,paint);

    }

    public void limpiar_firma(){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent e){
        x = e.getX();
        y = e.getY();
        this.getParent().requestDisallowInterceptTouchEvent(true);

        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x,y);
                canvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


}
