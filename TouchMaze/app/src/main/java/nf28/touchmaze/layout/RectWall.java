package nf28.touchmaze.layout;

import android.graphics.Rect;

import nf28.touchmaze.maze.maze.obstacle.Obstacle;

/**
 * Created by Baptiste on 11/06/2017.
 */

public class RectWall {
    private Rect rect;
    private int id;

    public RectWall(){
        rect = new Rect();
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
