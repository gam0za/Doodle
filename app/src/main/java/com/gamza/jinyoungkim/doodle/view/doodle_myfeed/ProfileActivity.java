package com.gamza.jinyoungkim.doodle.view.doodle_myfeed;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivityProfileBinding;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.UserApi;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    RequestManager requestManager;
    String description;
    NetworkService networkService;
    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri data;
    private MultipartBody.Part image;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        requestManager = Glide.with(this);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        Intent i = getIntent();
        description = i.getStringExtra("description");
        image = MultipartBody.Part.createFormData("image",SharedPreferenceController.getProfile(getApplicationContext()));
        flag = 1;
        binding.profileEditDescription.setText(description);

        if(SharedPreferenceController.getProfile(getApplicationContext()).equals("")){
            requestManager.load(R.drawable.profile).into(binding.profileImgProfile);
        }else{
            requestManager.load(SharedPreferenceController.getProfile(getApplicationContext())).into(binding.profileImgProfile);
        }

        binding.profileTextChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        binding.profileTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = binding.profileEditDescription.getText().toString();
                DisposableObserver<Response<ProfileResponse>> profileObserver = new DisposableObserver<Response<ProfileResponse>>() {
                    @Override
                    public void onNext(Response<ProfileResponse> profileResponseResponse) {
                     if(profileResponseResponse.isSuccessful()){
                         Intent i = new Intent(getApplicationContext(),MyfeedActivity.class);
                            startActivity(i);
                            finish();
                     }
                    }

                    @Override
                    public void onError(Throwable e) {
                        GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :)");
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                UserApi.modify(SharedPreferenceController.getToken(getApplicationContext()),image,flag,description).subscribeWith(profileObserver);
            }
        });


    }
    public void DressImage(FragmentActivity activity, Uri uri, ImageView image){
        Glide.with(activity)
                .load(uri)
                .into(image);
    }

    public void changeImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode==RESULT_OK){
                flag=2;
                try {
                    this.data = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    InputStream input=null;

                    try {

                        input = getContentResolver().openInputStream(this.data);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                    DressImage(this, data.getData(), binding.profileImgProfile);
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"),baos.toByteArray());
                    File photo = new File(this.data.toString());
                    image = MultipartBody.Part.createFormData("image",photo.getName(), photoBody);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
