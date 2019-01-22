package com.gamza.jinyoungkim.doodle.view.doodle_book;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivityBookBinding;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;

public class BookActivity extends AppCompatActivity {

    ActivityBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_book);

        binding.bookLayoutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalApplication.getGlobalApplicationContext().makeToast("준비중입니다 :)");
            }
        });
    }
}
