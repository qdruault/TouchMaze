package nf28.touchmaze.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;


/**
 * Created by Baptiste on 05/06/2017.
 */

public class WallLayout extends View {

    private boolean activeWall;

    private TactileDialogViewHolder dialogViewHolder;

    public WallLayout(Context context) {
        super(context);
    }

    public WallLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WallLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*public SurfaceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (activeWall) {
            dialogViewHolder.onDialogTouch(new DialogTouchEvent());
        }
        return true;
    }

    public boolean isActiveWall() {
        return activeWall;
    }

    public void setActiveWall(boolean activeWall) {
        this.activeWall = activeWall;
    }

    public TactileDialogViewHolder getDialogViewHolder() {
        return dialogViewHolder;
    }

    public void setDialogViewHolder(TactileDialogViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
    }
}
