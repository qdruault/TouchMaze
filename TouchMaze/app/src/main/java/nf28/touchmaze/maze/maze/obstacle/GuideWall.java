package nf28.touchmaze.maze.maze.obstacle;

/**
 * Created by theohordequin on 08/06/2017.
 */

// MUR DU GUIDE
public class GuideWall extends Wall {

    public static final int ID = 3;

    static{
        id2Class.put(ID, GuideWall.class);
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
        return false;
    }

    @Override
    public int getID() {
        return ID;
    }

}
