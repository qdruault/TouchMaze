package com.example.labocred.dial;

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

    // Nombre de rectangles sur une ligne.
    private static final int PILE_IN_ROW = 4;
    // Nombre de rectangles sur une colonne.
    private static final int PILE_IN_COL = 4;
    // Rayon de la surface touchée.
    public static int touchRadius = 20;

    // Largeur de la fenetre.
    private int width;
    // Hauteur de la fenetre.
    private int height;
    private int surfaceHorizontalPosition = 50;
    private int surfaceVerticalSize = 65;
    private int surfaceHorizontalSize = 100;
    private int surfaceVerticalPosition = 50;
    private TactileDialogViewHolder dialogViewHolder;

    Paint framePaint = new Paint();
    Paint touchFeedbackPaint = new Paint();
    Paint squareFeedbackPaint = new Paint();

    // Les rectangles à dessiner associés au picot à lever.
    Map<Point, Rect> squares = new HashMap<Point, Rect>();
    // touch positions relative to surface
    ArrayList<PointF> touchRelativePositions = new ArrayList<PointF>();
    // touch position on the view
    ArrayList<Point> touchAbsolutePositions = new ArrayList<Point>();
    // Picots à lever.
    Set<Point> squaresAffected = new HashSet<Point>();

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

        // Centre de la fenetre.
        int centerX = width * surfaceHorizontalPosition / 100;
        int centerY = height * surfaceVerticalPosition / 100;
        // Dimension des rectangles à dessiner.
        int squareWidth = width * surfaceHorizontalSize / PILE_IN_COL / 100;
        int squareHeight = height * surfaceVerticalSize / PILE_IN_ROW / 100;
        // Chaque colonne.
        for (int i = -PILE_IN_COL / 2, px = 0; i < PILE_IN_COL / 2; i++, px++) {
            // Chaque ligne.
            for (int j = -PILE_IN_ROW / 2, py = 0; j < PILE_IN_ROW / 2; j++, py++) {
                // On ajoute un rectangle et le picot à lever associé.
                squares.put(new Point(px, py), new Rect(
                        centerX + i * squareWidth, centerY + j * squareHeight,
                        centerX + (i + 1) * squareWidth, centerY + (j + 1) * squareHeight
                    )
                );
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
    }

    /**
     * Calcule la distance entre deux points.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    static public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 -= x1) * x2 + (y2 -= y1) * y2);
    }

    /**
     * Détermine si un cercle traverse un rectangle.
     * @param c : centre du cercle.
     * @param R : rayon du cercle.
     * @param rect : rectangle.
     * @return : true s'il traverse, false sinon.
     */
    static public boolean circleIntersectsRect(Point c, int R, Rect rect) {

        // Si le centre du cercle est dans le rectangle OK.
        if (rect.contains(c.x, c.y)) {
            return true;
        }

        // Le centre entre le haut et le bas du rectangle
        // en dehors du rectangle
        // plus proche du rectangle que son rayon OK.
        if (rect.top <= c.y
                && c.y <= rect.bottom
                && (Math.abs(c.x - rect.left) <= R || Math.abs(c.x - rect.right) <= R)
        ) {
            return true;
        }

        // Le centre entre le bord gauche et droit du rectangle
        // en dehors du rectangle
        // plus proche du rectangle que son rayon OK.
        if (rect.left <= c.x
                && c.x <= rect.right
                && (Math.abs(c.y - rect.bottom) <= R || Math.abs(c.y - rect.top) <= R)
        ) {
            return true;
        }

        // Si le cercle comprend un des coins du rectangle OK.
        if ((distance(c.x, c.y, rect.left, rect.top) <= R)
            || (distance(c.x, c.y, rect.right, rect.top) <= R)
            || (distance(c.x, c.y, rect.right, rect.bottom) <= R)
            || (distance(c.x, c.y, rect.left, rect.bottom) <= R)
        ) {
            return true;
        }

        // Sinon il n'y a pas d'intersection.
        return false;
    }

    /**
     * Lorsque l'on touche l'écran.
     * @param e : l'évenement.
     * @return
     */
    public boolean onTouchEvent(MotionEvent e) {
        // On vide les listes de zones touchées.
        touchAbsolutePositions.clear();
        touchRelativePositions.clear();
        squaresAffected.clear();

        switch (e.getActionMasked()) {
            // On touche
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            // On glisse.
            case MotionEvent.ACTION_MOVE:
            // On relache.
            case MotionEvent.ACTION_POINTER_UP:
                // On parcourt chaque point de la zone touchée.
                for (int i = 0; i < e.getPointerCount(); i++) {
                    // On récupère les coordonnées.
                    Point absoluteTouch = new Point((int) e.getX(i), (int) e.getY(i));
                    PointF relativeTouch = new PointF((e.getX(i)) / width, (e.getY(i) ) / height);
                    // On les ajoute aux listes.
                    touchRelativePositions.add(relativeTouch);
                    touchAbsolutePositions.add(absoluteTouch);

                    // On parcourt chaque rectangle.
                    for (Map.Entry<Point, Rect> squareEntry : squares.entrySet()) {
                        // Si le cercle formé par le point touché et le rayon du toucher
                        // touche le rectangle, on ajoute le picot associé à ce rectangle
                        // dans la liste des picots à lever si ce n'est pas déjà fait.
                        if (!squaresAffected.contains(squareEntry.getKey())
                                && circleIntersectsRect(absoluteTouch, touchRadius, squareEntry.getValue())
                        ) {
                            squaresAffected.add(squareEntry.getKey());
                        }
                    }
                }

                break;
        }

        // On execute la méthode onDialogTouch() de l'activité associée
        // pour lever les picots.
        dialogViewHolder.onDialogTouch(new DialogTouchEvent(touchRelativePositions, squaresAffected));
        invalidate();

        return true;
    }

    protected void onDraw(Canvas canvas) {

        // Grille
        for (Map.Entry<Point, Rect> squareEntry : squares.entrySet()) {
            canvas.drawRect(squareEntry.getValue(), framePaint);
        }

        // Mes cases
        for (Point affectedSquare : squaresAffected) {
            canvas.drawRect(squares.get(affectedSquare), squareFeedbackPaint);
        }
    }
}
