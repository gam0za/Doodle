package com.gamza.jinyoungkim.doodle.view.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmResponse;
import com.gamza.jinyoungkim.doodle.databinding.ActivityMainBinding;
import com.gamza.jinyoungkim.doodle.network.AlarmApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.BackPressCloseHandler;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_book.BookActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.MyfeedActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_write.WriteActivity;
import com.gamza.jinyoungkim.doodle.view.main.alarm.AlarmActivity;
import com.gamza.jinyoungkim.doodle.view.main.setting.SettingActivity;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    BackPressCloseHandler backPressCloseHandler;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewinit();
        backPressCloseHandler = new BackPressCloseHandler(this);

        // menu
        binding.mainLayoutWrite.setOnClickListener(this);
        binding.mainLayoutFeel.setOnClickListener(this);
        binding.mainLayoutMypage.setOnClickListener(this);
        binding.mainLayoutBook.setOnClickListener(this);

        // setting
        binding.mainImgSetting.setOnClickListener(this);

        // alarm
        binding.mainImgAlarm.setOnClickListener(this);

        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        getalarm(SharedPreferenceController.getToken(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewinit();
        getalarm(SharedPreferenceController.getToken(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void viewinit(){
        binding.mainLayoutWrite.setBackgroundResource(R.drawable.writeoff);
        binding.mainLayoutFeel.setBackgroundResource(R.drawable.feeloff);
        binding.mainLayoutMypage.setBackgroundResource(R.drawable.mypageoff);
        binding.mainLayoutBook.setBackgroundResource(R.drawable.bookoff);
    }

    @Override
    public void onClick(View v) {
        if(v==binding.mainLayoutWrite)
        {
            binding.mainLayoutWrite.setBackgroundResource(R.drawable.writeon);
            binding.mainLayoutFeel.setBackgroundResource(R.drawable.feeloff);
            binding.mainLayoutMypage.setBackgroundResource(R.drawable.mypageoff);
            binding.mainLayoutBook.setBackgroundResource(R.drawable.bookoff);

            Intent i = new Intent(getApplicationContext(),WriteActivity.class);
            startActivity(i);

        }
        else if(v==binding.mainLayoutFeel)
        {
            binding.mainLayoutWrite.setBackgroundResource(R.drawable.writeoff);
            binding.mainLayoutFeel.setBackgroundResource(R.drawable.feelon);
            binding.mainLayoutMypage.setBackgroundResource(R.drawable.mypageoff);
            binding.mainLayoutBook.setBackgroundResource(R.drawable.bookoff);

            Intent i = new Intent(getApplicationContext(),FeelActivity.class);
            startActivity(i);
        }
        else if(v==binding.mainLayoutMypage)
        {
            binding.mainLayoutWrite.setBackgroundResource(R.drawable.writeoff);
            binding.mainLayoutFeel.setBackgroundResource(R.drawable.feeloff);
            binding.mainLayoutMypage.setBackgroundResource(R.drawable.mypageon);
            binding.mainLayoutBook.setBackgroundResource(R.drawable.bookoff);

            Intent i = new Intent(getApplicationContext(),MyfeedActivity.class);
            startActivity(i);
        }
        else if(v==binding.mainLayoutBook)
        {
            binding.mainLayoutWrite.setBackgroundResource(R.drawable.writeoff);
            binding.mainLayoutFeel.setBackgroundResource(R.drawable.feeloff);
            binding.mainLayoutMypage.setBackgroundResource(R.drawable.mypageoff);
            binding.mainLayoutBook.setBackgroundResource(R.drawable.bookon);

            Intent i = new Intent(getApplicationContext(),BookActivity.class);
            startActivity(i);
        }
        else if(v==binding.mainImgSetting)
        {
            Intent i = new Intent(getApplicationContext(),SettingActivity.class);
            startActivity(i);
        }
        else if(v==binding.mainImgAlarm){
            Intent i = new Intent(getApplicationContext(),AlarmActivity.class);
            startActivity(i);
        }
    }

    public void getalarm(String token){
        DisposableObserver<Response<AlarmResponse>>getalarmObserver = new DisposableObserver<Response<AlarmResponse>>() {
            @Override
            public void onNext(Response<AlarmResponse> alarmResponseResponse) {
                if(alarmResponseResponse.isSuccessful()){
                    if (alarmResponseResponse.body().result.not_read==null){
                        binding.noticeNew.setVisibility(View.GONE);
                    }else{
                        if(alarmResponseResponse.body().result.not_read.size()==0){
                            binding.noticeNew.setVisibility(View.GONE);
                        }else{
                            binding.noticeNew.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AlarmApi.getalarm(token).subscribeWith(getalarmObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
