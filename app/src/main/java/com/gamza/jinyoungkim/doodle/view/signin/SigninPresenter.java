package com.gamza.jinyoungkim.doodle.view.signin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.UserApi;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninPresenter implements SigninInterface.Presenter{

    SigninInterface.View view;
    SigninModel model;
    NetworkService networkService;

    public SigninPresenter(SigninInterface.View view) {
        this.view = view;
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
    }


    /*
    * networking
    * */
    @SuppressLint("CheckResult")
    @Override
    public void setSigninPost(String email, final String pw, final Context context) {

        model = new SigninModel(email, pw, FirebaseInstanceId.getInstance().getToken().toString());
        UserApi.login(model).subscribeWith(new DisposableObserver<Response<SigninResult>>() {
            @Override
            public void onNext(Response<SigninResult> signinResultResponse) {
                if(signinResultResponse.isSuccessful()){
                    SharedPreferenceController.setEmail(context.getApplicationContext(),signinResultResponse.body().result.profile.email);
                    SharedPreferenceController.setToken(context.getApplicationContext(),signinResultResponse.body().result.token);
                    SharedPreferenceController.setNickname(context.getApplicationContext(),signinResultResponse.body().result.profile.nickname);
                    SharedPreferenceController.setProfile(context.getApplicationContext(),signinResultResponse.body().result.profile.profile);
                }
            }

            @Override
            public void onError(Throwable e) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :)");
            }

            @Override
            public void onComplete() {
                view.login("success");
            }
        });
    }



}
