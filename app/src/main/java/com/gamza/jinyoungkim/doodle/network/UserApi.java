package com.gamza.jinyoungkim.doodle.network;

import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.MyfeedProfileResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.ProfileResponse;
import com.gamza.jinyoungkim.doodle.view.signin.SigninModel;
import com.gamza.jinyoungkim.doodle.view.signin.SigninResult;
import com.gamza.jinyoungkim.doodle.view.signup.DuplicatePost;
import com.gamza.jinyoungkim.doodle.view.signup.DuplicateResponse;
import com.gamza.jinyoungkim.doodle.view.signup.SignupResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UserApi {

    public static Observable<Response<SigninResult>> login(SigninModel signinModel){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.login(signinModel)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<DuplicateResponse>> duplicates(DuplicatePost duplicatePost){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.duplicates(duplicatePost)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<SignupResponse>> register(RequestBody email, RequestBody pw1, RequestBody pw2, RequestBody nickname, MultipartBody.Part image){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.register(email,pw1,pw2,nickname,image)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<MyfeedProfileResponse>> myprofile(String token){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.myprofile(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<ProfileResponse>> modify(String token, MultipartBody.Part image,int flag, String description){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.modify(token,image,flag,description)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
