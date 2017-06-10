package nf28.touchmaze.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

/**
 * Created by Quentin on 10/06/2017.
 */

public class MapLayout extends View {

    private TactileDialogViewHolder dialogViewHolder;

    public MapLayout(Context context) {
        super(context);
    }

    public MapLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*public SurfaceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        dialogViewHolder.onDialogTouch(new DialogTouchEvent());
        return true;
    }

    public TactileDialogViewHolder getDialogViewHolder() {
        return dialogViewHolder;
    }

    public void setDialogViewHolder(TactileDialogViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
    }
}
