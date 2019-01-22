package com.gamza.jinyoungkim.doodle.view.doodle_myfeed.myfeed_single;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed.myfeed_single.MyfeedSingleAdapter;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySinglelineBinding;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelModel;

import java.util.ArrayList;

public class SingleLineActivity extends AppCompatActivity {

    ActivitySinglelineBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<FeelModel> myfeedSingleList;
    MyfeedSingleAdapter adapter;
    RequestManager requestManager;
    int position;
    String nickname;
    String profile;
    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_singleline);
        requestManager = Glide.with(this);

        Intent i = getIntent();
        flag = i.getIntExtra("flag",3);
        myfeedSingleList = (ArrayList<FeelModel>)i.getSerializableExtra("feedlist");
        position=i.getIntExtra("position",0);
        profile = i.getStringExtra("profile");
        nickname = i.getStringExtra("nickname");

        binding.singlelineTextNickname.setText(nickname);
        requestManager.load(profile).into(binding.singlelineImgProfile);

        if(flag==3){
            binding.singlelineLayoutTop.setVisibility(View.VISIBLE);
        }else if(flag==4){
            binding.singlelineTextTitle.setText("담은 글");
            binding.singlelineLayoutTop.setVisibility(View.GONE);
        }

        recyclerView = binding.singlelineRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.e("position",String.valueOf(position));
        adapter = new MyfeedSingleAdapter(flag,myfeedSingleList,getApplicationContext(),requestManager);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(position);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.getCompositeDisposable().dispose();
    }
}
