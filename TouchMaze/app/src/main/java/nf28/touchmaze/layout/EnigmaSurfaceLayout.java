package nf28.touchmaze.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaSurfaceLayout extends View {

    private int num;

    public EnigmaSurfaceLayout(Context context) {
        super(context);
    }

    public EnigmaSurfaceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EnigmaSurfaceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
