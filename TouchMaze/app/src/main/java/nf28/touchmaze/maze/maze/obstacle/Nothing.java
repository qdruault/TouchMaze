package nf28.touchmaze.maze.maze.obstacle;

/**
 * Created by theohordequin on 08/06/2017.
 */

public class Nothing extends Obstacle{

    public static final int ID = 0;

    static{
        id2Class.put(ID, Nothing.class);
    }


    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public boolean isVisibleByGuide() {
        return false;
    }

    @Override
    public boolean isTouchableByGuide() {
        return false;
    }

    @Override
    public boolean isTouchableByExplorer() {
        return false;
    }

    @Override
    public int getID() {
        return ID;
    }
}
