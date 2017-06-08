package nf28.touchmaze.maze.maze;

import nf28.touchmaze.maze.maze.listeners.EnigmaListener;
import nf28.touchmaze.maze.maze.listeners.GameListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thibault on 06/06/2017.
 */
public class ExplorerCommunicator extends Communicator {

    private List<GameListener> gameListeners = new ArrayList<>();
    private List<EnigmaListener> enigmasListeners = new ArrayList<>();

    public void enigmaSolved(){
        sendMessage("ENIGMA SOLVED");
    }

    public void enigmaStarted(int number){
        sendMessage("ENIGMA #" + number);
    }

    public void moveTo(Direction2D d){
        sendMessage("MOVE "+d.name);
    }

    @Override
    public void receiveMessage(String s) {
        if(s.startsWith("WALLS:")){

        }
        else {
            switch (s) {
                case "START GAME":
                    gameListeners.forEach(GameListener::whenStarted);
                    break;
                case "NEW ENIGMA":
                    enigmasListeners.forEach(EnigmaListener::newEnigma);
                    break;
                case "GAME WON":
                    gameListeners.forEach(gameListener -> gameListener.whenEnded(true));
                    break;
                case "GAME LOST":
                    gameListeners.forEach(gameListener -> gameListener.whenEnded(false));
                    break;
            }
        }
    }

    public void addEnigmasListener(EnigmaListener l){
        enigmasListeners.add(l);
    }

    public void addGameListener(GameListener l){
        gameListeners.add(l);
    }
}
