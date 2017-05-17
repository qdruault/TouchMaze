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

    // Liste des points touchés.
    private ArrayList<PointF> touches;
    // Liste des picots à lever.
    private Set<Point> affectedBoxes;

    /**
     * COnstructeur
     * @param touches : points touchés.
     * @param affectedBoxes : picots à lever.
     */
    public DialogTouchEvent(ArrayList<PointF> touches, Set<Point> affectedBoxes) {
        this.touches = touches;
        this.affectedBoxes = affectedBoxes;
    }

    public Set<Point> getAffectedBoxes() {
        return affectedBoxes;
    }

    /**
     * Ecrit les points touchés et les picots à laver.
     * @return
     */
    public String makeMessage() {
        String message = "{\"touchPos\": [";

        // Pour chaque point touché.
        for(PointF p : touches) {
            // On ajoute ses coordonnées au message.
            message += "{\"x\": " + p.x + ", \"y\": " + p.y + "},";
        }

        // Suppression de la dernière virgule
        if (touches.size() > 0) {
            message = message.substring(0, message.length() - 1);
        }

        message += "], \"boxes\": [";

        // Pour chaque picot à lever.
        for(Point affectedBox : affectedBoxes) {
            // On ajoute ses coordonnées au message.
            message += "{\"x\": " + affectedBox.x + ", \"y\": " + affectedBox.y + "},";
        }

        // Suppression de la dernière virgule.
        if (affectedBoxes.size() > 0) {
            message = message.substring(0, message.length() - 1);
        }

        // Ajout du rayon du toucher.
        message += "], \"Param\": [{\"r\":" + touchRadius;

        message += ",\"p\":}]}";

        return message;
    }
}
