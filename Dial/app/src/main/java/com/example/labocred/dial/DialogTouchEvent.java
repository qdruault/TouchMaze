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

    /**
     * Génère une représentation texte des picots levés.
     * @return
     */
    public String getBoxesText() {
        String res = "";

        // On initialise le tableau avec des 0.
        int[][] boxes = new int[4][4];
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                boxes[x][y] = 0;
            }
        }
        
        // On met des 1 pour les picots levés.
        for (Point point : affectedBoxes) {
            boxes[point.x][point.y] = 1;
        }

        // On écrit.
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                res += boxes[y][x] == 1 ? "X" : "O";
            }
            res += "\n";
        }

        return res;
    }
}
