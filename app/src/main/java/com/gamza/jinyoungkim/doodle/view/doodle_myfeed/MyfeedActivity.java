package com.gamza.jinyoungkim.doodle.view.doodle_myfeed;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed.MyfeedAdapter;
import com.gamza.jinyoungkim.doodle.databinding.ActivityMyfeedBinding;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.UserApi;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.RecyclerViewItemDeco;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelModel;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelPost;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelResponse;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.*;

public class MyfeedActivity extends AppCompatActivity {

    ActivityMyfeedBinding binding;
    RecyclerView recyclerView;
    LayoutManager layoutManager;
    NetworkService networkService;
    ArrayList<FeelModel> myfeedItem;
    ArrayList<FeelModel> myfeedList;
    MyfeedAdapter adapter;
    FeelPost myfeedPost;
    RequestManager requestManager;
    CompositeDisposable compositeDisposable;
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

            getlist(flag);
            adapter.update(myfeedList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_myfeed);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        compositeDisposable = new CompositeDisposable();

        // get profile
        getprofile();

        recyclerView = binding.myfeedRecyclerview;
        layoutManager = new GridLayoutManager(this, 2);
        ((GridLayoutManager) layoutManager).setOrientation(GridLayoutManager.VERTICAL);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDeco(2, convertDpToPixel(2), true));
        getlist(3);
        flag=3;

        binding.myfeedLayoutMyfeedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlist(3);
                flag=3;
                selected(true, binding.myfeedLayoutMyfeedTitle, binding.myfeedImgMyfeedTitle, binding.myfeedTextMyfeedTitle);
                selected(false, binding.myfeedLayoutScrapTitle, binding.myfeedImgScrapTitle, binding.myfeedTextScrapTitle);
            }
        });

        binding.myfeedLayoutScrapTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myfeedLayoutNull.setVisibility(View.GONE);
                getlist(4);
                flag=4;
                selected(false, binding.myfeedLayoutMyfeedTitle, binding.myfeedImgMyfeedTitle, binding.myfeedTextMyfeedTitle);
                selected(true, binding.myfeedLayoutScrapTitle, binding.myfeedImgScrapTitle, binding.myfeedTextScrapTitle);
            }
        });

        binding.myfeedTextGoprofile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                i.putExtra("description",binding.myfeedTextInfo.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }


    public void getlist(int flag){
        myfeedPost = new FeelPost(flag);
        DisposableObserver<Response<FeelResponse>> getlistObserver = new DisposableObserver<Response<FeelResponse>>() {
            @Override
            public void onNext(Response<FeelResponse> feelResponseResponse) {
                if(feelResponseResponse.isSuccessful()){
                    myfeedItem = new ArrayList<>();
                    myfeedList = new ArrayList<>();
                    myfeedItem = feelResponseResponse.body().result;
                    for (int i = 0; i < myfeedItem.size(); i++) {
                        myfeedList.add(new FeelModel(myfeedItem.get(i).image, myfeedItem.get(i).text, myfeedItem.get(i).idx,
                                myfeedItem.get(i).comment_count, myfeedItem.get(i).scrap_count, myfeedItem.get(i).like_count,
                                myfeedItem.get(i).user_idx, myfeedItem.get(i).nickname, myfeedItem.get(i).profile,
                                myfeedItem.get(i).scraps, myfeedItem.get(i).like, myfeedItem.get(i).created));

                    }
                }
                if (myfeedList!=null) {
                    binding.myfeedLayoutNull.setVisibility(View.GONE);
                    Log.e("myfeedList", "게시물 생김");
                } else if (flag == 3) {
                    binding.myfeedLayoutNull.setVisibility(View.VISIBLE);
                } else {
                    binding.myfeedLayoutNull.setVisibility(View.VISIBLE);
                }
                adapter = new MyfeedAdapter(flag, myfeedList, getApplicationContext(), requestManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(getlistObserver);
        DoodleApi.doodlelist(SharedPreferenceController.getToken(getApplicationContext()),myfeedPost).subscribeWith(getlistObserver);
    }

    public void getprofile(){
        DisposableObserver<Response<MyfeedProfileResponse>> myprofileObserver = new DisposableObserver<Response<MyfeedProfileResponse>>() {
            @Override
            public void onNext(Response<MyfeedProfileResponse> myfeedProfileResponseResponse) {
                if(myfeedProfileResponseResponse.isSuccessful()){
                    if (myfeedProfileResponseResponse.body().result.image==null){
                        requestManager.load(R.drawable.profile).into(binding.myfeedImgProfile);
                    }else{
                        requestManager.load(myfeedProfileResponseResponse.body().result.image).into(binding.myfeedImgProfile);
                    }
                    if(myfeedProfileResponseResponse.body().result.description==null){
                        binding.myfeedTextInfo.setText("작은 끄적임이 글이되다");
                    }else{
                        binding.myfeedTextInfo.setText(myfeedProfileResponseResponse.body().result.description);
                    }
                    binding.myfeedTextNickname.setText(myfeedProfileResponseResponse.body().result.nickname);
                    binding.myfeedTextWritecount.setText(String.valueOf(myfeedProfileResponseResponse.body().result.doodle_count));
                    binding.myfeedTextScrapcount.setText(String.valueOf(myfeedProfileResponseResponse.body().result.scrap_count));
                }
            }

            @Override
            public void onError(Throwable e) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :)");
            }

            @Override
            public void onComplete() {

            }
        };

        compositeDisposable.add(myprofileObserver);
        UserApi.myprofile(SharedPreferenceController.getToken(getApplicationContext())).subscribeWith(myprofileObserver);

    }


    public void selected(boolean select, RelativeLayout layout, ImageView image, TextView textView){

        if(select==true){
            layout.setBackgroundColor(Color.parseColor("#464D56"));
            image.setVisibility(View.VISIBLE);
            textView.setTextColor(Color.parseColor("#ffffff"));
        }else if(select==false){
            layout.setBackgroundColor(Color.parseColor("#C8CAC8"));
            image.setVisibility(View.GONE);
            textView.setTextColor(Color.parseColor("#A1A2A1"));
        }
    }

    public float convertDpToPixel(int dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp*(metrics.densityDpi/160f);
        return px;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.dispose();
    }
}


