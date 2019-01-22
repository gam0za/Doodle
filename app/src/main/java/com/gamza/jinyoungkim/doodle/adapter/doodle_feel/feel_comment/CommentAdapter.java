package com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment;

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
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherActivity;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ArrayList<CommentModel> commentItem;
    private Context context;
    private RequestManager requestManager;

    public CommentAdapter(ArrayList<CommentModel> commentItem, Context context, RequestManager requestManager) {
        this.commentItem = commentItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView nickname;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_comment_img_profile);
            nickname = itemView.findViewById(R.id.item_comment_name);
            content = itemView.findViewById(R.id.item_comment_content);
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
        if(commentItem.get(position).profile==null){
            requestManager.load(R.drawable.profile).into(viewHolder.profile);
        }else{
            requestManager.load(commentItem.get(position).profile).into(viewHolder.profile);
        }
        viewHolder.nickname.setText(commentItem.get(position).nickname);
        viewHolder.content.setText(commentItem.get(position).content);

        viewHolder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",commentItem.get(position).user_idx);
                context.startActivity(i);
            }
        });

        viewHolder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",commentItem.get(position).user_idx);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentItem.size();
    }

    @Override
    public void onClick(View v) {

    }
    public void update(ArrayList<CommentModel> commentModels){
        commentItem.clear();
        commentItem.addAll(commentModels);
        notifyDataSetChanged();
    }
}
