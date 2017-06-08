package nf28.touchmaze.maze.maze.obstacle;

/**
 * Created by theohordequin on 08/06/2017.
 */

public class TouchableWall extends Wall {

    public static final int ID = 3;

    static{
        id2Class.put(ID, TouchableWall.class);
    }

    @Override
    public boolean isVisibleByGuide() {
        return false;
    }

    @Override
    public boolean isTouchableByGuide() {
        return true;
    }

    @Override
    public boolean isTouchableByExplorer() {
        return true;
    }

    @Override
    public int getID() {
        return ID;
    }

}
