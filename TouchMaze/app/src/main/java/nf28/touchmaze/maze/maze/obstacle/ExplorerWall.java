package nf28.touchmaze.maze.maze.obstacle;

public class ExplorerWall extends Wall {

    public static final int ID = 2;

    static{
        id2Class.put(ID, ExplorerWall.class);
    }

    @Override
    public boolean isVisibleByGuide() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTouchableByGuide() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTouchableByExplorer() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int getID() {
        return ID;
    }

}
