package com.example.labocred.dial;

import android.graphics.Point;

import java.util.Set;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TouchConverter {
    /**
     * Génère le tableau de bytes pour lever les picots.
     * @param touches : liste des picots à lever.
     * @return
     */
    public static byte[] SetToByte(Set<Point> touches) {
        boolean[] rightTouches = {false, false, false, false, false, false, false, false};
        boolean[] leftTouches = {false, false, false, false, false, false, false, false};
        // Pour chaque picot à lever.
        for (Point entry : touches)
        {
            // On récupère les coordonnées.
            int x = entry.x;
            int y = entry.y;
            // On met à true la case qu'il faut.
            if (x < 2) {
                leftTouches[y * 2 + x] = true;
            } else {
                rightTouches[y * 2 + x - 2] = true;
            }
        }

        byte[] data = new byte[4];
        data[0] = 0x1b;
        data[1] = 0x01;
        data[2] = regularBoolToByte(rectifyTouches(leftTouches));
        data[3] = regularBoolToByte(rectifyTouches(rightTouches));

        return data;
    }

    /**
     * Convertir le tableau de bool en byte.
     * @param p_tab : le tableau de bool à convertir
     * @return
     */
    static byte regularBoolToByte(boolean[] p_tab) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            // Remplace les false par 0 et les true par 1.
            output.append(p_tab[i] ? '1' : '0');
        }
        return (byte) Integer.parseInt(output.toString(), 2);
    }

    /**
     * Remet le tableau dans l'ordre.
     * Don't ask me why. I don't know
     */

    static boolean[] rectifyTouches(boolean[] t) {
        return new boolean[]{t[1], t[0], t[3], t[5], t[7], t[2], t[4], t[6]};
    }
}
