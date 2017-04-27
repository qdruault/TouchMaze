package com.example.labocred.mirrormodedialtact;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by LaboCred on 29/03/2017.
 */

public class DialogTouchEvent {

    private ArrayList<PointF> touches;
    private Set<Point> affectedBoxes;
    private TactileDialogViewHolder.OPTIONS Opt;

    public DialogTouchEvent(ArrayList<PointF> touches, Set<Point> affectedBoxes, TactileDialogViewHolder.OPTIONS opt) {
        this.touches = touches;
        this.affectedBoxes = affectedBoxes;
        this.Opt = opt;
    }

    public Set<Point> getAffectedBoxes() {
        return affectedBoxes;
    }

    public String makeMessage() {
        String message = "{\"touchPos\": [";

        for(PointF p : touches) {
            message += "{\"x\": " + p.x + ", \"y\": " + p.y + "},";
        }
        if (touches.size() > 0) { // DA : Suppression de la dernière virgule
            message = message.substring(0, message.length() - 1);
        }

        message += "], \"boxes\": [";

        for(Point affectedBox : affectedBoxes) {
            message += "{\"x\": " + affectedBox.x + ", \"y\": " + affectedBox.y + "},";
        }
        if (affectedBoxes.size() > 0) { // DA : Suppression de la dernière virgule
            message = message.substring(0, message.length() - 1);
        }

        message += "], \"Param\": [{\"r\":" +  Opt.touchRadius; // Accès direct par parthner

        message += ",\"p\":}]}";

        return message;
    }
}
