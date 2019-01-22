package com.gamza.jinyoungkim.doodle.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gamza.jinyoungkim.doodle.R;

public class MessageDialog extends Dialog {
    TextView textView;
    public MessageDialog(Context context, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x90ffffff));
        setContentView(R.layout.dialog_result);
        textView = (TextView)findViewById(R.id.dialog_result_message);
        final Animation fade = AnimationUtils.loadAnimation(context, R.anim.fadeout);

        textView.setText(message);
        textView.setAnimation(fade);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @SuppressLint("NewApi")
            @Override
            public void run() {
                dismiss();
            }
        }, 500);
    }
}
