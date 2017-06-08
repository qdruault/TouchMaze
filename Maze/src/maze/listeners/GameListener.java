package maze.listeners;

/**
 * Created by Thibault on 06/06/2017.
 */
public abstract class GameListener {

    public abstract void whenStarted();

    public abstract void whenEnded(Boolean won);

}
