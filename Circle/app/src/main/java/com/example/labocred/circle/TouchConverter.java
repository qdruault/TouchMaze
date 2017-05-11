package com.example.labocred.circle;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class TouchConverter {
    static int compteur = 0;
    static byte[] SetToByte() {
        boolean[] rightTouches = {false, false, false, false, false, false, false, false};
        boolean[] leftTouches = {false, false, false, false, false, false, false, false};

        //Permet l'allumage des picots dans l'ordre indiqué

        if (compteur == 0) {
            rightTouches[1] = true;
            stopThread();
            compteur++;
        }else if(compteur == 1){
            rightTouches[3] = true;
            stopThread();
            compteur++;
        }else if(compteur == 2){
            rightTouches[5] = true;
            stopThread();
            compteur++;
        }else if(compteur == 3){
            rightTouches[7] = true;
            stopThread();
            compteur++;
        }else if(compteur == 4) {
            rightTouches[6] = true;
            stopThread();
            compteur++;
        }else if(compteur == 5) {
            leftTouches[7] = true;
            stopThread();
            compteur++;
        }else if(compteur == 6) {
            leftTouches[6] = true;
            stopThread();
            compteur++;
        }else if(compteur == 7) {
            leftTouches[4] = true;
            stopThread();
            compteur++;
        }else if(compteur == 8) {
            leftTouches[2] = true;
            stopThread();
            compteur++;
        }else if(compteur == 9) {
            leftTouches[0] = true;
            stopThread();
            compteur++;
        }else if(compteur == 10) {
            leftTouches[1] = true;
            stopThread();
            compteur++;
        }else if(compteur == 11) {
            rightTouches[0] = true;
            stopThread();
            compteur++;
        }else if(compteur == 12) {
            rightTouches[2] = true;
            stopThread();
            compteur++;
        }else if(compteur == 13) {
            rightTouches[4] = true;
            stopThread();
            compteur++;
        }else if(compteur == 14) {
            leftTouches[5] = true;
            stopThread();
            compteur++;
        }else if(compteur == 15) {
            leftTouches[3] = true;
            stopThread();
            compteur = 0;
        }

        byte[] data = new byte[4];
        data[0] = 0x1b;
        data[1] = 0x01;
        data[2] = regularBoolToByte(rectifyTouches(leftTouches));
        data[3] = regularBoolToByte(rectifyTouches(rightTouches));

        return data;
    }

    //Permet l'attente du Thread pour un temps donné

    static void stopThread(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        return new boolean[]{t[1], t[0], t[3], t[5], t[7], t[2], t[4], t[6]};
    }
}
