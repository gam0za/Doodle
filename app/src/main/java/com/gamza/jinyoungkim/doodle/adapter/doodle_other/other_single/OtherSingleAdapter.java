package com.gamza.jinyoungkim.doodle.adapter.doodle_other.other_single;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikeModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikePost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapPost;
import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherModel;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherSingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Typeface tf,tf_regular;
    private ArrayList<OtherModel> otherSingleItem;
    private Context context;
    private RequestManager requestManager;
    private NetworkService networkService;
    private LikePost likePost;
    private ScrapPost scrapPost;
    private CompositeDisposable compositeDisposable;

    @SuppressLint("NewApi")
    public OtherSingleAdapter(ArrayList<OtherModel> otherSingleItem, Context context, RequestManager requestManager) {
        this.otherSingleItem = otherSingleItem;
        this.context = context;
        this.requestManager = requestManager;
        this.tf = context.getResources().getFont(R.font.nanum_bold);
        this.tf_regular = context.getResources().getFont(R.font.nanum);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        compositeDisposable = new CompositeDisposable();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView date;
        TextView nickname;
        ImageView image;
        TextView like;
        TextView likecount;
        TextView comment;
        TextView commentcount;
        TextView scrap;
        TextView scrapcount;
        ImageView more;
        LinearLayout moremenu;
        TextView delete;
        TextView restore;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_singleline_text_date);
            nickname = itemView.findViewById(R.id.item_singleline_text_nickname);
            image = itemView.findViewById(R.id.item_singleline_img_image);
            like = itemView.findViewById(R.id.item_singleline_text_like);
            likecount = itemView.findViewById(R.id.item_singleline_text_likecount);
            comment = itemView.findViewById(R.id.item_singleline_text_comment);
            commentcount = itemView.findViewById(R.id.item_singleline_text_commentcount);
            scrap = itemView.findViewById(R.id.item_singleline_text_scrap);
            scrapcount = itemView.findViewById(R.id.item_singleline_text_scrapcount);
            more = itemView.findViewById(R.id.item_singleline_img_more);
            moremenu = itemView.findViewById(R.id.item_singleline_layer_moremenu);
            delete = itemView.findViewById(R.id.item_singleline_text_delete);
            restore = itemView.findViewById(R.id.item_singleline_text_restore);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleline,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.nickname.setVisibility(View.GONE);
        viewHolder.more.setVisibility(View.GONE);

        viewHolder.date.setText(otherSingleItem.get(position).created);
        requestManager.load(otherSingleItem.get(position).image).into(viewHolder.image);

        if(otherSingleItem.get(position).like!=0){
            setBold(viewHolder.like);
        }else{
            setRegular(viewHolder.like);
        }
        viewHolder.likecount.setText(String.valueOf(otherSingleItem.get(position).like_count));
        viewHolder.commentcount.setText(String.valueOf(otherSingleItem.get(position).comment_count));

        if(otherSingleItem.get(position).scraps!=0){
            setBold(viewHolder.scrap);
        }else{
            setRegular(viewHolder.scrap);
        }
        viewHolder.scrapcount.setText(String.valueOf(otherSingleItem.get(position).scrap_count));

        // like networking
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            String likeString="";
            @Override
            public void onClick(View v) {
                if(otherSingleItem.get(position).like!=0){
                    likeString = "unlike";
                }else{
                    likeString = "like";
                }
                likePost = new LikePost(likeString);
                doodleLike(SharedPreferenceController.getToken(context),likePost,otherSingleItem.get(position).idx,position);
            }
        });

        // scrap networking
        viewHolder.scrap.setOnClickListener(new View.OnClickListener() {
            String scrapString="";
            @Override
            public void onClick(final View v) {
                if(otherSingleItem.get(position).scraps!=0){
                    scrapString="unscrap";
                }else{
                    scrapString="scrap";
                }
                scrapPost = new ScrapPost(scrapString);
                doodleScrap(SharedPreferenceController.getToken(context),scrapPost,otherSingleItem.get(position).idx,position,v);
            }
        });

        // comment
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CommentActivity.class);
                i.putExtra("idx",otherSingleItem.get(position).idx);
                context.startActivity(i);
            }
        });

    }

    public void doodleLike(String token, LikePost likePost, int idx, int position){
        DisposableObserver<Response<LikeModel>> likeObserver = new DisposableObserver<Response<LikeModel>>() {
            @Override
            public void onNext(Response<LikeModel> likeModelResponse) {
                if(likeModelResponse.isSuccessful()){
                    if(otherSingleItem.get(position).like!=0){
                        otherSingleItem.get(position).like=0;
                    }else{
                        otherSingleItem.get(position).like = otherSingleItem.get(position).idx;
                    }
                    otherSingleItem.get(position).like_count = likeModelResponse.body().result.count;
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
        DoodleApi.like(token, likePost, idx).subscribeWith(likeObserver);
    }

    public void doodleScrap(String token, ScrapPost scrapPost, int idx, int position, View v){
        DisposableObserver<Response<ScrapModel>>scrapObserver = new DisposableObserver<Response<ScrapModel>>() {
            @Override
            public void onNext(Response<ScrapModel> scrapModelResponse) {
             if(scrapModelResponse.isSuccessful()){
                 if(otherSingleItem.get(position).scraps!=0){
                     otherSingleItem.get(position).scraps=0;
                 }else{
                     otherSingleItem.get(position).scraps = otherSingleItem.get(position).idx;
                     Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                     LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                     View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                     TextView t = view.findViewById(R.id.toast_confirm_text);
                     t.setText("글을 담았습니다");
                     toast.setView(view);
                     toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                     toast.show();
                 }
                 otherSingleItem.get(position).scrap_count = scrapModelResponse.body().result.count;
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

    public CompositeDisposable getCompositeDisposable(){
        return compositeDisposable;
    }

    @Override
    public int getItemCount() {
        return otherSingleItem.size();
    }



    public void setRegular(TextView textView){
        textView.setTextColor(Color.parseColor("#80000000"));
        textView.setTypeface(tf_regular);
    }

    public void setBold(TextView textView){
        textView.setTextColor(Color.parseColor("#464D56"));
        textView.setTypeface(tf);
    }
    public void update(ArrayList<OtherModel> otherModels){
        otherSingleItem.clear();
        otherSingleItem.addAll(otherModels);

        notifyDataSetChanged();
    }
}
