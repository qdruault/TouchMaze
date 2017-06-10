package nf28.touchmaze.maze.position;

/**
 * Created by theohordequin on 08/06/2017.
 */

public class Position2D extends Position1D{

    public int y;

    public Position2D(int x, int y) {
        super(x);
        this.y = y;
    }

    @Override
    public Position toFront() {
        y+=1;
        return this;
    }

    @Override
    public Position toRear() {
        y-=1;
        return this;
    }

    @Override
    public boolean is(Position p) {
        if(p.getClass() == getClass()){
            Position2D that = (Position2D) p;
            if(that.x == x && that.y == y){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "Position[x = " + x + ", y = " + y +"]";
    }

}
