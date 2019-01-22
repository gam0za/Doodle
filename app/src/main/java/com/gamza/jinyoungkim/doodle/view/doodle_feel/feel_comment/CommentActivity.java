package com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment.CommentAdapter;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment.CommentModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment.CommentResponse;
import com.gamza.jinyoungkim.doodle.databinding.ActivityCommentBinding;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NetworkService networkService;
    ArrayList<CommentModel> commentItem;
    ArrayList<CommentModel> commentList;
    CommentAdapter adapter;
    RequestManager requestManager;
    CommentPost commentPost;
    CompositeDisposable compositeDisposable;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
        idx = i.getIntExtra("idx",0);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);

        recyclerView = binding.commentRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        compositeDisposable = new CompositeDisposable();
        getComment(SharedPreferenceController.getToken(getApplicationContext()),idx);


        binding.commentLayoutPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.commentEditContent.getText().toString().equals("")) {
                    commentPost = new CommentPost(binding.commentEditContent.getText().toString());
                    postComment(SharedPreferenceController.getToken(getApplicationContext()),idx,commentPost);
                }
            }
        });


    }
    public void postComment(String token, int idx, CommentPost commentPost){
        DisposableObserver<Response<CommentPostResponse>>postcommentObserver = new DisposableObserver<Response<CommentPostResponse>>() {
            @Override
            public void onNext(Response<CommentPostResponse> commentPostResponseResponse) {
                if(commentPostResponseResponse.isSuccessful()){
                    binding.commentEditContent.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.commentEditContent.getWindowToken(), 0);

                    getComment(SharedPreferenceController.getToken(getApplicationContext()),idx);
                    adapter.update(commentList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(postcommentObserver);
        DoodleApi.postcomment(token, idx, commentPost).subscribeWith(postcommentObserver);
    }

    public void getComment(String token, int idx){
        this.idx = idx;
        DisposableObserver<Response<CommentResponse>>getcommentObserver = new DisposableObserver<Response<CommentResponse>>() {
            @Override
            public void onNext(Response<CommentResponse> commentResponseResponse) {
                if(commentResponseResponse.isSuccessful()){
                    if(commentResponseResponse.body().result.doodle.image==null){
                        requestManager.load(R.drawable.profile).into(binding.commentImgWriter);
                    }else{
                        requestManager.load(commentResponseResponse.body().result.doodle.image).into(binding.commentImgWriter);
                    }
                    binding.commentTextWriter.setText(commentResponseResponse.body().result.doodle.nickname);
                    binding.commentTextLikecount.setText(String.valueOf(commentResponseResponse.body().result.doodle.like_count));
                    binding.commentTextCommentcount.setText(String.valueOf(commentResponseResponse.body().result.doodle.comment_count));
                    binding.commentTextScrapcount.setText(String.valueOf(commentResponseResponse.body().result.doodle.scrap_count));
                    binding.commentTextDate.setText(commentResponseResponse.body().result.doodle.created);
                    commentItem = new ArrayList<>();
                    commentList = new ArrayList<>();
                    commentItem = commentResponseResponse.body().result.comments;
                    for(int i=0;i<commentItem.size();i++){
                        commentList.add(new CommentModel(commentItem.get(i).idx,commentItem.get(i).content,commentItem.get(i).created,
                                commentItem.get(i).updated,commentItem.get(i).user_idx,commentItem.get(i).doodle_idx,
                                commentItem.get(i).is_read,commentItem.get(i).nickname,commentItem.get(i).profile));
                    }
                    adapter = new CommentAdapter(commentList,getApplicationContext(),requestManager);
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
        compositeDisposable.add(getcommentObserver);
        DoodleApi.getcomment(token, idx).subscribeWith(getcommentObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
