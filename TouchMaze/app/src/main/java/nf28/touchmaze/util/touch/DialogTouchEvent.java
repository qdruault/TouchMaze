package nf28.touchmaze.util.touch;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Baptiste on 07/06/2017.
 */

public class DialogTouchEvent {
    private ArrayList<PointF> touches;
    private TactileDialogViewHolder.OPTIONS Opt;


    public DialogTouchEvent(ArrayList<PointF> touches, TactileDialogViewHolder.OPTIONS opt) {
        this.touches = touches;
        this.Opt = opt;
    }

    public DialogTouchEvent() {
    }

    public String makeMessage() {
        String message = "{\"touchPos\": [";

        for(PointF p : touches) {
            message += "{\"x\": " + p.x + ", \"y\": " + p.y + "},";
        };
        if (touches.size() > 0) { // DA : Suppression de la dernière virgule
            message = message.substring(0, message.length() - 1);
        }

        message += "], \"Param\": [{\"r\":" +  Opt.touchRadius; // Accès direct par parthner

        // Pour mémoire enregistrement serveur

        String mess = "";
        mess = String.format("%1$03d-%2$c%3$c%4$c%5$c%6$c%7$c",
                Opt.touchRadius,
                Opt.IsMyPos?'x':'-',
                Opt.IsParthnerPos?'x':'-',
                Opt.IsModeTouchThrough?'x':'-',
                Opt.TTVisual?'x':'-',
                Opt.TTVibr?'x':'-',
                Opt.TTSound?'x':'-');


        message += ",\"p\":" + mess + "}]}";

        return message;
    }

}
