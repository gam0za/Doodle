package com.gamza.jinyoungkim.doodle.adapter.doodle_feel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelModel;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherActivity;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private int flag;
    private ArrayList<FeelModel> feelItem;
    private Context context;
    private Typeface tf, tf_regular;
    final static int size = ViewGroup.LayoutParams.WRAP_CONTENT;
    private RequestManager requestManager;
    private NetworkService networkService;
    private LikePost likePost;
    private ScrapPost scrapPost;
    private CompositeDisposable compositeDisposable;


    @SuppressLint("NewApi")
    public FeelAdapter(int flag, ArrayList<FeelModel> feelItem, Context context, RequestManager requestManager) {
        this.flag = flag;
        this.feelItem = feelItem;
        this.context = context;
        this.tf = context.getResources().getFont(R.font.nanum_bold);
        this.tf_regular = context.getResources().getFont(R.font.nanum);
        this.requestManager = requestManager;
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        compositeDisposable = new CompositeDisposable();
    }

    // ViewHolder(all)
    public static class AllViewHolder extends RecyclerView.ViewHolder{

        ImageView rank;
        ImageView image;
        TextView like;
        TextView likecount;
        TextView scrap;
        TextView scrapcount;
        TextView comment;
        TextView commentcount;
        TextView writer;

        public AllViewHolder(View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.item_feel_img_rank);
            image = itemView.findViewById(R.id.item_feel_img_image);
            like = itemView.findViewById(R.id.item_feel_text_like);
            likecount = itemView.findViewById(R.id.item_feel_text_likecount);
            scrap = itemView.findViewById(R.id.item_feel_text_scrap);
            scrapcount = itemView.findViewById(R.id.item_feel_text_scrapcount);
            comment = itemView.findViewById(R.id.item_feel_text_comment);
            commentcount = itemView.findViewById(R.id.item_feel_text_commentcount);
            writer = itemView.findViewById(R.id.item_feel_text_writer);
        }
    }

    // ViewHolder(today)
    public static class TodayViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout datebox;
        TextView date;
        ImageView image;
        TextView like;
        TextView likecount;
        TextView scrap;
        TextView scrapcount;
        TextView comment;
        TextView commentcount;
        TextView writer;

        public TodayViewHolder(View itemView) {
            super(itemView);
            datebox = itemView.findViewById(R.id.item_today_layout_date);
            date = itemView.findViewById(R.id.item_today_text_date);
            image = itemView.findViewById(R.id.item_today_img_image);
            like = itemView.findViewById(R.id.item_today_text_like);
            likecount = itemView.findViewById(R.id.item_today_text_likecount);
            scrap = itemView.findViewById(R.id.item_today_text_scrap);
            scrapcount = itemView.findViewById(R.id.item_today_text_scrapcount);
            comment = itemView.findViewById(R.id.item_today_text_comment);
            commentcount = itemView.findViewById(R.id.item_today_text_commentcount);
            writer = itemView.findViewById(R.id.item_feel_today_text_writer);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(flag==-1 || flag==1){ // all || week
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feel,parent,false);
            return new AllViewHolder(v);
        }else if(flag == 2){ // today
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today,parent,false);
            return new TodayViewHolder(v);
        }else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // all || week
        if(flag==-1 || flag==1){
            final AllViewHolder viewHolder = (AllViewHolder)holder;

            //ranking1
            if(position==0){
              viewHolder.rank.setVisibility(View.VISIBLE);
              viewHolder.rank.setImageResource(R.drawable.rank1);
            }
            //ranking2
            else if(position==1){
                viewHolder.rank.setVisibility(View.VISIBLE);
                viewHolder.rank.setImageResource(R.drawable.rank2);
            }
            //ranking3
            else if(position==2){
                viewHolder.rank.setVisibility(View.VISIBLE);
                viewHolder.rank.setImageResource(R.drawable.rank3);
            }
            else if (position>2){
                viewHolder.rank.setVisibility(View.GONE);
            }

            requestManager.load(feelItem.get(position).image).apply(new RequestOptions().override(size,size)).into(viewHolder.image);

            if(feelItem.get(position).like!=0){
                setBold(viewHolder.like);
            }else{
                setRegular(viewHolder.like);
            }
            viewHolder.likecount.setText(String.valueOf(feelItem.get(position).like_count));
            viewHolder.commentcount.setText(String.valueOf(feelItem.get(position).comment_count));

            if(feelItem.get(position).scraps!=0){
                setBold(viewHolder.scrap);
            }else{
                setRegular(viewHolder.scrap);
            }
            viewHolder.scrapcount.setText(String.valueOf(feelItem.get(position).scrap_count));
            viewHolder.writer.setText(feelItem.get(position).nickname);

            // like networking
            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                String likeString="";
                @Override
                public void onClick(View v) {
                    if(feelItem.get(position).like!=0){
                       likeString = "unlike";
                    }else{
                        likeString = "like";
                    }
                    likePost = new LikePost(likeString);
                    doodleLike(SharedPreferenceController.getToken(context),likePost,feelItem.get(position).idx,position);
                }
            });

            // scrap networking
            viewHolder.scrap.setOnClickListener(new View.OnClickListener() {
                String scrapString="";
                @Override
                public void onClick(final View v) {
                    if(feelItem.get(position).scraps!=0){
                        scrapString="unscrap";
                    }else{
                        scrapString="scrap";
                    }
                    scrapPost = new ScrapPost(scrapString);
                    doodleScrap(SharedPreferenceController.getToken(context),scrapPost, feelItem.get(position).idx,position,v);
                }
            });

            // comment
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,CommentActivity.class);
                    i.putExtra("idx",feelItem.get(position).idx);
                    context.startActivity(i);
                }
            });
            // other
            viewHolder.writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,OtherActivity.class);
                    i.putExtra("user_idx",feelItem.get(position).user_idx);
                    context.startActivity(i);
                }
            });
        }

        // today
        else if(flag==2){
            TodayViewHolder viewHolder = (TodayViewHolder)holder;

            if(position==0){
                viewHolder.datebox.setVisibility(View.VISIBLE);
                viewHolder.date.setText(feelItem.get(position).created);
            }else{
                viewHolder.datebox.setVisibility(View.GONE);
            }

            requestManager.load(feelItem.get(position).image).apply(new RequestOptions().override(size,size)).into(viewHolder.image);

            if(feelItem.get(position).like>0){
                setBold(viewHolder.like);
            }else{
                setRegular(viewHolder.like);
            }
            viewHolder.likecount.setText(String.valueOf(feelItem.get(position).like_count));
            viewHolder.commentcount.setText(String.valueOf(feelItem.get(position).comment_count));
            if(feelItem.get(position).scraps>0){
                setBold(viewHolder.scrap);
            }else{
                setRegular(viewHolder.scrap);
            }
            viewHolder.scrapcount.setText(String.valueOf(feelItem.get(position).scrap_count));
            viewHolder.writer.setText(feelItem.get(position).nickname);

            // like networking
            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                String likeString="";
                @Override
                public void onClick(View v) {
                    if(feelItem.get(position).like!=0){
                        likeString = "unlike";
                    }else{
                        likeString = "like";
                    }
                    likePost = new LikePost(likeString);
                    doodleLike(SharedPreferenceController.getToken(context),likePost,feelItem.get(position).idx,position);
                }
            });

            // scrap networking
            viewHolder.scrap.setOnClickListener(new View.OnClickListener() {
                String scrapString="";
                @Override
                public void onClick(final View v) {
                    if(feelItem.get(position).scraps!=0){
                        scrapString="unscrap";
                    }else{
                        scrapString="scrap";
                    }
                    scrapPost = new ScrapPost(scrapString);
                    doodleScrap(SharedPreferenceController.getToken(context),scrapPost, feelItem.get(position).idx,position,v);
                }
            });

            // comment
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,CommentActivity.class);
                    i.putExtra("idx",feelItem.get(position).idx);
                    i.putExtra("position",position);
                    i.putExtra("flag",flag);
                    context.startActivity(i);
                }
            });

            // other
            viewHolder.writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,OtherActivity.class);
                    i.putExtra("user_idx",feelItem.get(position).user_idx);
                    context.startActivity(i);
                }
            });

        }
    }

    public void doodleLike(String token, LikePost likePost, int idx, int position){
        DisposableObserver<Response<LikeModel>> likeObserver = new DisposableObserver<Response<LikeModel>>() {
            @Override
            public void onNext(Response<LikeModel> likeModelResponse) {
                if(likeModelResponse.isSuccessful()){
                    if(feelItem.get(position).like!=0){
                        feelItem.get(position).like=0;
                    }else{
                        feelItem.get(position).like = feelItem.get(position).idx;
                    }
                    feelItem.get(position).like_count = likeModelResponse.body().result.count;
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(likeObserver);
        DoodleApi.like(token,likePost,idx).subscribeWith(likeObserver);
    }

    public void doodleScrap(String token, ScrapPost scrapPost, int idx, int position, View v){
        DisposableObserver<Response<ScrapModel>> scrapObserver = new DisposableObserver<Response<ScrapModel>>() {
            @Override
            public void onNext(Response<ScrapModel> scrapModelResponse) {
                if(scrapModelResponse.isSuccessful()){
                    if(feelItem.get(position).scraps!=0){
                        feelItem.get(position).scraps=0;
                    }else{
                        feelItem.get(position).scraps = feelItem.get(position).idx;
                        Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                        TextView t = view.findViewById(R.id.toast_confirm_text);
                        t.setText(feelItem.get(position).nickname+"님의 글을 담았습니다");
                        toast.setView(view);
                        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                        toast.show();
                    }
                    feelItem.get(position).scrap_count = scrapModelResponse.body().result.count;
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(scrapObserver);
        DoodleApi.scrap(token, scrapPost, idx).subscribeWith(scrapObserver);
    }

    @Override
    public int getItemCount() {
        return feelItem.size();
    }
    @Override
    public void onClick(View v) {
    }

    public void setRegular(TextView textView){
        textView.setTextColor(Color.parseColor("#80000000"));
        textView.setTypeface(tf_regular);
    }

    public void setBold(TextView textView){
        textView.setTextColor(Color.parseColor("#464D56"));
        textView.setTypeface(tf);
    }

    public void update(ArrayList<FeelModel> feelModels){
        feelItem.clear();
        feelItem.addAll(feelModels);

        notifyDataSetChanged();
    }

    public CompositeDisposable getCompositeDisposable(){
        return  compositeDisposable;
    }

}
