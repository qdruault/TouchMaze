package nf28.touchmaze.layout;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import nf28.touchmaze.maze.maze.Maze2D;
import nf28.touchmaze.maze.maze.obstacle.Obstacle;
import nf28.touchmaze.maze.position.Position2D;

import static nf28.touchmaze.layout.MapLayout.ORIGIN;
import static nf28.touchmaze.layout.MapLayout.SQUARE_SIZE;
import static nf28.touchmaze.layout.MapLayout.WALL_LONG_SIDE;
import static nf28.touchmaze.layout.MapLayout.WALL_SHORT_SIDE;

/**
 * Created by Baptiste on 11/06/2017.
 */

public class MazeView {

    private Rect explorerV;
    private Rect exitV;
    private ArrayList<Rect> enigmasV;
    private RectWall[][] hobstaclesV;
    private RectWall[][] vobstaclesV;

    public MazeView(Maze2D maze){

        explorerV = new Rect();
        explorerV.left = ORIGIN + SQUARE_SIZE + maze.getExplorerPosition().x * SQUARE_SIZE;
        explorerV.top = ORIGIN + SQUARE_SIZE + maze.getExplorerPosition().y * SQUARE_SIZE;
        explorerV.right = explorerV.left + SQUARE_SIZE;
        explorerV.bottom = explorerV.top + SQUARE_SIZE;

        exitV = new Rect();
        exitV.left = ORIGIN + SQUARE_SIZE + maze.getExit().x * SQUARE_SIZE;
        exitV.top = ORIGIN + SQUARE_SIZE + maze.getExit().y * SQUARE_SIZE;
        exitV.right = exitV.left + SQUARE_SIZE;
        exitV.bottom = exitV.top + SQUARE_SIZE;

        enigmasV = new ArrayList<Rect>();

        for (Position2D pos : maze.getEnigmas()) {

            Rect enigma = new Rect();
            enigma.left = ORIGIN + SQUARE_SIZE + pos.x * SQUARE_SIZE;
            enigma.top = ORIGIN + SQUARE_SIZE + pos.y * SQUARE_SIZE;
            enigma.right = enigma.left + SQUARE_SIZE;
            enigma.bottom = enigma.top + SQUARE_SIZE;

            this.enigmasV.add(enigma);
        }

        vobstaclesV = new RectWall[10][21];

        for (int i = 0; i<10; i++) {
            for (int j = 0; j < 21; j++) {

                    RectWall rect = new RectWall();
                    rect.getRect().left = ORIGIN + SQUARE_SIZE + j * SQUARE_SIZE;
                    rect.getRect().top = ORIGIN + SQUARE_SIZE + i * SQUARE_SIZE;
                    rect.getRect().right = rect.getRect().left + WALL_SHORT_SIDE;
                    rect.getRect().bottom = rect.getRect().top + WALL_LONG_SIDE;

                    rect.setId(maze.vobstacles[i][j].getID());

                    this.vobstaclesV[i][j]=rect;

            }
        }

        hobstaclesV = new RectWall[11][20];

        for (int i = 0; i<11; i++) {
            for (int j = 0; j < 20; j++) {

                    RectWall rect = new RectWall();
                    rect.getRect().left = ORIGIN + SQUARE_SIZE + j * SQUARE_SIZE;
                    rect.getRect().top = ORIGIN + SQUARE_SIZE + i * SQUARE_SIZE;
                    rect.getRect().right = rect.getRect().left + WALL_LONG_SIDE;
                    rect.getRect().bottom = rect.getRect().top + WALL_SHORT_SIDE;

                    rect.setId(maze.hobstacles[i][j].getID());

                    this.hobstaclesV[i][j]=rect;
            }
        }

    }

    public void updateExplorer(Position2D pos){
        explorerV.left = ORIGIN + SQUARE_SIZE + pos.x * SQUARE_SIZE;
        explorerV.top = ORIGIN + SQUARE_SIZE + pos.y * SQUARE_SIZE;
        explorerV.right = explorerV.left + SQUARE_SIZE;
        explorerV.bottom = explorerV.top + SQUARE_SIZE;
    }

    public Rect getExplorerV() {
        return explorerV;
    }

    public void setExplorerV(Rect explorerV) {
        this.explorerV = explorerV;
    }

    public Rect getExitV() {
        return exitV;
    }

    public void setExitV(Rect exitV) {
        this.exitV = exitV;
    }

    public ArrayList<Rect> getEnigmasV() {
        return enigmasV;
    }

    public void setEnigmasV(ArrayList<Rect> enigmasV) {
        this.enigmasV = enigmasV;
    }

    public RectWall[][] getHobstaclesV() {
        return hobstaclesV;
    }

    public void setHobstaclesV(RectWall[][] hobstaclesV) {
        this.hobstaclesV = hobstaclesV;
    }

    public RectWall[][] getVobstaclesV() {
        return vobstaclesV;
    }

    public void setVobstaclesV(RectWall[][] vobstaclesV) {
        this.vobstaclesV = vobstaclesV;
    }
}
