package com.gamza.jinyoungkim.doodle.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.gamza.jinyoungkim.doodle.R;
public class LogoDialog extends Dialog {
    ImageView logo;
    public LogoDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x99464D56));
        setContentView(R.layout.dialog_logo);
        logo = (ImageView)findViewById(R.id.dialog_img_logo);

        TranslateAnimation anim = new TranslateAnimation
                (0,   // fromXDelta
                        0,  // toXDelta
                        -30,    // fromYDelta
                        100);// toYDelta
        anim.setDuration(2000);
        logo.startAnimation(anim);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @SuppressLint("NewApi")
            @Override
            public void run() {
                dismiss();
            }
        }, 1000);// 1 초

    }
}
