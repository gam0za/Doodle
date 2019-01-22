package com.gamza.jinyoungkim.doodle.adapter.doodle_alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.network.AlarmApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_detail.DetailActivity;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AlarmModel> alarmItem;
    private Context context;
    private RequestManager requestManager;
    AlarmCheckPost alarmCheckPost;

    public AlarmAdapter(ArrayList<AlarmModel> alarmItem, Context context, RequestManager requestManager) {
        this.alarmItem = alarmItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView nickname;
        TextView content;
        TextView created;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_alarm_img_profile);
            nickname = itemView.findViewById(R.id.item_alarm_text_nickname);
            content = itemView.findViewById(R.id.item_alarm_text_content);
            created = itemView.findViewById(R.id.item_alarm_text_created);
            layout = itemView.findViewById(R.id.item_alarm_layout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.nickname.setText(alarmItem.get(position).nickname);
        if(alarmItem.get(position).image==null){
            requestManager.load(R.drawable.profile).into(viewHolder.profile);
        }else{
            requestManager.load(alarmItem.get(position).image).into(viewHolder.profile);
        }
        if(alarmItem.get(position).flag==1) {
            viewHolder.content.setText("공감하였습니다");
        }else if(alarmItem.get(position).flag==2){
            viewHolder.content.setText("댓글을 남겼습니다");
        }else if(alarmItem.get(position).flag==3){
            viewHolder.content.setText("담아갔습니다");
        }
        viewHolder.created.setText(alarmItem.get(position).created);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmCheckPost = new AlarmCheckPost(alarmItem.get(position).flag,alarmItem.get(position).doodle_idx,alarmItem.get(position).idx);
                checkalarm(SharedPreferenceController.getToken(context),alarmCheckPost,position);
            }
        });
    }

    public void checkalarm(String token, AlarmCheckPost alarmCheckPost, int position){
        DisposableObserver<Response<AlarmCheckResponse>>checkalarmObserver = new DisposableObserver<Response<AlarmCheckResponse>>() {
            @Override
            public void onNext(Response<AlarmCheckResponse> alarmCheckResponseResponse) {
                if(alarmCheckResponseResponse.isSuccessful()){
                    Intent i = new Intent(context.getApplicationContext(),DetailActivity.class);
                    i.putExtra("detail_idx",alarmItem.get(position).doodle_idx);
                    context.startActivity(i);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AlarmApi.checkalarm(token, alarmCheckPost).subscribeWith(checkalarmObserver);
    }

    @Override
    public int getItemCount() {
        return alarmItem.size();
    }

    public void update(ArrayList<AlarmModel> alarmModels){
        alarmItem.clear();
        alarmItem.addAll(alarmModels);
        notifyDataSetChanged();
    }
}
