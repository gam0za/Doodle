package com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelModel;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.myfeed_single.SingleLineActivity;

import java.util.ArrayList;

public class MyfeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int flag;
    private ArrayList<FeelModel> myfeedItem;
    private Context context;
    private RequestManager requestManager;

    public MyfeedAdapter(int flag, ArrayList<FeelModel> myfeedItem, Context context, RequestManager requestManager) {
        this.flag = flag;
        this.myfeedItem = myfeedItem;
        this.context = context;
        this.requestManager = requestManager;
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
        requestManager.load(myfeedItem.get(position).image).into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(),SingleLineActivity.class);
                i.putExtra("position",position);
                if(flag==3){
                    i.putExtra("flag",3);
                    i.putExtra("nickname",myfeedItem.get(position).nickname);
                    i.putExtra("profile",myfeedItem.get(position).profile);
                    i.putExtra("feedlist",myfeedItem);
                }else if(flag==4){
                    i.putExtra("flag",4);
                    i.putExtra("nickname",myfeedItem.get(position).nickname);
                    i.putExtra("profile",myfeedItem.get(position).profile);
                    i.putExtra("feedlist",myfeedItem);
                }
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return myfeedItem.size();
    }

    public void update(ArrayList<FeelModel> feelModels){
        myfeedItem.clear();
        myfeedItem.addAll(feelModels);

        notifyDataSetChanged();
    }

}
