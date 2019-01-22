package com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed.myfeed_single;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
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
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelModel;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentActivity;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyfeedSingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int flag;
    private Typeface tf, tf_regular;
    private ArrayList<FeelModel> myfeedSingleItem;
    private Context context;
    private RequestManager requestManager;
    private NetworkService networkService;
    private LikePost likePost;
    private ScrapPost scrapPost;
    private CompositeDisposable compositeDisposable;

    @SuppressLint("NewApi")
    public MyfeedSingleAdapter(int flag, ArrayList<FeelModel> myfeedSingleItem, Context context, RequestManager requestManager) {
        this.flag = flag;
        this.myfeedSingleItem = myfeedSingleItem;
        this.context = context;
        this.requestManager = requestManager;
        this.tf = context.getResources().getFont(R.font.nanum_bold);
        this.tf_regular = context.getResources().getFont(R.font.nanum);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        compositeDisposable = new CompositeDisposable();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
        if(flag==3) {
            viewHolder.nickname.setVisibility(View.GONE);
        }else if(flag==4){
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.nickname.setVisibility(View.VISIBLE);
            viewHolder.nickname.setText(myfeedSingleItem.get(position).nickname);
        }
            requestManager.load(myfeedSingleItem.get(position).image).into(viewHolder.image);
            viewHolder.date.setText(myfeedSingleItem.get(position).created);
        if(myfeedSingleItem.get(position).like!=0){
            setBold(viewHolder.like);
        }else{
            setRegular(viewHolder.like);
        }
        viewHolder.likecount.setText(String.valueOf(myfeedSingleItem.get(position).like_count));
        viewHolder.commentcount.setText(String.valueOf(myfeedSingleItem.get(position).comment_count));

        if(myfeedSingleItem.get(position).scraps!=0){
            setBold(viewHolder.scrap);
        }else{
            setRegular(viewHolder.scrap);
        }
        viewHolder.scrapcount.setText(String.valueOf(myfeedSingleItem.get(position).scrap_count));


        // like networking
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            String likeString="";
            @Override
            public void onClick(View v) {
                if(myfeedSingleItem.get(position).like!=0){
                    likeString = "unlike";
                }else{
                    likeString = "like";
                }
                likePost = new LikePost(likeString);
                doodleLike(SharedPreferenceController.getToken(context),likePost,myfeedSingleItem.get(position).idx,position);
            }
        });

        // scrap networking
        viewHolder.scrap.setOnClickListener(new View.OnClickListener() {
            String scrapString="";
            @Override
            public void onClick(final View v) {
                if(myfeedSingleItem.get(position).scraps!=0){
                    scrapString="unscrap";
                }else{
                    scrapString="scrap";
                }
                scrapPost = new ScrapPost(scrapString);
                doodleScrap(SharedPreferenceController.getToken(context),scrapPost,myfeedSingleItem.get(position).idx,position,v);
            }
        });

        // comment
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CommentActivity.class);
                i.putExtra("idx",myfeedSingleItem.get(position).idx);
                context.startActivity(i);
            }
        });

        // more menu
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.moremenu.getVisibility()==View.VISIBLE){
                    viewHolder.moremenu.setVisibility(View.GONE);
                }else{
                    viewHolder.moremenu.setVisibility(View.VISIBLE);
                }
            }
        });

        // delete
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                doodleDelete(SharedPreferenceController.getToken(context),myfeedSingleItem.get(position).idx,position,v);
            }
        });

        // 저장
        viewHolder.restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                TextView t = view.findViewById(R.id.toast_confirm_text);
                t.setText("게시물을 앨범에 저장하였습니다");
                toast.setView(view);
                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                toast.show();
                viewHolder.moremenu.setVisibility(View.GONE);

                viewHolder.image.setDrawingCacheEnabled(true);
                Bitmap b = Bitmap.createBitmap(viewHolder.image.getDrawingCache());
                viewHolder.image.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG,100,bytes);

                File f = new File(Environment.getExternalStorageDirectory().toString()+File.separator+String.valueOf(System.currentTimeMillis())+".jpg");

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                i.setData(Uri.fromFile(f));
                context.sendBroadcast(i);
            }
        });

        // other
        viewHolder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherActivity.class);
                i.putExtra("user_idx",myfeedSingleItem.get(position).user_idx);
                context.startActivity(i);
            }
        });

    }

    public void doodleLike(String token, LikePost likePost, int idx, int position){
        DisposableObserver<Response<LikeModel>> likeObserver = new DisposableObserver<Response<LikeModel>>() {
            @Override
            public void onNext(Response<LikeModel> likeModelResponse) {
                if(likeModelResponse.isSuccessful()){
                    if(myfeedSingleItem.get(position).like!=0){
                        myfeedSingleItem.get(position).like=0;
                    }else{
                        myfeedSingleItem.get(position).like = myfeedSingleItem.get(position).idx;
                    }
                    myfeedSingleItem.get(position).like_count = likeModelResponse.body().result.count;
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
        DisposableObserver<Response<ScrapModel>> scrapObserver = new DisposableObserver<Response<ScrapModel>>() {
            @Override
            public void onNext(Response<ScrapModel> scrapModelResponse) {
                if(scrapModelResponse.isSuccessful()){
                    if(flag==3){
                        if(myfeedSingleItem.get(position).scraps!=0){
                            myfeedSingleItem.get(position).scraps=0;
                        }else{
                            myfeedSingleItem.get(position).scraps = myfeedSingleItem.get(position).idx;
                        }
                        myfeedSingleItem.get(position).scrap_count = scrapModelResponse.body().result.count;
                        notifyItemChanged(position);
                    }else if(flag==4){
                        if(myfeedSingleItem.get(position).scraps!=0){
                            myfeedSingleItem.get(position).scraps=0;
                            Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                            View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                            TextView t = view.findViewById(R.id.toast_confirm_text);
                            t.setText("담아감을 해제했습니다");
                            toast.setView(view);
                            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                            toast.show();
                        }else{
                            myfeedSingleItem.get(position).scraps = myfeedSingleItem.get(position).idx;

                            Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                            View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                            TextView t = view.findViewById(R.id.toast_confirm_text);
                            t.setText("다시 담기 완료 :)");
                            toast.setView(view);
                            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                            toast.show();
                        }
                        myfeedSingleItem.get(position).scrap_count = scrapModelResponse.body().result.count;
                        notifyItemChanged(position);

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
        compositeDisposable.add(scrapObserver);
        DoodleApi.scrap(token, scrapPost, idx).subscribeWith(scrapObserver);
    }

    public void doodleDelete(String token, int idx, int position, View v){
        DisposableObserver<Response<DeleteResponse>>deleteObserver = new DisposableObserver<Response<DeleteResponse>>() {
            @Override
            public void onNext(Response<DeleteResponse> deleteResponseResponse) {
                if(deleteResponseResponse.isSuccessful()){
                    notifyItemRemoved(position);
                    Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    View view = inflater.inflate(R.layout.toast_confirm,(ViewGroup)v.findViewById(R.id.toast_confirm_layout));
                    TextView t = view.findViewById(R.id.toast_confirm_text);
                    t.setText("게시물을 삭제했습니다");
                    toast.setView(view);
                    toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
                    toast.show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(deleteObserver);
        DoodleApi.delete(token, idx).subscribeWith(deleteObserver);
    }

    public CompositeDisposable getCompositeDisposable(){
        return compositeDisposable;
    }

    @Override
    public int getItemCount() {
        return myfeedSingleItem.size();
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
        myfeedSingleItem.clear();
        myfeedSingleItem.addAll(feelModels);

        notifyDataSetChanged();
    }
}
