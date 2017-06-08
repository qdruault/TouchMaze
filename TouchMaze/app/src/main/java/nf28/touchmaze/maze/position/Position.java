package nf28.touchmaze.maze.position;

/**
 * Created by theohordequin on 08/06/2017.
 */

public interface Position {

    public boolean is(Position p);
    public abstract Position toRight();
    public abstract Position toLeft();
    public abstract Position toFront();
    public abstract Position toRear();
    public abstract Position toTop();
    public abstract Position toBot();
}