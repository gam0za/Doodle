package com.gamza.jinyoungkim.doodle.view.doodle_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikeModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikePost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapPost;
import com.gamza.jinyoungkim.doodle.databinding.ActivityDetailBinding;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    int detail_idx;
    NetworkService networkService;
    RequestManager requestManager;
    private Typeface tf, tf_regular;
    LikePost likePost;
    ScrapPost scrapPost;
    DetailModel detailModel;
    CompositeDisposable compositeDisposable;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        detail_idx = i.getIntExtra("detail_idx",0);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        compositeDisposable = new CompositeDisposable();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        tf = getResources().getFont(R.font.nanum_bold);
        tf_regular = getResources().getFont(R.font.nanum);
        getdetail(SharedPreferenceController.getToken(getApplicationContext()),detail_idx);

        binding.detailTextLike.setOnClickListener(new View.OnClickListener() {
            String likeString="";
            @Override
            public void onClick(View v) {
                if(detailModel.like!=0){
                    likeString = "unlike";
                }else{
                    likeString="like";
                }
                likePost = new LikePost(likeString);
                doodleLike(SharedPreferenceController.getToken(getApplicationContext()),likePost,detail_idx);
            }
        });
        binding.detailTextScrap.setOnClickListener(new View.OnClickListener() {
            String scrapString="";
            @Override
            public void onClick(View v) {
                if(detailModel.scraps!=0){
                    scrapString="unscrap";
                }else{
                    scrapString="scrap";
                }
                scrapPost = new ScrapPost(scrapString);
                doodleScrap(SharedPreferenceController.getToken(getApplicationContext()),scrapPost,detailModel.idx);
            }
        });
        binding.detailTextComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CommentActivity.class);
                i.putExtra("idx",detailModel.idx);
                startActivity(i);
            }
        });

        binding.detailTextWriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),OtherActivity.class);
                i.putExtra("user_idx",detailModel.user_idx);
                startActivity(i);
            }
        });
    }

    public void doodleLike(String token, LikePost likePost, int idx){
        DisposableObserver<Response<LikeModel>> likeObserver = new DisposableObserver<Response<LikeModel>>() {
            @Override
            public void onNext(Response<LikeModel> likeModelResponse) {
                if(likeModelResponse.isSuccessful()){
                    if(detailModel.like!=0){
                        detailModel.like=0;
                        setRegular(binding.detailTextLike);
                    }else{
                        detailModel.like = detailModel.idx;
                        setBold(binding.detailTextLike);
                    }
                    detailModel.like_count=likeModelResponse.body().result.count;
                    binding.detailTextLikecount.setText(String.valueOf(likeModelResponse.body().result.count));

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(likeObserver);
        DoodleApi.like(token, likePost, idx).subscribeWith(likeObserver);
    }

    public void doodleScrap(String token, ScrapPost scrapPost, int idx){
        DisposableObserver<Response<ScrapModel>> scrapObserver = new DisposableObserver<Response<ScrapModel>>() {
            @Override
            public void onNext(Response<ScrapModel> scrapModelResponse) {
                if(scrapModelResponse.isSuccessful()){
                    if(detailModel.scraps!=0){
                        detailModel.scraps=0;
                        setRegular(binding.detailTextScrap);
                    }else{
                        detailModel.scraps = detailModel.idx;
                        Toast toast = Toast.makeText(getApplicationContext(),"글 작성이 완료되었습니다",Toast.LENGTH_SHORT);
                        LayoutInflater inflater = getLayoutInflater();
                        View v = inflater.inflate(R.layout.toast_confirm,(ViewGroup)findViewById(R.id.toast_confirm_layout));
                        TextView t = v.findViewById(R.id.toast_confirm_text);
                        t.setText("글을 담았습니다");
                        toast.setView(v);
                        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                        toast.show();
                        setBold(binding.detailTextScrap);
                    }
                    detailModel.scrap_count = scrapModelResponse.body().result.count;
                    binding.detailTextScrapcount.setText(String.valueOf(scrapModelResponse.body().result.count));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(scrapObserver);
        DoodleApi.scrap(token, scrapPost, idx).subscribeWith(scrapObserver);
    }

    public void getdetail(String token, int idx){
        DisposableObserver<Response<DetailResponse>>getdetailObserver = new DisposableObserver<Response<DetailResponse>>() {
            @Override
            public void onNext(Response<DetailResponse> detailResponseResponse) {
                if(detailResponseResponse.isSuccessful()){
                    detailModel = detailResponseResponse.body().result;

                    requestManager.load(detailResponseResponse.body().result.image).into(binding.detailImgImage);

                    if(detailResponseResponse.body().result.like!=0){
                        setBold(binding.detailTextLike);
                    }else {
                        setRegular(binding.detailTextLike);
                    }

                    binding.detailTextLikecount.setText(String.valueOf(detailResponseResponse.body().result.like_count));
                    binding.detailTextCommentcount.setText(String.valueOf(detailResponseResponse.body().result.comment_count));

                    if(detailResponseResponse.body().result.scraps!=0){
                        setBold(binding.detailTextScrap);
                    }else{
                        setRegular(binding.detailTextScrap);
                    }

                    binding.detailTextScrapcount.setText(String.valueOf(detailResponseResponse.body().result.scrap_count));
                    binding.detailTextWriter.setText(detailResponseResponse.body().result.nickname);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(getdetailObserver);
        DoodleApi.getdetail(token, idx).subscribeWith(getdetailObserver);
    }


    public void setRegular(TextView textView){
        textView.setTextColor(Color.parseColor("#80000000"));
        textView.setTypeface(tf_regular);
    }

    public void setBold(TextView textView){
        textView.setTextColor(Color.parseColor("#464D56"));
        textView.setTypeface(tf);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
