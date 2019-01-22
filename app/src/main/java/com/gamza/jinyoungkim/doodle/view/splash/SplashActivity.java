package com.gamza.jinyoungkim.doodle.view.splash;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySplashBinding;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.UserApi;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.main.MainActivity;
import com.gamza.jinyoungkim.doodle.view.signin.SigninActivity;
import com.gamza.jinyoungkim.doodle.view.signin.SigninModel;
import com.gamza.jinyoungkim.doodle.view.signin.SigninResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        final Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        binding.splash.startAnimation(fade);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();


        //permission check
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        if(!SharedPreferenceController.getEmail(getApplicationContext()).equals("")){
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
                        }else{
                            Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();

                        }

                    }
                }, 3000);// 2 초
            }

            @Override
            public void onPermissionDenied(ArrayList<String> arrayList) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        if(!SharedPreferenceController.getToken(getApplicationContext()).equals("")){
                            login(SharedPreferenceController.getEmail(getApplicationContext()),SharedPreferenceController.getPw(getApplicationContext()));
                        }else{
                            Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();

                        }

                    }
                }, 3000);// 2 초
            }
        };

        TedPermission tedPermission = new TedPermission(this);
        tedPermission.setPermissionListener(permissionListener)
                     .setDeniedMessage("[설정]>[권한] 에서 추후 허용하실 수 있습니다 :)")
                     .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                     .check();


    }


    public void login(final String email, final String pw){
        SigninModel signinModel = new SigninModel(email,pw,FirebaseInstanceId.getInstance().getToken());
        DisposableObserver<Response<SigninResult>> loginObserver = new DisposableObserver<Response<SigninResult>>() {
            @Override
            public void onNext(Response<SigninResult> signinResultResponse) {
                if(signinResultResponse.isSuccessful()){
                    SharedPreferenceController.setEmail(getApplicationContext(),email);
                    SharedPreferenceController.setPw(getApplicationContext(),pw);
                    SharedPreferenceController.setToken(getApplicationContext(),signinResultResponse.body().result.token);

                    Toast toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
                    LayoutInflater inflater = getLayoutInflater();
                    View v = inflater.inflate(R.layout.toast_confirm,(ViewGroup)findViewById(R.id.toast_confirm_layout));
                    TextView t = v.findViewById(R.id.toast_confirm_text);
                    t.setText(signinResultResponse.body().result.profile.nickname+"님 반갑습니다 :)");
                    toast.setView(v);
                    toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                    toast.show();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        UserApi.login(signinModel).subscribeWith(loginObserver);
    }
}
