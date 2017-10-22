package io.github.livethread.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.github.livethread.R;

/**
 * Activity to hold the About Stuff.
 */
public class AboutActivity extends AppCompatActivity {
    private Animation spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_post);
        toolbar.setTitle("About");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // animation
        spin = AnimationUtils.loadAnimation(this, R.anim.spin_accel);
        findViewById(R.id.iv_logo).startAnimation(spin);
    }

    public void animate(View view) {
        findViewById(R.id.iv_logo).startAnimation(spin);
    }
}
