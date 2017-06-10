package nf28.touchmaze.maze.position;

/**
 * Created by theohordequin on 08/06/2017.
 */

public class PositionBuilder {

    public static Position1D p(int x) { return new Position1D(x);}
    public static Position2D p(int x, int y) { return new Position2D(x, y);}
    public static Position3D p(int x, int y, int z) { return new Position3D(x,y,z);}

}
