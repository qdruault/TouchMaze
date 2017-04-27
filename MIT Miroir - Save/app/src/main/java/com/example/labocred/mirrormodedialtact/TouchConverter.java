package com.example.labocred.mirrormodedialtact;

import android.graphics.Point;

import java.util.Set;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TouchConverter {
    public static byte[] SetToByte(Set<Point> touches) {
        boolean[] rightTouches = {false, false, false, false, false, false, false, false};
        boolean[] leftTouches = {false, false, false, false, false, false, false, false};
        for (Point entry : touches)
        {
            int x = entry.x;
            int y = entry.y;
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

    static byte regularBoolToByte(boolean[] p_tab) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            output.append(p_tab[i] ? '1' : '0');
        }
        return (byte) Integer.parseInt(output.toString(), 2);
    }

    /**
     * Don't ask me why. I don't know
     */

    static boolean[] rectifyTouches(boolean[] t) {
        boolean[] result = {t[1], t[0], t[3], t[5], t[7], t[2], t[4], t[6]};
        return result;
    };
}
