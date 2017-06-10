package nf28.touchmaze.maze.position;

/**
 * Created by theohordequin on 08/06/2017.
 */

public class Position3D extends Position2D{

    public int z;

    public Position3D(int x, int y, int z) {
        super(x,y);
        this.z = z;
    }

    @Override
    public Position3D toTop() {
        z+=1;
        return this;
    }

    @Override
    public Position3D toBot() {
        z-=1;
        return this;
    }


    @Override
    public boolean is(Position p) {
        if(p.getClass() == getClass()){
            Position3D that = (Position3D) p;
            if(that.x == x && that.y == y && that.z == z){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "Position[x = " + x + ", y = " + y +", z = " + z + "]";
    }

}
