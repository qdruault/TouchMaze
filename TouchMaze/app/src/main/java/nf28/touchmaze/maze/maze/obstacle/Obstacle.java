package nf28.touchmaze.maze.maze.obstacle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thibault on 22/05/2017.
 */
public abstract class Obstacle {

    public static int ID;

    public static Map<Integer, Class> id2Class = new HashMap<>();

    public abstract boolean isTraversable();
    public abstract boolean isVisibleByGuide();
    public abstract boolean isTouchableByGuide();
    public abstract boolean isTouchableByExplorer();
    public abstract int getID();

    public static Obstacle fromId(int id) throws IllegalAccessException, InstantiationException {
        Integer iid = id-48;
        Class c = id2Class.get(iid);
        Object obs = c.newInstance();
        return ((Obstacle) obs);
    }
}

