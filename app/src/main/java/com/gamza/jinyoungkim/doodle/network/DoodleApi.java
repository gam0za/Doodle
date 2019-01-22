package com.gamza.jinyoungkim.doodle.network;

import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikeModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikePost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapPost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment.CommentResponse;
import com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed.myfeed_single.DeleteResponse;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.view.doodle_detail.DetailResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelPost;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentPost;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentPostResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_write.WriteResponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class DoodleApi {

    public static Observable<Response<WriteResponse>> write(String token, RequestBody text, MultipartBody.Part image){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.doodlepost(token, text, image)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<FeelResponse>> doodlelist(String token, FeelPost feelPost){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.doodlelist(token, feelPost)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<LikeModel>> like(String token, LikePost likePost, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.like(token,likePost,idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<ScrapModel>> scrap(String token, ScrapPost scrapPost, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.scrap(token, scrapPost, idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<CommentResponse>>getcomment(String token, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.getcomment(token, idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<CommentPostResponse>>postcomment(String token, int idx, CommentPost commentPost){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.postcomment(token, idx, commentPost)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<DetailResponse>>getdetail(String token, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.getdetail(token, idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<DeleteResponse>>delete(String token, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.delete(token, idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<OtherResponse>>getother(String token, int idx){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.getother(token, idx)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
