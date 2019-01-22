package com.gamza.jinyoungkim.doodle.adapter.doodle_search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchNameModel;

import java.util.ArrayList;

public class SearchNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SearchNameModel> searchnameItem;
    private Context context;
    private RequestManager requestManager;

    public SearchNameAdapter(ArrayList<SearchNameModel> searchnameItem, Context context, RequestManager requestManager) {
        this.searchnameItem = searchnameItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView nickname;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_comment_img_profile);
            nickname = itemView.findViewById(R.id.item_comment_name);
            description = itemView.findViewById(R.id.item_comment_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.nickname.setText(searchnameItem.get(position).nickname);
        if(searchnameItem.get(position).image==null){
            requestManager.load(R.drawable.profile).into(viewHolder.profile);
        }else{
            requestManager.load(searchnameItem.get(position).image).into(viewHolder.profile);
        }

        viewHolder.description.setText(searchnameItem.get(position).description);

        viewHolder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",searchnameItem.get(position).idx);
                context.startActivity(i);

            }
        });

        viewHolder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",searchnameItem.get(position).idx);
                context.startActivity(i);
            }
        });

        viewHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",searchnameItem.get(position).idx);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchnameItem.size();
    }
}
