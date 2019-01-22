package com.gamza.jinyoungkim.doodle.view.main.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySendemailBinding;

public class SendemailActivity extends AppCompatActivity {

    ActivitySendemailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sendemail);

        binding.sendemailImgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                finish();
            }
        });
    }

    public void sendEmail(){
        try{
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            // email setting 배열로 해놔서 복수 발송 가능
            String[] address = {"devgamza@gmail.com"};
            email.putExtra(Intent.EXTRA_EMAIL, address);
            email.putExtra(Intent.EXTRA_SUBJECT,"글적 의견보내기 from "+binding.sendemailEditEmail.getText().toString());
            email.putExtra(Intent.EXTRA_TEXT,binding.sendemailEditContent.getText().toString());
            startActivity(email);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
