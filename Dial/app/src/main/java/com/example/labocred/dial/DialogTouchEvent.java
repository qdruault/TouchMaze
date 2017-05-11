package com.example.labocred.dial;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Set;

import static com.example.labocred.dial.MainLayout.touchRadius;

/**
 * Created by LaboCred on 29/03/2017.
 */

public class DialogTouchEvent {

    private ArrayList<PointF> touches;
    private Set<Point> affectedBoxes;

    public DialogTouchEvent(ArrayList<PointF> touches, Set<Point> affectedBoxes) {
        this.touches = touches;
        this.affectedBoxes = affectedBoxes;
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

        message += "], \"Param\": [{\"r\":" + touchRadius ; // Accès direct par parthner

        message += ",\"p\":}]}";

        return message;
    }
}
