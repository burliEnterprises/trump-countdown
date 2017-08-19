package biz.burli.trump;

import android.app.Dialog;
import android.content.Intent;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujiyuu75.sequent.Sequent;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView tv_number, tv_days;
    AlphaAnimation blinkanimation;
    ImageView iv_trump, iv_share;
    int counter, audio;
    boolean addieren;
    RelativeLayout main;
    MediaPlayer mp;
    ImageView iv_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get views and elements:
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_days = (TextView) findViewById(R.id.tv_days);
        main = (RelativeLayout) findViewById(R.id.activity_main);
        iv_audio = (ImageView) findViewById(R.id.audio);
        iv_share = (ImageView) findViewById(R.id.share);

        // shared preferences loading:
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        counter = pref.getInt("counter", 3); // getting Integer
        audio = pref.getInt("audio", 1);    // eingeschalten 1, aus 0
        Log.d("count", String.valueOf(counter));

        // set sounds and audio:
        mp = new MediaPlayer();
        try {
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            AssetFileDescriptor afd = getAssets().openFd("trumpsong.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            if (audio == 1) {
                mp.start();
            } else {
                iv_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_grey_700_24dp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // activity & fragment handling:
        iv_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                SharedPreferences tmp_pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                audio = tmp_pref.getInt("audio", 1);    // eingeschalten 1, aus 0
                if (audio == 1) {
                    editor.putInt("audio", 0); // Storing integer
                    editor.commit(); // commit changes
                    iv_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_grey_700_24dp));
                    mp.pause();
                } else {
                    editor.putInt("audio", 1); // Storing integer
                    editor.commit(); // commit changes
                    iv_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_grey_700_24dp));
                    mp.start();
                }
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, tv_number.getText().toString() + " days of trump's term are left.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        // set typefaces:
        Typeface face = Typeface.createFromAsset(getAssets(), "manteka.ttf");
        tv_number.setTypeface(face);
        tv_days.setTypeface(face);

        // calculate days till end:
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


        // set animations:
        blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(500); // duration
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(100000); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        tv_number.setAnimation(blinkanimation);
        Sequent
                .origin(main)
                .duration(1000) // option.
                .start();


        // dialog for second run or not:
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

