package biz.burli.trump;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujiyuu75.sequent.Sequent;

import pl.droidsonroids.gif.GifTextView;

public class Splash extends AppCompatActivity {

    /* start activity, splash screen with logo ... */
    GifTextView gif;
    RelativeLayout xy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView title = (TextView) findViewById(R.id.trump_text);
        gif = (GifTextView) findViewById(R.id.gifTextView);
        xy = (RelativeLayout) findViewById(R.id.text);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // fadein:
        Sequent
                .origin(xy)
                .duration(300) // option.
                .start();

        changeStatusBarColor();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gif.setVisibility(View.VISIBLE);
            }
        }, 2*1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, 6*1000);
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}