package com.example.hangapaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

public class PaintView extends View {
    Path path;
    Paint paint;
    Canvas canvas;
    Deque<Path> paths = new ArrayDeque<>();
    Deque<Path> stack = new ArrayDeque<>();

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
        canvas = new Canvas();
        paint.setColor(0xff000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas cv) {
        for (Path p : paths) {
            cv.drawPath(p, paint);
        }
        cv.drawPath(path, paint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stack.clear();
                path.reset();
                path.moveTo(x, y);

                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);

                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                canvas.drawPath(path, paint);
                paths.add(path);
                path = new Path();

                break;
        }
        return true;
    }
}
