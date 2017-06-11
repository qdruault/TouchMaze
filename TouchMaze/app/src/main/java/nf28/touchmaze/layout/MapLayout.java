package nf28.touchmaze.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import nf28.touchmaze.maze.maze.Maze2D;
import nf28.touchmaze.maze.position.Position2D;
import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

/**
 * Created by Quentin on 10/06/2017.
 */

public class MapLayout extends View {

    public static final int ORIGIN = 5;
    public static final int SQUARE_SIZE = 28;
    public static final int WALL_LONG_SIDE = 28;
    public static final int WALL_SHORT_SIDE = 2;

    private MazeView mazeV;

    private TactileDialogViewHolder dialogViewHolder;

    public MapLayout(Context context) {
        super(context);
        init(null);
    }

    public MapLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MapLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /*public MapLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }*/

    private void init(@Nullable AttributeSet set){

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mazeV!=null) {
            drawLab(canvas);
        }
        else
        {
            // test
            Rect r = new Rect();
            r.left = SQUARE_SIZE;
            r.top = SQUARE_SIZE;
            r.right = SQUARE_SIZE + SQUARE_SIZE;
            r.bottom = SQUARE_SIZE + SQUARE_SIZE;

            Paint playerPaint = new Paint();
            playerPaint.setColor(Color.GREEN);

            canvas.drawRect(r, playerPaint);
        }

    }

    private void drawLab(Canvas canv){

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        Paint enigmaPaint = new Paint();
        enigmaPaint.setColor(Color.RED);

        Paint objPaint = new Paint();
        objPaint.setColor(Color.BLUE);

        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.GREEN);

        for (int i = 0; i<10; i++) {
            for (int j = 0; j < 21; j++) {
                if (mazeV.getVobstaclesV()[i][j].getId() == 1) {
                    canv.drawRect(mazeV.getVobstaclesV()[i][j].getRect(), paint);
                }
            }
        }

        for (int i = 0; i<11; i++) {
            for (int j = 0; j < 20; j++) {
                if (mazeV.getHobstaclesV()[i][j].getId() == 1) {
                    canv.drawRect(mazeV.getHobstaclesV()[i][j].getRect(), paint);
                }
            }
        }

        for (Rect rect : mazeV.getEnigmasV()) {
            canv.drawRect(rect, enigmaPaint);
        }

        canv.drawRect(mazeV.getExitV(), objPaint);

        canv.drawRect(mazeV.getExplorerV(), playerPaint);

    }

    public void updateExplorerV(Position2D pos){
        mazeV.updateExplorer(pos);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        dialogViewHolder.onDialogTouch(new DialogTouchEvent());
        return true;
    }

    public TactileDialogViewHolder getDialogViewHolder() {
        return dialogViewHolder;
    }

    public void setDialogViewHolder(TactileDialogViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
    }

    public MazeView getMazeV() {
        return mazeV;
    }

    public void setMazeV(MazeView mazeV) {
        this.mazeV = mazeV;
    }

    public void constructMazeV(Maze2D maze) {
        this.mazeV = new MazeView(maze);
    }
}
