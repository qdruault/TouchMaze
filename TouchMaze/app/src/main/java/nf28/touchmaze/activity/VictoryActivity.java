package nf28.touchmaze.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import nf28.touchmaze.R;

public class VictoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        Animation shakeAnim = AnimationUtils.loadAnimation(VictoryActivity.this, R.anim.shake);
        findViewById(R.id.main_layout_victory).startAnimation(shakeAnim);
    }
}
