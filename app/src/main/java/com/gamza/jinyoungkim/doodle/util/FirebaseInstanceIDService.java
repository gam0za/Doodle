package com.gamza.jinyoungkim.doodle.util;

import android.util.Log;

import com.gamza.jinyoungkim.doodle.network.AlarmApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmPost;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private NetworkService networkService;
    private PushAlarmPost pushAlarmPost;
    private  static final String TAG="MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("fcm token",token);
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token){
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        pushAlarmPost = new PushAlarmPost(token);
        pushalarm(SharedPreferenceController.getToken(getApplicationContext()),pushAlarmPost);

    }

    public void pushalarm(String token, PushAlarmPost pushAlarmPost){
        DisposableObserver<Response<PushAlarmResponse>>pushalarmObserver = new DisposableObserver<Response<PushAlarmResponse>>() {
            @Override
            public void onNext(Response<PushAlarmResponse> pushAlarmResponseResponse) {
                if(pushAlarmResponseResponse.isSuccessful()){
                    Log.e("success push alarm","success");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AlarmApi.pushalarm(token, pushAlarmPost).subscribeWith(pushalarmObserver);
    }
}
