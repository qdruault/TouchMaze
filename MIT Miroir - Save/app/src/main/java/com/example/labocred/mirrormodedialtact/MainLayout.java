package com.example.labocred.mirrormodedialtact;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by LaboCred on 28/03/2017.
 */

public class MainLayout extends View {

    private static final int PILE_IN_ROW = 4;
    private static final int PILE_IN_COL = 4;

    private int width;
    private int height;
    private int touchRadius = 20;
    private int surfaceHorizontalPosition = 50;
    private int surfaceVerticalSize = 65;
    private int surfaceHorizontalSize = 100;
    private int surfaceVerticalPosition = 50;
    private TactileDialogViewHolder.OPTIONS opt;
    private TactileDialogViewHolder dialogViewHolder;

    Paint framePaint = new Paint();
    Paint touchFeedbackPaint = new Paint();
    Paint squareFeedbackPaint = new Paint();

    Map<Point, Rect> squares = new HashMap<Point, Rect>(); // squares of the surface to draw
    ArrayList<PointF> touchRelativePositions = new ArrayList<PointF>(); // touch positions relative to surface
    ArrayList<Point> touchAbsolutePositions = new ArrayList<Point>(); // touch position on the view
    Set<Point> squaresAffected = new HashSet<Point>(); // squares affected by touch

    public MainLayout(Context context) {
        super(context);
        init();
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        framePaint.setColor(Color.BLUE);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(1);

        touchFeedbackPaint.setColor(Color.BLUE);
        touchFeedbackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        touchFeedbackPaint.setAlpha(100);
        touchFeedbackPaint.setAntiAlias(true);

        squareFeedbackPaint.setColor(Color.BLUE);
        squareFeedbackPaint.setStyle(Paint.Style.FILL);
        squareFeedbackPaint.setAlpha(100);
    }

    private void updateMeasures() {
        squares.clear();

        int centerX = width * surfaceHorizontalPosition / 100;
        int centerY = height * surfaceVerticalPosition / 100;
        int squareWidth = width * surfaceHorizontalSize / PILE_IN_COL / 100;
        int squareHeight = height * surfaceVerticalSize / PILE_IN_ROW / 100;
        for (int i = -PILE_IN_COL / 2, px = 0; i < PILE_IN_COL / 2; i++, px++) {
            for (int j = -PILE_IN_ROW / 2, py = 0; j < PILE_IN_ROW / 2; j++, py++) {
                squares.put(new Point(px, py), new Rect(
                        centerX + i * squareWidth, centerY + j * squareHeight,
                        centerX + (i + 1) * squareWidth, centerY + (j + 1) * squareHeight
                ));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        updateMeasures();
    }

    public void setDialogViewHolder(TactileDialogViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
        opt = dialogViewHolder.GetOptions();
    }

    static public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 -= x1) * x2 + (y2 -= y1) * y2);
    }

    static public boolean circleIntersectsRect(Point c, int R, Rect rect) {
        if (rect.contains(c.x, c.y)) { return true; }

        if (rect.top <= c.y && c.y <= rect.bottom && (Math.abs(c.x - rect.left) <= R || Math.abs(c.x - rect.right) <= R)) { return true; }

        if (rect.left <= c.x && c.x <= rect.right && (Math.abs(c.y - rect.bottom) <= R || Math.abs(c.y - rect.top) <= R)) { return true; }

        if (distance(c.x, c.y, rect.left, rect.top) <= R) { return true; }
        if (distance(c.x, c.y, rect.right, rect.top) <= R) { return true; }
        if (distance(c.x, c.y, rect.right, rect.bottom) <= R) { return true; }
        if (distance(c.x, c.y, rect.left, rect.bottom) <= R) { return true; }

        return false;
    }

    public boolean onTouchEvent(MotionEvent e) {
        touchAbsolutePositions.clear();
        touchRelativePositions.clear();
        squaresAffected.clear();

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0; i < e.getPointerCount(); i++) {
                    Point absoluteTouch = new Point((int) e.getX(i), (int) e.getY(i));
                    PointF relativeTouch = new PointF((e.getX(i)) / width, (e.getY(i) ) / height);
                    touchRelativePositions.add(relativeTouch);

                    touchAbsolutePositions.add(absoluteTouch);
                    for (Map.Entry<Point, Rect> squareEntry : squares.entrySet()) {
                        if (!squaresAffected.contains(squareEntry.getKey()) && circleIntersectsRect(absoluteTouch, touchRadius, squareEntry.getValue())) {
                            squaresAffected.add(squareEntry.getKey());
                        }
                    }
                }

                break;
        }

        dialogViewHolder.onDialogTouch(new DialogTouchEvent(touchRelativePositions, squaresAffected, opt));
        invalidate();

        return true;
    }

    protected void onDraw(Canvas canvas) {

        for (Map.Entry<Point, Rect> squareEntry : squares.entrySet()) {  // Affichage de la Grille
            canvas.drawRect(squareEntry.getValue(), framePaint);
        }

        for (Point affectedSquare : squaresAffected) {                   // Affichage des cases touchées
            canvas.drawRect(squares.get(affectedSquare), squareFeedbackPaint);
        }
    }
}
