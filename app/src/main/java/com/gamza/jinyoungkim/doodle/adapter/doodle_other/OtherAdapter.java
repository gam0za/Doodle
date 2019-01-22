package com.gamza.jinyoungkim.doodle.adapter.doodle_other;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherModel;
import com.gamza.jinyoungkim.doodle.view.doodle_other.other_single.OtherSingleActivity;

import java.util.ArrayList;

public class OtherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<OtherModel> otherItem;
    private Context context;
    private RequestManager requestManager;
    private String profile;
    private String nickname;

    public OtherAdapter(ArrayList<OtherModel> otherItem, Context context, RequestManager requestManager, String profile, String nickname) {
        this.otherItem = otherItem;
        this.context = context;
        this.requestManager = requestManager;
        this.profile = profile;
        this.nickname = nickname;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_myfeed_img);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridimage,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        requestManager.load(otherItem.get(position).image).into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(),OtherSingleActivity.class);
                i.putExtra("position",position);
                i.putExtra("profile",profile);
                i.putExtra("otherlist",otherItem);
                i.putExtra("nickname",nickname);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherItem.size();
    }
}
