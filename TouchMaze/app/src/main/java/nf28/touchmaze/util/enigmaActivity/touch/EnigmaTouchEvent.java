package nf28.touchmaze.util.enigmaActivity.touch;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Baptiste on 09/06/2017.
 */

public class EnigmaTouchEvent {

    public enum Action {
        RunT,
        MoveT
    }

    private int EnigmaSurfaceLayoutNumber;
    private int EnigmaSurfaceLayoutToMove;
    private Action action;


    public EnigmaTouchEvent(Action p_action, int p_EnigmaSurfaceLayoutNumber) {
        this.EnigmaSurfaceLayoutNumber = p_EnigmaSurfaceLayoutNumber;
        this.action = p_action;
    }

    public EnigmaTouchEvent(Action p_action, int p_EnigmaSurfaceLayoutNumber, int p_EnigmaSurfaceLayoutToMove) {
        this.EnigmaSurfaceLayoutNumber = p_EnigmaSurfaceLayoutNumber;
        this.action = p_action;
        this.EnigmaSurfaceLayoutToMove = p_EnigmaSurfaceLayoutToMove;
    }

    public int getEnigmaSurfaceLayoutNumber() {
        return EnigmaSurfaceLayoutNumber;
    }

    public int getEnigmaSurfaceLayoutToMove() {
        return EnigmaSurfaceLayoutToMove;
    }

    public Action getAction() {
        return action;
    }
}
