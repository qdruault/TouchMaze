package position;

/**
 * Created by Thibault on 28/05/2017.
 */
public class PositionBuilder {

    public static Position1D p(int x) { return new Position1D(x);}
    public static Position2D p(int x, int y) { return new Position2D(x, y);}
    public static Position3D p(int x, int y, int z) { return new Position3D(x,y,z);}

}
