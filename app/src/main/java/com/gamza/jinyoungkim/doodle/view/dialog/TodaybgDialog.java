package com.gamza.jinyoungkim.doodle.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.gamza.jinyoungkim.doodle.R;


public class TodaybgDialog extends Dialog {

    private Context context;
    private LinearLayout layout;

    public TodaybgDialog(final Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x99464D56));
        setContentView(R.layout.dialog_todaybg);
        layout = (LinearLayout)findViewById(R.id.dialog_todaybg_text);
        final Animation fade = AnimationUtils.loadAnimation(context, R.anim.fadeout);
        layout.startAnimation(fade);

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
