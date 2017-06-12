package nf28.touchmaze.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebStorage;

import nf28.touchmaze.R;
import nf28.touchmaze.maze.maze.Maze2D;
import nf28.touchmaze.maze.position.Position2D;

/**
 * Created by Baptiste on 11/06/2017.
 */

public class ShapeLayout extends View {

    private Maze2D maze;

    private static final int ORIGIN = 5;
    private static final int SQUARE_SIZE = 28;
    private static final int WALL_LONG_SIDE = 28;
    private static final int WALL_SHORT_SIDE = 2;
    private static final int OFFSET = 20;

    private Rect player;

    public ShapeLayout(Context context) {
        super(context);

        init(null);
    }

    public ShapeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public ShapeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public ShapeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (maze!=null) {

            drawLab(canvas);
        }
        else
        {
            // test
            player = new Rect();
            player.left = SQUARE_SIZE;
            player.top = SQUARE_SIZE;
            player.right = SQUARE_SIZE + SQUARE_SIZE;
            player.bottom = SQUARE_SIZE + SQUARE_SIZE;

            Paint playerPaint = new Paint();
            playerPaint.setColor(Color.GREEN);

            canvas.drawRect(player, playerPaint);
        }

    }

    private void drawLab(Canvas canv){

        for (int i = 0; i<10; i++) {
            for (int j = 0; j < 21; j++) {

                Log.d("Draw", String.valueOf(maze.vobstacles[i][j].getID()));
                if (maze.vobstacles[i][j].getID() == 1) {
                    Rect rect = new Rect();
                    rect.left = ORIGIN + SQUARE_SIZE + j * SQUARE_SIZE;
                    rect.top = ORIGIN + SQUARE_SIZE + i * SQUARE_SIZE;
                    rect.right = rect.left + WALL_SHORT_SIDE;
                    rect.bottom = rect.top + WALL_LONG_SIDE;

                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);

                    canv.drawRect(rect, paint);
                }
            }
        }

        for (int i = 0; i<11; i++) {
            for (int j = 0; j < 20; j++) {

                if (maze.hobstacles[i][j].getID() == 1) {
                    Rect rect = new Rect();
                    rect.left = ORIGIN + SQUARE_SIZE + j * SQUARE_SIZE;
                    rect.top = ORIGIN + SQUARE_SIZE + i * SQUARE_SIZE;
                    rect.right = rect.left + WALL_LONG_SIDE;
                    rect.bottom = rect.top + WALL_SHORT_SIDE;

                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);

                    canv.drawRect(rect, paint);
                }
            }
        }

        for (Position2D pos : maze.getEnigmas()) {

            Rect enigma = new Rect();
            enigma.left = ORIGIN + SQUARE_SIZE + pos.x * SQUARE_SIZE;
            enigma.top = ORIGIN + SQUARE_SIZE + pos.y * SQUARE_SIZE;
            enigma.right = enigma.left + SQUARE_SIZE;
            enigma.bottom = enigma.top + SQUARE_SIZE;

            Paint enigmaPaint = new Paint();
            enigmaPaint.setColor(Color.RED);

            canv.drawRect(enigma, enigmaPaint);
        }

        Rect endObjectiv = new Rect();
        endObjectiv.left = ORIGIN + SQUARE_SIZE + maze.getExit().x * SQUARE_SIZE;
        endObjectiv.top = ORIGIN + SQUARE_SIZE + maze.getExit().y * SQUARE_SIZE;
        endObjectiv.right = endObjectiv.left + SQUARE_SIZE;
        endObjectiv.bottom = endObjectiv.top + SQUARE_SIZE;

        Paint objPaint = new Paint();
        objPaint.setColor(Color.BLUE);

        canv.drawRect(endObjectiv, objPaint);

        player = new Rect();
        player.left = ORIGIN + SQUARE_SIZE + maze.getExplorerPosition().x * SQUARE_SIZE;
        player.top = ORIGIN + SQUARE_SIZE + maze.getExplorerPosition().y * SQUARE_SIZE;
        player.right = player.left + SQUARE_SIZE;
        player.bottom = player.top + SQUARE_SIZE;

        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.GREEN);

        canv.drawRect(player, playerPaint);


    }

    public Maze2D getMaze() {
        return maze;
    }

    public void setMaze(Maze2D maze) {
        this.maze = maze;
        invalidate();
    }
}
