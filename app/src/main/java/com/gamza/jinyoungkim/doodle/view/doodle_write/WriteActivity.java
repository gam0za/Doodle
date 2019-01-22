package com.gamza.jinyoungkim.doodle.view.doodle_write;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivityWriteBinding;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.dialog.TodaybgDialog;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WriteActivity extends AppCompatActivity implements WriteInterface.View, View.OnClickListener{

    ActivityWriteBinding binding;
    WritePresenter presenter;
    ViewGroup.LayoutParams params;
    ViewGroup.LayoutParams params_filter;
    boolean flag=true;
    boolean confirm =false;
    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri data;
    private MultipartBody.Part image;
    Typeface gothic, myeongjo, barun, square;

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write);
        presenter = new WritePresenter(this);

        Glide.with(this).load(R.drawable.writingbg).into(binding.writeImgPhoto);
        Glide.with(this).load(R.drawable.clear).into(binding.writeImgFilter);

        // 사진 정방형
        params = binding.writeLayoutPhoto.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = params.width;

        params_filter = binding.writeImgFilter.getLayoutParams();
        params_filter.width = getResources().getDisplayMetrics().widthPixels;
        params_filter.height = params_filter.width;

        gothic = getResources().getFont(R.font.nanumgothic);
        myeongjo = getResources().getFont(R.font.nanum);
        barun = getResources().getFont(R.font.nanumbarun);
        square = getResources().getFont(R.font.nanumsquare);

        TodaybgDialog dialog = new TodaybgDialog(this);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show();

        //indicator
        binding.writeImgDown.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(flag==true){
                    binding.writeLayoutBottom.animate().translationY((float)(binding.writeLayoutEditfont.getHeight())).withLayer();
                    binding.writeImgDown.setImageResource(R.drawable.up);
                    flag=false;
                }else{
                    binding.writeLayoutBottom.animate().translationY(0F);
                    binding.writeImgDown.setImageResource(R.drawable.down);
                    flag=true;
                }

            }
        });
        binding.writeImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false){
                    binding.writeLayoutBottom.animate().translationY(0F);
                    binding.writeImgDown.setImageResource(R.drawable.down);
                    binding.writeLayoutFilter.setVisibility(View.GONE);
                    binding.writeLayoutEditfont.setVisibility(View.VISIBLE);
                    flag=true;
                }else{
                    binding.writeLayoutFilter.setVisibility(View.GONE);
                    binding.writeLayoutEditfont.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.writeImgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==false){
                    binding.writeLayoutBottom.animate().translationY(0F);
                    binding.writeImgDown.setImageResource(R.drawable.down);
                    binding.writeLayoutFilter.setVisibility(View.VISIBLE);
                    binding.writeLayoutEditfont.setVisibility(View.GONE);
                    flag=true;
                }else{
                    binding.writeLayoutFilter.setVisibility(View.VISIBLE);
                    binding.writeLayoutEditfont.setVisibility(View.GONE);
                }
            }
        });

        //font
        binding.writeLayoutNanumgothic.setOnClickListener(this);
        binding.writeLayoutNanummyeongjo.setOnClickListener(this);
        binding.writeLayoutNanumbarun.setOnClickListener(this);
        binding.writeLayoutNanumsquare.setOnClickListener(this);

        //color
        binding.writeLayoutFont1.setOnClickListener(this);
        binding.writeLayoutFont2.setOnClickListener(this);
        binding.writeLayoutFont3.setOnClickListener(this);
        binding.writeLayoutFont4.setOnClickListener(this);
        binding.writeLayoutFont5.setOnClickListener(this);
        binding.writeLayoutFont6.setOnClickListener(this);
        binding.writeLayoutFont7.setOnClickListener(this);
        binding.writeLayoutFont8.setOnClickListener(this);
        binding.writeLayoutFont9.setOnClickListener(this);

        // fontsize
        binding.writeSeekbarFontsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setFontSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // lineSpacing
        binding.writeSeekbarLinespacing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setLineSpacing(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // go to album
        binding.writeLayoutGotoalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        // select filter
        binding.writeLayoutBasic.setOnClickListener(this);
        binding.writeLayoutAngae.setOnClickListener(this);
        binding.writeLayoutLatte.setOnClickListener(this);
        binding.writeLayoutDalbit.setOnClickListener(this);
        binding.writeLayoutSaebyuk.setOnClickListener(this);

        // set filter alpha
        binding.writeSeekbarFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setFilterAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // edit
        binding.writeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!binding.writeEditText.getText().toString().equals("")){
                    binding.writeTextConfirm.setTextColor(R.color.maincolor);
                    binding.writeTextConfirm.setClickable(true);
                }else{
                    binding.writeTextConfirm.setTextColor(Color.parseColor("#30000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // confirm
        binding.writeTextConfirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(confirm==false){
                    binding.writeTextConfirm.setText("완료");
                    binding.writeLayoutBottom.animate().translationY((float)(binding.writeLayoutEditfont.getHeight())).withLayer();
                    binding.writeImgDown.setImageResource(R.drawable.up);

                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(binding.writeEditText.getWindowToken(),0);

                    binding.writeEditText.setCursorVisible(false);

                    flag=false;
                    confirm=true;
                }
                else if(confirm==true){
                    // networking
                    binding.writeLayoutPhoto.setDrawingCacheEnabled(true);
                    binding.writeLayoutPhoto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    binding.writeLayoutPhoto.layout(0,0,binding.writeLayoutPhoto.getMeasuredWidth(),binding.writeLayoutPhoto.getMeasuredHeight());
                    binding.writeLayoutPhoto.buildDrawingCache(true);
                    Bitmap b = Bitmap.createBitmap(binding.writeLayoutPhoto.getDrawingCache());
                    binding.writeLayoutPhoto.setDrawingCacheEnabled(false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                    File f = new File(Environment.getExternalStorageDirectory().toString()+File.separator+String.valueOf(System.currentTimeMillis())+".jpg");
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"),bytes.toByteArray());
                    image = MultipartBody.Part.createFormData("image",f.getName(), photoBody);

                    RequestBody text = RequestBody.create(MediaType.parse("text/plain"),binding.writeEditText.getText().toString());

                    presenter.WritePost(SharedPreferenceController.getToken(getApplicationContext()),text, image);

                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // go feel today
                    Intent i = new Intent(getApplicationContext(),FeelActivity.class);
                    i.putExtra("flag",2);
                    startActivity(i);
                    finish();

                }
            }
        });

    }
    @Override
    public void setText(String text) {
        binding.writeEditText.setText(text);
    }

    @Override
    public void setFont(String font) {
        if(font.equals("gothic")){
            binding.writeEditText.setTypeface(gothic);
        }else if(font.equals("myeongjo")){
            binding.writeEditText.setTypeface(myeongjo);
        }else if(font.equals("barun")){
            binding.writeEditText.setTypeface(barun);
        }else if(font.equals("square")){
            binding.writeEditText.setTypeface(square);
        }
    }

    @Override
    public void setFontSize(int fontSize) {
        binding.writeEditText.setTextSize((float)fontSize);
    }

    @Override
    public void setlineSpacing(int lineSpacing) {
        binding.writeEditText.setLineSpacing(0F, (float) (lineSpacing / 200 + 1));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setFontColor(String fontColor) {
       binding.writeEditText.setTextColor(Color.parseColor(fontColor));
    }

    @Override
    public void setFilterName(String filterName) {
        if(filterName.equals("angae")){
            binding.writeImgFilter.setVisibility(View.VISIBLE);
            binding.writeImgFilter.setImageResource(R.drawable.angae);
        }
        else if(filterName.equals("latte")){
            binding.writeImgFilter.setVisibility(View.VISIBLE);
            binding.writeImgFilter.setImageResource(R.drawable.latte);
        }
        else if(filterName.equals("dalbit")){
            binding.writeImgFilter.setVisibility(View.VISIBLE);
            binding.writeImgFilter.setImageResource(R.drawable.dalbit);
        }
        else if(filterName.equals("saebyuk")){
            binding.writeImgFilter.setVisibility(View.VISIBLE);
            binding.writeImgFilter.setImageResource(R.drawable.saebyuck);
        }
        else if(filterName.equals("basic")){
            binding.writeImgFilter.setVisibility(View.VISIBLE);
            binding.writeImgFilter.setImageResource(R.drawable.clear);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void setFilterAlpha(int filterAlpha) {
        Drawable alpha = ((ImageView)binding.writeImgFilter).getDrawable();

        if(filterAlpha>1) {
            alpha.setAlpha(255-filterAlpha);
        }else{
            alpha.setAlpha(255);
        }
    }

    @Override
    public void writeResult(String result) {
        if(result.equals("success")){
            Toast toast = Toast.makeText(getApplicationContext(),"글 작성이 완료되었습니다",Toast.LENGTH_SHORT);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.toast_confirm,(ViewGroup)findViewById(R.id.toast_confirm_layout));
            TextView t = v.findViewById(R.id.toast_confirm_text);
            t.setText("글 작성 완료 :)");
            toast.setView(v);
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL | Gravity.END,0,0);
            toast.show();
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        // font type
        if(v==binding.writeLayoutNanumgothic){
            presenter.setFont("gothic");
        }
        else if(v==binding.writeLayoutNanummyeongjo){
            presenter.setFont("myeongjo");
        }
        else if(v==binding.writeLayoutNanumbarun){
            presenter.setFont("barun");
        }
        else if(v==binding.writeLayoutNanumsquare){
            presenter.setFont("square");
        }

        // font color
        else if(v==binding.writeLayoutFont1){
            presenter.setFontColor("#000000");
        }
        else if(v==binding.writeLayoutFont2){
            presenter.setFontColor("#ffffff");
        }
        else if(v==binding.writeLayoutFont3){
            presenter.setFontColor("#EBC0BC");
        }
        else if(v==binding.writeLayoutFont4){
            presenter.setFontColor("#EFD4B3");
        }
        else if(v==binding.writeLayoutFont5){
            presenter.setFontColor("#F6EDBE");
        }
        else if(v==binding.writeLayoutFont6){
            presenter.setFontColor("#CBDDB5");
        }
        else if(v==binding.writeLayoutFont7){
            presenter.setFontColor("#BEDDD8");
        }
        else if(v==binding.writeLayoutFont8){
            presenter.setFontColor("#B8D1E2");
        }
        else if(v==binding.writeLayoutFont9){
            presenter.setFontColor("#C5AFC5");
        }

        // select filter
        else if(v==binding.writeLayoutBasic){
            presenter.setFilterName("basic");
        }
        else if(v==binding.writeLayoutAngae){
            presenter.setFilterName("angae");
        }
        else if(v==binding.writeLayoutLatte){
            presenter.setFilterName("latte");
        }
        else if(v==binding.writeLayoutDalbit){
            presenter.setFilterName("dalbit");
        }
        else if(v==binding.writeLayoutSaebyuk){
            presenter.setFilterName("saebyuk");
        }

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

                    DressImage(this, data.getData(), binding.writeImgPhoto);
                    DressImage(this, data.getData(), binding.writeImgBasic);
                    DressImage(this, data.getData(), binding.writeImgAngaeBasic);
                    DressImage(this, data.getData(), binding.writeImgDalbitBasic);
                    DressImage(this, data.getData(), binding.writeImgLatteBasic);
                    DressImage(this, data.getData(), binding.writeImgSaebyukBasic);



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
