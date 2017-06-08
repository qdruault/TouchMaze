package maze;

import maze.listeners.EnigmaListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Thibault on 06/06/2017.
 */
public class GuideCommunicator extends Communicator {


    private List<EnigmaListener> enigmasListeners = new ArrayList<>();
    private List<Consumer<Direction2D>> directionListeners = new ArrayList<>();


    void startGame(){
        sendMessage("START GAME");
    }

    void startEnigma(){
        sendMessage("NEW ENIGMA");
    }

    void endGame(Boolean won) {
        if(won) sendMessage("GAME WON");
        else sendMessage("GAME LOST");
    }

    @Override
    public void receiveMessage(String s){
        if(s.equals("ENIGMA SOLVED")){
            enigmasListeners.forEach(EnigmaListener::whenSolved);
        }
        else if(s.startsWith("ENIGMA #")){
            int enigma = Integer.decode(s.substring(8));
            enigmasListeners.forEach(enigmaListener -> enigmaListener.startedWith(enigma));
        }
        else if(s.startsWith("MOVE")){
            String d = s.substring(5);
            Direction2D direction = null;
            switch (d) {
                case "RIGHT":
                    direction = Direction2D.RIGHT;
                    break;
                case "FRONT":
                    direction = Direction2D.FRONT;
                    break;
                case "REAR":
                    direction = Direction2D.REAR;
                    break;
                case "LEFT":
                    direction = Direction2D.LEFT;
                    break;
            }

            if(direction != null){
                Direction2D finalDirection = direction;
                directionListeners.forEach(direction2DConsumer -> direction2DConsumer.accept(finalDirection));

            }

        }
    }

    public void addEnigmasListener(EnigmaListener l){
        enigmasListeners.add(l);
    }

    public void addDirectionListener(Consumer<Direction2D> l){
        directionListeners.add(l);
    }
}
