package maze;

import java.util.function.Consumer;
import java.util.function.Function;
import maze.obstacle.Obstacle;
import position.Position2D;

public enum Direction2D {
	LEFT("LEFT", m -> m.vobstacles[m.explorer.y][m.explorer.x], position -> {position.toLeft();}),
	RIGHT("RIGHT", m -> m.vobstacles[m.explorer.y][m.explorer.x+1], position -> {position.toRight();}),
	FRONT("FRONT", m -> m.hobstacles[m.explorer.y][m.explorer.x], position -> {position.toFront();}),
	REAR("REAR", m -> m.hobstacles[m.explorer.y+1][m.explorer.x], position -> {position.toRear();});
	
	public String name;
	public Function<Maze2D, Obstacle> obstacle;
	Consumer<Position2D> move;

	Direction2D(String name, Function<Maze2D, Obstacle> object, Consumer<Position2D> move) {
		this.name = name;
		this.obstacle = object;
		this.move = move;
	}

}