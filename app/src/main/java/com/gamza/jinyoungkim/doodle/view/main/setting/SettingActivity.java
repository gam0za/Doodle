package com.gamza.jinyoungkim.doodle.view.main.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySettingBinding;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.signin.SigninActivity;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        // logout
        binding.settingTextLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceController.setToken(getApplicationContext(),"");
                SharedPreferenceController.setEmail(getApplicationContext(),"");
                SharedPreferenceController.setPw(getApplicationContext(),"");
                SharedPreferenceController.setNickname(getApplicationContext(),"");
                SharedPreferenceController.setProfile(getApplicationContext(),"");
                Intent i = new Intent(getApplicationContext(),SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        // send email
        binding.settingTextSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SendemailActivity.class);
                startActivity(i);
            }
        });

        // law
        binding.settingTextLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LawActivity.class);
                startActivity(i);
            }
        });

        // license
        binding.settingTextLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LicenseActivity.class);
                startActivity(i);
            }
        });
    }
}
