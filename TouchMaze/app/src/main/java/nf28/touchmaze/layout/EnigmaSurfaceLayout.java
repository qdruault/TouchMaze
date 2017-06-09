package nf28.touchmaze.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nf28.touchmaze.util.enigmaActivity.touch.EnigmaTactileViewHolder;
import nf28.touchmaze.util.enigmaActivity.touch.EnigmaTouchEvent;

import static nf28.touchmaze.util.enigmaActivity.touch.EnigmaTouchEvent.Action.RunT;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaSurfaceLayout extends View {

    private boolean activeTacticon = false;
    private boolean mobileTacticon = false;
    private int num;

    private EnigmaTactileViewHolder dialogViewHolder;

    public EnigmaSurfaceLayout(Context context) {
        super(context);
    }

    public EnigmaSurfaceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EnigmaSurfaceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent e) {
        if (activeTacticon) {
            dialogViewHolder.onDialogTouch(new EnigmaTouchEvent(RunT, num));
        }
        return true;
    }*/

    public boolean isActiveTacticon() {
        return activeTacticon;
    }

    public void setActiveTacticon(boolean activeTacticon) {
        this.activeTacticon = activeTacticon;
    }

    public boolean isMobileTacticon() {
        return mobileTacticon;
    }

    public void setMobileTacticon(boolean mobileTacticon) {
        this.mobileTacticon = mobileTacticon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public EnigmaTactileViewHolder getDialogViewHolder() {
        return dialogViewHolder;
    }

    public void setDialogViewHolder(EnigmaTactileViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
    }
}
