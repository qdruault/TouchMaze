package nf28.touchmaze.maze.maze;

import nf28.touchmaze.maze.maze.obstacle.Obstacle;
import nf28.touchmaze.maze.position.Position;
public class Direction2D {

    private String name;
    private Maze2D maze;

    public Direction2D (Maze2D maze, String name) {
        this.maze = maze;
        this.name = name;
    }

    public Position accept() {
        switch (name) {
            case "LEFT" :
                return maze.getExplorerPosition().toLeft();
            case "RIGHT" :
                return maze.getExplorerPosition().toRight();
            case "FRONT" :
                return maze.getExplorerPosition().toFront();
            default:
                return maze.getExplorerPosition().toRear();
        }
    }

    public Obstacle apply() {
        switch (name) {
            case "LEFT" :
                return maze.vobstacles[maze.explorer.y][maze.explorer.x];
            case "RIGHT" :
                return maze.vobstacles[maze.explorer.y][maze.explorer.x+1];
            case "FRONT" :
                return maze.hobstacles[maze.explorer.y+1][maze.explorer.x];
            default:
                return maze.hobstacles[maze.explorer.y][maze.explorer.x];
        }
    }

}