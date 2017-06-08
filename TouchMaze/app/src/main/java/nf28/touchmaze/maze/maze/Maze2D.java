package nf28.touchmaze.maze.maze;

import nf28.touchmaze.maze.maze.obstacle.*;
import nf28.touchmaze.maze.position.Position2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by theohordequin on 08/06/2017.
 */

public class Maze2D extends Maze{

    static{
        new ExplorerWall();
        new GuideWall();
        new Nothing();
        new TouchableWall();
        //needed to execute static blocks in classes
    }

    Position2D explorer;
    private Position2D exit;
    private List<Position2D> enigmas;
    public Obstacle[][] hobstacles;
    public Obstacle[][] vobstacles;
    private GuideCommunicator gc;

    public Maze2D() throws InstantiationException, IllegalAccessException {
        BufferedReader br = null;
        FileReader fr = null;

        String FILENAME = "ressources\\Lab1";

        try {

            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(FILENAME));

            String[] dims = br.readLine().split(" ");

            br.readLine();

            int x = Integer.parseInt(dims[0]);
            int y = Integer.parseInt(dims[1]);

            hobstacles = new Obstacle[y+1][x];
            vobstacles = new Obstacle[y][x+1];


            int i = 0;
            //HORIZONTAUX
            while (!(sCurrentLine = br.readLine()).equals("")) {
                if(sCurrentLine.length() != x){
                    System.err.println("Erreur h : ligne "+ i + " taille " + sCurrentLine.length() + " pour " +  x);
                }
                for(int c = 0; c < sCurrentLine.length(); c++){
                    int id = sCurrentLine.charAt(c);
                    Obstacle obs = Obstacle.fromId(id);
                    hobstacles[i][c] = obs;
                }

                i++;
            }

            if(i != y + 1 ){
                System.err.println("Erreur h :"+ i + " lignes pour " + y + "attendues");
            }

            i = 0;
            //VERTICAUX
            while (!(sCurrentLine = br.readLine()).equals("")) {
                if(sCurrentLine.length() != x + 1 ){
                    System.err.println("Erreur v : ligne "+ i + " taille " + sCurrentLine.length() + " pour " +  x + 1);
                }
                for(int c = 0; c < sCurrentLine.length(); c++){
                    int id = sCurrentLine.charAt(c);
                    Obstacle obs = Obstacle.fromId(id);
                    vobstacles[i][c] = obs;
                }

                i++;
            }

            if(i != y){
                System.err.println("Erreur h :"+ i + " lignes pour " +  y  + "attendues");
            }

            String[] start = br.readLine().split(" ");
            br.readLine();
            String[] end = br.readLine().split(" ");

            Position2D startPos = p(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
            Position2D endPos = p(Integer.parseInt(end[0]), Integer.parseInt(end[1]));

            br.readLine();

            ArrayList<Position2D> enigms = new ArrayList<>();
            String[] enigmsString = br.readLine().split(" X ");
            for (String s : enigmsString) {
                String[] temp = s.split(" ");
                Position2D ptemp = p(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
                enigms.add(ptemp);
            }

            explorer = startPos;
            exit = endPos;
            enigmas = enigms;

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    public Position2D getExplorerPosition() { return explorer;}


    private boolean canMoveTo(Direction2D d){
        return d.obstacle.apply(this).isTraversable();
    }

    public void init(GuideCommunicator gc){
        this.gc = gc;
        gc.addDirectionListener(direction2D -> {
            moveTo(direction2D);
            String s = "";
            s += Direction2D.FRONT.obstacle.apply(this).isTouchableByExplorer()+",";
            s += Direction2D.RIGHT.obstacle.apply(this).isTouchableByExplorer()+",";
            s += Direction2D.REAR.obstacle.apply(this).isTouchableByExplorer()+",";
            s += Direction2D.LEFT.obstacle.apply(this).isTouchableByExplorer();
            gc.sendMessage("WALLS: "+s);
        });
        gc.startGame();
    }

    private void moveTo(Direction2D d){
        if(canMoveTo(d)){
            d.move.accept(explorer);
            enigmas.stream().filter(p -> p.is(explorer)).findFirst().ifPresent( p -> {
                gc.startEnigma();
                enigmas.remove(p);
            });
            if(explorer.is(exit) && enigmas.isEmpty()){
                gc.endGame(true);
            }
        }
        else{
            /* Error handling */
        	/*
        	 * Here is the code to execute when explorer make a mistake and try to cross an obstacle
        	 * which is not traversable.
        	 * Players may lost one life point, etc.
        	 */
        }
    }




}
