package com.gamza.jinyoungkim.doodle.view.doodle_feel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.FeelAdapter;
import com.gamza.jinyoungkim.doodle.databinding.ActivityFeelBinding;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.dialog.LogoDialog;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchActivity;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class FeelActivity extends AppCompatActivity{

    ActivityFeelBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NetworkService networkService;
    ArrayList<FeelModel> feelItem;
    ArrayList<FeelModel> feelList;
    FeelAdapter adapter;
    FeelPost feelPost;
    RequestManager requestManager;

    int flag;
    boolean pause;

    @Override
    protected void onPause() {
        super.onPause();
        pause=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pause==true){
            LogoDialog logoDialog = new LogoDialog(this);
            // Dialog 사이즈 조절 하기
            WindowManager.LayoutParams params = logoDialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            logoDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

            logoDialog.show();
            getlist(flag);
            adapter.update(feelList);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        flag = i.getIntExtra("flag",-1);
        Log.e("flag: ",String.valueOf(flag));

        binding = DataBindingUtil.setContentView(this,R.layout.activity_feel);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        pause = false;
        recyclerView = binding.feelRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getlist(flag);

        if(flag==2){
            binding.feelTextAll.setTextColor(Color.parseColor("#30000000"));
            binding.feelTextWeek.setTextColor(Color.parseColor("#30000000"));
            binding.feelTextToday.setTextColor(Color.parseColor("#464D56"));
        }else{
            binding.feelTextAll.setTextColor(Color.parseColor("#464D56"));
            binding.feelTextWeek.setTextColor(Color.parseColor("#30000000"));
            binding.feelTextToday.setTextColor(Color.parseColor("#30000000"));
        }


        binding.feelTextAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlist(-1);
                setBold(binding.feelTextAll);
                setRegular(binding.feelTextWeek);
                setRegular(binding.feelTextToday);
            }
        });

        binding.feelTextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlist(1);
                setBold(binding.feelTextWeek);
                setRegular(binding.feelTextAll);
                setRegular(binding.feelTextToday);
            }
        });

        binding.feelTextToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlist(2);
                setBold(binding.feelTextToday);
                setRegular(binding.feelTextWeek);
                setRegular(binding.feelTextAll);
            }
        });

        // go search
        binding.feelImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(i);
            }
        });

    }

    public void getlist(int flag){
        this.flag=flag;
        feelPost = new FeelPost(flag);
        DisposableObserver<Response<FeelResponse>> getlistObserver = new DisposableObserver<Response<FeelResponse>>() {
            @Override
            public void onNext(Response<FeelResponse> feelResponseResponse) {
                if(feelResponseResponse.isSuccessful()){
                    feelItem = new ArrayList<>();
                    feelList = new ArrayList<>();
                    feelItem = feelResponseResponse.body().result;
                    for (int i=0;i<feelItem.size();i++){
                        feelList.add(new FeelModel(feelItem.get(i).image,feelItem.get(i).text,feelItem.get(i).idx,
                                feelItem.get(i).comment_count,feelItem.get(i).scrap_count,feelItem.get(i).like_count,
                                feelItem.get(i).user_idx,feelItem.get(i).nickname,feelItem.get(i).profile,
                                feelItem.get(i).scraps,feelItem.get(i).like,feelItem.get(i).created));
                    }
                }
                if(feelResponseResponse.body().result!=null){
                    adapter = new FeelAdapter(flag,feelList,getApplicationContext(),requestManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        DoodleApi.doodlelist(SharedPreferenceController.getToken(getApplicationContext()),feelPost).subscribeWith(getlistObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.getCompositeDisposable().dispose();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();

    }

    public void setBold(TextView v){
        v.setTextColor(Color.parseColor("#464D56"));
    }
    public void setRegular(TextView v){
        v.setTextColor(Color.parseColor("#30000000"));
    }
}
