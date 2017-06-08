package maze.obstacle;

public abstract class Wall extends Obstacle {

	@Override
	public boolean isTraversable() {
		return false;
	}
}
