package biz.burli.trump;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView tv_number, tv_days;
    AlphaAnimation blinkanimation;
    ImageView iv_trump;
    int counter;
    boolean addieren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_days = (TextView) findViewById(R.id.tv_days);
        iv_trump = (ImageView) findViewById(R.id.iv_flag);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();


        counter = pref.getInt("counter", 3); // getting Integer
        Log.d("count", String.valueOf(counter));

        blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(500); // duration
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(100000); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);

        final MediaPlayer mp = new MediaPlayer();
        try {
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            AssetFileDescriptor afd = getAssets().openFd("trumpsong.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "manteka.ttf");
        tv_number.setTypeface(face);
        tv_days.setTypeface(face);

        Date currentDate = new Date(System.currentTimeMillis());
        Date endDate = new Date();
        endDate.setYear(121);   // von 1900 gerechnet
        endDate.setMonth(0);
        endDate.setDate(21);

        final int difference = ((int) ((endDate.getTime() / (24 * 60 * 60 * 1000))
                - (int) (currentDate.getTime() / (24 * 60 * 60 * 1000))));
        Log.d("current", String.valueOf(currentDate));
        Log.d("end", String.valueOf(endDate));
        tv_number.setText(String.valueOf(difference));

        addieren = pref.getBoolean("addieren", false);
        Log.d("addieren", String.valueOf(addieren));
        if (addieren == true) {
            int xy =  Integer.parseInt(tv_number.getText().toString());
            xy = xy + (365+365+365+366);
            Log.d("xy", String.valueOf(xy));
            tv_number.setText(String.valueOf(xy));
        } else {
            // kein vertrauen
        };

        tv_number.setAnimation(blinkanimation);
        iv_trump.setImageDrawable(getResources().getDrawable(R.drawable.blink));

        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.trumpflag2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.trumpflag2silent), 500);
        animation.setOneShot(false);

        iv_trump.setBackgroundDrawable(animation);

        // start the animation!
        animation.start();

        final Dialog dialog = new Dialog(this);
        if (counter == 3) {
            counter = 1;
            editor.putInt("counter", counter); // Storing integer
            editor.commit(); // commit changes
            dialog.setContentView(R.layout.dialog_trust);
            dialog.setTitle("Hey you, we have to talk");
            Button dialogButtonNo = (Button) dialog.findViewById(R.id.btn_no);
            Button dialogButtonYes = (Button) dialog.findViewById(R.id.btn_yes);
            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    editor.putBoolean("addieren", true); // Storing integer
                    editor.commit();
                    int sa = difference + (365+365+365+366);
                    tv_number.setText(String.valueOf(sa));
                }
            });

            dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    editor.putBoolean("addieren", false); // Storing integer
                    editor.commit();
                    int sa = difference;
                    tv_number.setText(String.valueOf(sa));
                }
            });

            dialog.show();
        } else {
            counter++;
            editor.putInt("counter", counter); // Storing integer
            editor.commit(); // commit changes
            // no showing, nur beim 5 mal -> def init = 5, d.h. beinm erstmaligen öffnen auch angezeigt
        };




    }

}

