package nf28.touchmaze.maze.maze.obstacle;

/**
 * Created by theohordequin on 08/06/2017.
 */

public abstract class Wall extends Obstacle {

    @Override
    public boolean isTraversable() {
        return false;
    }
}

