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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nf28.touchmaze.maze.maze.Maze2D;
import nf28.touchmaze.maze.position.Position2D;
import nf28.touchmaze.util.PinsDisplayer;
import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

/**
 * Created by Quentin on 10/06/2017.
 */

public class MapLayout extends View {

    //EMULATEUR

    public static final int ORIGINX = 40;
    public static final int ORIGINY = 20;
    public static final int MARGIN = 8;
    public static final int SQUARE_SIZE = 48;
    public static final int WALL_LONG_SIDE = 56;
    public static final int WALL_SHORT_SIDE = 8;


    // TEL
    /*
    public static final int ORIGINX = 60;
    public static final int ORIGINY = 10;
    public static final int MARGIN = 12;
    public static final int SQUARE_SIZE = 72;
    public static final int WALL_LONG_SIDE = 84;
    public static final int WALL_SHORT_SIDE = 12;
    */

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
            drawGrid(canvas);
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
                else if (mazeV.getVobstaclesV()[i][j].getId() == 2) {
                    canv.drawRect(mazeV.getVobstaclesV()[i][j].getRect(), playerPaint);
                }
                else if (mazeV.getVobstaclesV()[i][j].getId() == 3) {
                    canv.drawRect(mazeV.getVobstaclesV()[i][j].getRect(), objPaint);
                }
            }
        }

        for (int i = 0; i<11; i++) {
            for (int j = 0; j < 20; j++) {
                if (mazeV.getHobstaclesV()[i][j].getId() == 1) {
                    canv.drawRect(mazeV.getHobstaclesV()[i][j].getRect(), paint);
                }
                else if (mazeV.getHobstaclesV()[i][j].getId() == 2) {
                    canv.drawRect(mazeV.getHobstaclesV()[i][j].getRect(), playerPaint);
                }
                else if (mazeV.getHobstaclesV()[i][j].getId() == 3) {
                    canv.drawRect(mazeV.getHobstaclesV()[i][j].getRect(), objPaint);
                }
            }
        }

        for (Rect rect : mazeV.getEnigmasV()) {
            canv.drawRect(rect, enigmaPaint);
        }

        canv.drawRect(mazeV.getExitV(), objPaint);

        canv.drawRect(mazeV.getExplorerV(), playerPaint);

    }

    private void drawGrid(Canvas canv){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        for (int i = 0; i<10; i++) {
            for (int j = 0; j < 21; j++) {
                Rect rect = new Rect();
                rect.left = ORIGINX + j * WALL_LONG_SIDE;
                rect.top = ORIGINY + i * WALL_LONG_SIDE;
                rect.right = rect.left + WALL_SHORT_SIDE;
                rect.bottom = rect.top + WALL_LONG_SIDE;

                canv.drawRect(rect, paint);
            }
        }

        for (int i = 0; i<11; i++) {
            for (int j = 0; j < 20; j++) {
                Rect rect = new Rect();
                rect.left = ORIGINX + j * WALL_LONG_SIDE;
                rect.top = ORIGINY + i * WALL_LONG_SIDE;
                rect.right = rect.left + WALL_LONG_SIDE;
                rect.bottom = rect.top + WALL_SHORT_SIDE;

                canv.drawRect(rect, paint);
            }
        }
    }

    public void updateExplorerV(Position2D pos){
        mazeV.updateExplorer(pos);
        postInvalidate();
    }

    public void updateEnigmasV (List<Position2D> enigmas){
        mazeV.updateEnigmas(enigmas);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int x = (int) e.getX();
        int y = (int) e.getY();

        int Xmin = 0;
        int Xmax = 10;
        int Ymin = 0;
        int Ymax = 20;

        /*int decalage = 5;

        int Xmin = x/20 - decalage;
        int Xmax = x/20 + decalage;
        int Ymin = y/20 - decalage;
        int Ymax = y/20 + decalage;

        if(Xmax > 10)
            Xmax = 10;
        if(Xmin > 10)
            Xmin = 10-(decalage+1);
        if(Xmax < 0)
            Xmax = (decalage+1);
        if(Xmin < 0)
            Xmin = 0;

        if(Ymax > 20)
            Ymax = 20;
        if(Ymin > 20)
            Ymin = 20-(decalage+1);
        if(Ymax < 0)
            Ymax = (decalage+1);
        if(Ymin < 0)
            Ymin = 0;*/

        for (int i = Xmin; i<Xmax; i++) {
            for (int j = Ymin; j < Ymax+1 ; j++) {
                if (mazeV.getVobstaclesV()[i][j].getId()==1 || mazeV.getVobstaclesV()[i][j].getId()==3) {
                    if (mazeV.getVobstaclesV()[i][j].getRect().contains(x, y)) {
                        dialogViewHolder.onDialogTouch(new DialogTouchEvent());
                        return true;
                    }
                }
            }
        }

        for (int i = Xmin; i<Xmax+1; i++) {
            for (int j = Ymin; j < Ymax ; j++) {
                if (mazeV.getHobstaclesV()[i][j].getId()==1 || mazeV.getHobstaclesV()[i][j].getId()==3) {
                    if (mazeV.getHobstaclesV()[i][j].getRect().contains(x, y)) {
                        dialogViewHolder.onDialogTouch(new DialogTouchEvent());
                        return true;
                    }
                }
            }
        }

        /*switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("TAG", "touched down (" + x + ", " + y + ")");
                Log.d("TAG", "explo (" + mazeV.getExplorerV().left + " " + mazeV.getExplorerV().top + " " + mazeV.getExplorerV().right + " " + mazeV.getExplorerV().bottom);
                Log.d("TAG", "explo (" + mazeV.getExitV().left + " " + mazeV.getExitV().top + " " + mazeV.getExitV().right + " " + mazeV.getExitV().bottom);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("TAG", "moving: (" + x/20 + ", " + y/20 + ") x : " + Xmin + Xmax + " y : " + Ymin + Ymax);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TAG", "touched up");
                break;
        }*/

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
