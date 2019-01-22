package com.gamza.jinyoungkim.doodle.view.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySignupBinding;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.UserApi;
import com.gamza.jinyoungkim.doodle.util.BackPressCloseHandler;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.view.signin.SigninActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    BackPressCloseHandler backPressCloseHandler;
    RequestManager requestManager;
    DuplicatePost duplicatePost;

    CompositeDisposable compositeDisposable;

    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri data;
    private MultipartBody.Part image;

    String message="";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        backPressCloseHandler = new BackPressCloseHandler(this);

        requestManager = Glide.with(this);
        compositeDisposable = new CompositeDisposable();

        // 1. change image
        binding.signupTextChangeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        // 2. nickname
        binding.signupEditNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(binding.signupEditNickname.getText().toString().length()>=2 && binding.signupEditNickname.getText().toString().length()<=8){
                    duplicates(binding.signupEditNickname.getText().toString(),binding.signupEditEmail.getText().toString(),1);
                }else{
                    binding.signupTextNotice.setText("필명은 2~8자로 작성해주세요");
                    binding.signupImgNickname.setImageResource(R.drawable.profilex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 3. email
        binding.signupEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(checkEmail(binding.signupEditEmail.getText().toString())){
                    duplicates(binding.signupEditNickname.getText().toString(),binding.signupEditEmail.getText().toString(),2);
                }else if(checkEmail(binding.signupEditEmail.getText().toString())==false){
                    binding.signupTextNotice.setText("이메일 형식에 맞지 않습니다");
                    binding.signupImgEmail.setImageResource(R.drawable.profilex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 4. password
        binding.signupEditPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.signupEditPw.getText().toString().length()>=8 &&
                        binding.signupEditPw.getText().toString().length()<=20 &&
                        checkPw(binding.signupEditPw.getText().toString())){

                    binding.signupImgPw.setImageResource(R.drawable.profileo);
                    binding.signupTextNotice.setText(null);
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(binding.signupEditPw.getWindowToken(),0);
                }else{
                    binding.signupTextNotice.setText("비밀번호를 영문/숫자, 8~20자로 작성해주세요");
                    binding.signupImgPw.setImageResource(R.drawable.profilex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 5. password2
        binding.signupEditPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.signupEditPw2.getText().toString().equals(binding.signupEditPw.getText().toString())){
                    binding.signupImgPw2.setImageResource(R.drawable.profileo);
                    binding.signupTextNotice.setText(null);
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(binding.signupEditPw2.getWindowToken(),0);
                }else{
                    binding.signupTextNotice.setText("비밀번호가 일치하지 않습니다");
                    binding.signupImgPw2.setImageResource(R.drawable.profilex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 6. confirm signup
        binding.signupImgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.signupEditEmail.getText().toString().replace(" ","");
                final String pw = binding.signupEditPw.getText().toString().replace(" ","");
                String nickname = binding.signupEditNickname.getText().toString().replace(" ","");
                RequestBody _email = RequestBody.create(MediaType.parse("text/plain"),email);
                RequestBody _pw = RequestBody.create(MediaType.parse("text/plain"),pw);
                RequestBody _nickname = RequestBody.create(MediaType.parse("text/plain"),nickname);
                register(_email,_pw,_pw,_nickname,image);
            }
        });

        // 7. go login page
        binding.signupTextGologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    // duplicates networking
    public void duplicates(String nickname, String email, int flag){
        DisposableObserver<Response<DuplicateResponse>> duplicatesObserver = new DisposableObserver<Response<DuplicateResponse>>() {
            @Override
            public void onNext(Response<DuplicateResponse> duplicateResponseResponse) {
                if(duplicateResponseResponse.isSuccessful()){
                    if(flag==1){
                        binding.signupImgNickname.setImageResource(R.drawable.profileo);
                        binding.signupTextNotice.setText(null);
                        message = duplicateResponseResponse.body().message;
                        Log.e("message",message);
                    }else if(flag==2){
                        binding.signupImgEmail.setImageResource(R.drawable.profileo);
                        binding.signupTextNotice.setText(null);
                        message = duplicateResponseResponse.body().message;
                        Log.e("message",message);
                    }


                }else {
                    if(flag==1){
                        Log.e("message",message);
                        binding.signupTextNotice.setText("필명이 중복됩니다");
                        binding.signupImgNickname.setImageResource(R.drawable.profilex);
                    }else if(flag==2){
                        binding.signupTextNotice.setText("이메일이 중복됩니다");
                        binding.signupImgEmail.setImageResource(R.drawable.profilex);
                    }

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
        compositeDisposable.add(duplicatesObserver);
        duplicatePost = new DuplicatePost(nickname, email, flag);
        UserApi.duplicates(duplicatePost).subscribeWith(duplicatesObserver);

    }

    public void register(RequestBody email, RequestBody pw1, RequestBody pw2, RequestBody nickname, MultipartBody.Part image){
        Log.e("register in", "SUCCESS");
        DisposableObserver<Response<SignupResponse>> registerObserver = new DisposableObserver<Response<SignupResponse>>() {
            @Override
            public void onNext(Response<SignupResponse> signupResponseResponse) {
               if(signupResponseResponse.isSuccessful()){
                   Log.e("signup success", "SUCCESS");
                   Intent i = new Intent(getApplicationContext(),SigninActivity.class);
                            i.putExtra("email",signupResponseResponse.body().result.email);
                            i.putExtra("pw",binding.signupEditPw.getText().toString().replace(" ",""));
                            startActivity(i);
                            finish();
               }else{
                   Log.e("fail","fail");
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
        compositeDisposable.add(registerObserver);
        UserApi.register(email,pw1,pw2,nickname,image).subscribeWith(registerObserver);
    }

    // sub function
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode==RESULT_OK){
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

                    DressImage(this, data.getData(), binding.signupImgProfile);
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"),baos.toByteArray());
                    File photo = new File(this.data.toString());
                    image = MultipartBody.Part.createFormData("image",photo.getName(), photoBody);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }

    public static boolean checkPw(String pw){
        String pw_regrex = "^[a-zA-Z0-9]*$";
        Pattern p = Pattern.compile(pw_regrex);
        Matcher m = p.matcher(pw);
        boolean isNormal = m.matches();
        return  isNormal;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
