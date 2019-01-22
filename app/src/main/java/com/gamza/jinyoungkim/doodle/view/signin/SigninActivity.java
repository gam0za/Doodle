package com.gamza.jinyoungkim.doodle.view.signin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySigninBinding;
import com.gamza.jinyoungkim.doodle.util.BackPressCloseHandler;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.main.MainActivity;
import com.gamza.jinyoungkim.doodle.view.signup.SignupActivity;

public class SigninActivity extends AppCompatActivity implements SigninInterface.View {

    ActivitySigninBinding binding;
    SigninPresenter presenter;
    BackPressCloseHandler backPressCloseHandler;
    String email="";
    String pw="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_signin);
        presenter = new SigninPresenter(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        if(email!=null){
            email = email.replaceAll(" ","");
        }

        pw = i.getStringExtra("pw");
        if(pw!=null){
            pw = pw.replaceAll(" ","");
        }

        binding.signinEditEmail.setText(email);
        binding.signinEditPw.setText(pw);

        binding.signinImgConfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.signinEditEmail.getText().equals("")||binding.signinEditPw.getText().equals("")){
                    GlobalApplication.getGlobalApplicationContext().makeToast("이메일과 비밀번호를 모두 입력해주세요 :)");
                }else{
                    presenter.setSigninPost(binding.signinEditEmail.getText().toString(),binding.signinEditPw.getText().toString(),getApplicationContext());
                    Log.e("token: ",SharedPreferenceController.getToken(getApplicationContext()));
                    Log.e("email: ",SharedPreferenceController.getEmail(getApplicationContext()));
                    Log.e("nickname: ",SharedPreferenceController.getNickname(getApplicationContext()));
                    Log.e("profile: ",SharedPreferenceController.getProfile(getApplicationContext()));
                }
            }
        });

        binding.signinTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void login(String result) {
        if(result.equals("success")){
            Toast toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.toast_confirm,(ViewGroup)findViewById(R.id.toast_confirm_layout));
            TextView t = v.findViewById(R.id.toast_confirm_text);
            t.setText(SharedPreferenceController.getNickname(getApplicationContext())+"님 반갑습니다 :)");
            toast.setView(v);
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
            toast.show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();

        }
    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
