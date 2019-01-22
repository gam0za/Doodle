package com.gamza.jinyoungkim.doodle.view.doodle_other.other_single;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySinglelineBinding;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_other.other_single.OtherSingleAdapter;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherModel;

import java.util.ArrayList;

public class OtherSingleActivity extends AppCompatActivity {

    ActivitySinglelineBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<OtherModel> otherSingleList;
    OtherSingleAdapter adapter;
    RequestManager requestManager;
    int position;
    String profile;
    String nickname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_singleline);
        requestManager = Glide.with(this);

        Intent i = getIntent();
        otherSingleList = (ArrayList<OtherModel>)i.getSerializableExtra("otherlist");
        position = i.getIntExtra("position",0);
        Log.e("position: ",String.valueOf(position));
        profile = i.getStringExtra("profile");
        nickname = i.getStringExtra("nickname");

        if(profile==null){
            requestManager.load(R.drawable.profile).into(binding.singlelineImgProfile);
        }else{
            requestManager.load(profile).into(binding.singlelineImgProfile);
        }
        binding.singlelineTextNickname.setText(nickname);


        recyclerView = binding.singlelineRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        binding.singlelineTextTitle.setText("");
        adapter = new OtherSingleAdapter(otherSingleList,getApplicationContext(),requestManager);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.getCompositeDisposable().dispose();
    }
}
