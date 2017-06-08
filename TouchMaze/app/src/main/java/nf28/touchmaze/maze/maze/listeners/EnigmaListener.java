package nf28.touchmaze.maze.maze.listeners;

/**
 * Created by Thibault on 06/06/2017.
 */
public abstract class EnigmaListener {

    public abstract void whenSolved();

    public abstract void newEnigma();

    public abstract void startedWith(int x);
}

