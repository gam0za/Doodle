package com.gamza.jinyoungkim.doodle.adapter.doodle_search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.doodle_detail.DetailActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchDoodleModel;

import java.util.ArrayList;

public class SearchDoodleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SearchDoodleModel> searchdoodleItem;
    private Context context;
    private RequestManager requestManager;

    public SearchDoodleAdapter(ArrayList<SearchDoodleModel> searchdoodleItem, Context context, RequestManager requestManager) {
        this.searchdoodleItem = searchdoodleItem;
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
        requestManager.load(searchdoodleItem.get(position).image).into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(),DetailActivity.class);
                i.putExtra("detail_idx",searchdoodleItem.get(position).idx);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchdoodleItem.size();
    }
}
