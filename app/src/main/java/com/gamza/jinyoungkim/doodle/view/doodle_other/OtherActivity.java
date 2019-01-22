package com.gamza.jinyoungkim.doodle.view.doodle_other;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_other.OtherAdapter;
import com.gamza.jinyoungkim.doodle.databinding.ActivityOtherBinding;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.RecyclerViewItemDeco;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class OtherActivity extends AppCompatActivity {

    ActivityOtherBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NetworkService networkService;
    ArrayList<OtherModel> otherItem;
    ArrayList<OtherModel> otherList;
    OtherAdapter adapter;
    RequestManager requestManager;
    int user_idx;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_other);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        Intent i = getIntent();
        user_idx = i.getIntExtra("user_idx",56);
        Log.e("user_idx",String.valueOf(user_idx));

        recyclerView = binding.otherRecyclerview;
        layoutManager = new GridLayoutManager(this,2);
        ((GridLayoutManager) layoutManager).setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDeco(2, convertDpToPixel(2), true));
        getother(SharedPreferenceController.getToken(getApplicationContext()),user_idx);

    }

    public void getother(String token, int idx){
        DisposableObserver<Response<OtherResponse>>otherObserver = new DisposableObserver<Response<OtherResponse>>() {
            @Override
            public void onNext(Response<OtherResponse> otherResponseResponse) {
                if(otherResponseResponse.isSuccessful()){
                    otherItem = new ArrayList<>();
                    otherList = new ArrayList<>();
                    otherItem = otherResponseResponse.body().result.doodle;

                    binding.otherTextNickname.setText(otherResponseResponse.body().result.user.nickname);
                    if(otherResponseResponse.body().result.user.profile==null){
                        requestManager.load(R.drawable.profile).into(binding.otherImgProfile);
                    }else{
                        requestManager.load(otherResponseResponse.body().result.user.profile).into(binding.otherImgProfile);
                    }

                    binding.otherTextInfo.setText(otherResponseResponse.body().result.user.description);

                    binding.otherTextScrapcount.setText(String.valueOf(otherResponseResponse.body().result.user.scrap_count));
                    binding.otherTextWritecount.setText(String.valueOf(otherResponseResponse.body().result.user.doodle_count));

                    for(int i=0;i<otherItem.size();i++){
                        otherList.add(new OtherModel(otherItem.get(i).idx,otherItem.get(i).text,otherItem.get(i).image,
                                otherItem.get(i).comment_count,otherItem.get(i).scrap_count,otherItem.get(i).like_count,
                                otherItem.get(i).created,otherItem.get(i).updated,otherItem.get(i).user_idx,otherItem.get(i).scraps,
                                otherItem.get(i).like));
                    }
                    adapter = new OtherAdapter(otherList,getApplicationContext(),requestManager,otherResponseResponse.body().result.user.profile,otherResponseResponse.body().result.user.nickname);
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
        DoodleApi.getother(token, idx).subscribeWith(otherObserver);
    }

    public float convertDpToPixel(int dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp*(metrics.densityDpi/160f);
        return px;
    }
}
