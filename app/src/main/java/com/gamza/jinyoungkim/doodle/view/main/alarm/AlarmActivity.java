package com.gamza.jinyoungkim.doodle.view.main.alarm;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmAdapter;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmResponse;
import com.gamza.jinyoungkim.doodle.databinding.ActivityAlarmBinding;
import com.gamza.jinyoungkim.doodle.network.AlarmApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding binding;
    RecyclerView recyclerView_new;
    RecyclerView recyclerView_last;
    RecyclerView.LayoutManager layoutManager_new;
    RecyclerView.LayoutManager layoutManager_last;
    NetworkService networkService;
    ArrayList<AlarmModel> alarmItem_new;
    ArrayList<AlarmModel> alarmList_new;
    ArrayList<AlarmModel> alarmItem_last;
    ArrayList<AlarmModel> alarmList_last;
    AlarmAdapter adapter_new;
    AlarmAdapter adapter_last;
    RequestManager requestManager;

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
            getalarm(SharedPreferenceController.getToken(getApplicationContext()));
            adapter_last.update(alarmList_last);
            adapter_new.update(alarmList_new);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_alarm);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);

        recyclerView_new = binding.alarmRecyclerviewNewalarm;
        recyclerView_last = binding.alarmRecyclerviewLastalarm;
        recyclerView_new.setHasFixedSize(true);
        recyclerView_last.setHasFixedSize(true);
        layoutManager_new = new LinearLayoutManager(this);
        layoutManager_last = new LinearLayoutManager(this);
        recyclerView_new.setLayoutManager(layoutManager_new);
        recyclerView_last.setLayoutManager(layoutManager_last);

        recyclerView_new.setNestedScrollingEnabled(false);
        recyclerView_last.setNestedScrollingEnabled(false);

        getalarm(SharedPreferenceController.getToken(getApplicationContext()));
    }

    public void getalarm(String token){
        DisposableObserver<Response<AlarmResponse>>getalarmObserver = new DisposableObserver<Response<AlarmResponse>>() {
            @Override
            public void onNext(Response<AlarmResponse> alarmResponseResponse) {
                if(alarmResponseResponse.isSuccessful()){
                    alarmItem_new= new ArrayList<>();
                    alarmList_new = new ArrayList<>();

                    alarmItem_new = alarmResponseResponse.body().result.not_read;
                    if (alarmItem_new==null){
                        recyclerView_new.setVisibility(View.GONE);
                    }else{
                        for(int i=0;i<alarmItem_new.size();i++){
                            alarmList_new.add(new AlarmModel(alarmItem_new.get(i).flag,alarmItem_new.get(i).image,alarmItem_new.get(i).created,
                                    alarmItem_new.get(i).doodle_idx,alarmItem_new.get(i).nickname,alarmItem_new.get(i).count,
                                    alarmItem_new.get(i).idx));
                        }
                        adapter_new = new AlarmAdapter(alarmList_new,getApplicationContext(),requestManager);
                        recyclerView_new.setAdapter(adapter_new);
                    }


                    alarmItem_last = new ArrayList<>();
                    alarmList_last = new ArrayList<>();

                    alarmItem_last = alarmResponseResponse.body().result.is_read;
                    if(alarmItem_last==null){
                        recyclerView_last.setVisibility(View.GONE);
                    }else{
                        for(int i=0;i<alarmItem_last.size();i++){
                            alarmList_last.add(new AlarmModel(alarmItem_last.get(i).flag,alarmItem_last.get(i).image,alarmItem_last.get(i).created,
                                    alarmItem_last.get(i).doodle_idx,alarmItem_last.get(i).nickname,alarmItem_last.get(i).count,
                                    alarmItem_last.get(i).idx));
                        }
                        adapter_last = new AlarmAdapter(alarmList_last,getApplicationContext(),requestManager);
                        recyclerView_last.setAdapter(adapter_last);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AlarmApi.getalarm(token).subscribeWith(getalarmObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
