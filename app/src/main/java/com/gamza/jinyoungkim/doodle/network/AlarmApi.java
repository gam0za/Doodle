package com.gamza.jinyoungkim.doodle.network;

import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmCheckPost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmCheckResponse;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmResponse;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmPost;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AlarmApi {
    public static Observable<Response<AlarmResponse>> getalarm(String token){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.getalarm(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<AlarmCheckResponse>>checkalarm(String token, AlarmCheckPost alarmCheckPost){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.checkalarm(token, alarmCheckPost)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<PushAlarmResponse>>pushalarm(String token, PushAlarmPost pushAlarmPost){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.pushalarm(token, pushAlarmPost)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
