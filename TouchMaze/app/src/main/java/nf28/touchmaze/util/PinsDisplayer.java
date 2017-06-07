package nf28.touchmaze.util;

import android.util.Log;

/**
 * Created by Baptiste on 18/05/2017.
 */

public class PinsDisplayer {

    private static char[] pins = new char[16];

    public PinsDisplayer(){}

    public static void setAndDisplay(boolean[] leftTouches, boolean[] rightTouches){

        resetPins();

        int j = 0;
        for (int i = 0; i < 8; i++) {
            if (leftTouches[i]){
                pins[j]='x';
            }
            if ((i+1)%2==0){
                j+=3;
            }else{
                j++;
            }
        }
        j=2;
        for (int i = 0; i < 8; i++) {
            if (rightTouches[i]){
                pins[j]='x';
            }
            if ((i+1)%2==0){
                j+=3;
            }else{
                j++;
            }
        }

        displayPins();

    }

    public static void resetPins(){
        for (int i = 0; i < 16 ; ++i) {
            pins[i] = 'o';
        }
    }

    public static void displayPins(){
        String grid = "";
        for (int i = 0; i < 16; i++) {
            if (i%4==0)
                grid += "\n";
            grid += pins[i];
        }
        Log.d("pinsDisplayer", grid);
    }

}

