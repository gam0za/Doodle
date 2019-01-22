package com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.adapter.doodle_search.SearchDoodleAdapter;
import com.gamza.jinyoungkim.doodle.adapter.doodle_search.SearchNameAdapter;
import com.gamza.jinyoungkim.doodle.databinding.ActivitySearchBinding;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.network.SearchApi;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    RequestManager requestManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager_name;
    RecyclerView.LayoutManager layoutManager_doodle;
    NetworkService networkService;

    ArrayList<SearchNameModel> searchNameItem;
    ArrayList<SearchNameModel> searchNameList;
    ArrayList<SearchDoodleModel> searchDoodleItem;
    ArrayList<SearchDoodleModel> searchDoodleList;

    SearchNameAdapter adapter_name;
    SearchDoodleAdapter adapter_doodle;

    CompositeDisposable compositeDisposable;

    String flag;
    boolean text_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        flag="name";
        text_flag=false;
        compositeDisposable = new CompositeDisposable();

        recyclerView = binding.searchRecyclerview;
        recyclerView.setHasFixedSize(true);

        layoutManager_name = new LinearLayoutManager(this);
        layoutManager_doodle = new GridLayoutManager(this,2);
        ((GridLayoutManager)layoutManager_doodle).setOrientation(GridLayoutManager.VERTICAL);

        setBold(binding.searchTextNickname);
        setRegular(binding.searchTextDoodle);

        binding.searchTextNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="name";
                setRegular(binding.searchTextDoodle);
                setBold(binding.searchTextNickname);
                binding.searchEditText.setText("");
                recyclerView.setVisibility(View.GONE);
                binding.searchLayoutResult.setVisibility(View.GONE);
            }
        });

        binding.searchTextDoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="doodle";
                setRegular(binding.searchTextNickname);
                setBold(binding.searchTextDoodle);
                binding.searchEditText.setText("");
                recyclerView.setVisibility(View.GONE);
                binding.searchLayoutResult.setVisibility(View.GONE);
            }
        });

        binding.searchTextCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.getWindowToken(),0);

            if(flag.equals("name")){
                recyclerView.setVisibility(View.VISIBLE);
                searchname(SharedPreferenceController.getToken(getApplicationContext()),binding.searchEditText.getText().toString());
            }else if(flag.equals("doodle")){
                recyclerView.setVisibility(View.VISIBLE);
                searchdoodle(SharedPreferenceController.getToken(getApplicationContext()),binding.searchEditText.getText().toString());
            }

            }
        });


    }
    public void setBold(TextView v){
        v.setTextColor(Color.parseColor("#464D56"));
    }
    public void setRegular(TextView v){
        v.setTextColor(Color.parseColor("#30000000"));
    }

    public void searchname(String token, String keyword){
        DisposableObserver<Response<SearchNameResponse>>searchnameObserver = new DisposableObserver<Response<SearchNameResponse>>() {
            @Override
            public void onNext(Response<SearchNameResponse> searchNameResponseResponse) {
                if(searchNameResponseResponse.isSuccessful()){
                    searchNameItem = new ArrayList<>();
                    searchNameList = new ArrayList<>();
                    searchNameItem = searchNameResponseResponse.body().result;
                    if(searchNameItem.size()==0){
                        binding.searchLayoutResult.setVisibility(View.VISIBLE);
                    }else{
                        binding.searchLayoutResult.setVisibility(View.GONE);
                    }
                    for (int i=0;i<searchNameItem.size();i++){
                        searchNameList.add(new SearchNameModel(searchNameItem.get(i).nickname,searchNameItem.get(i).description,searchNameItem.get(i).image,searchNameItem.get(i).idx));

                    }
                    recyclerView.setLayoutManager(layoutManager_name);
                    adapter_name = new SearchNameAdapter(searchNameList,getApplicationContext(),requestManager);
                    recyclerView.setAdapter(adapter_name);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(searchnameObserver);
        SearchApi.searchname(token, keyword).subscribeWith(searchnameObserver);
    }
    public void searchdoodle(String token, String keyword){
        DisposableObserver<Response<SearchDoodleResponse>>searchdoodleObserver = new DisposableObserver<Response<SearchDoodleResponse>>() {
            @Override
            public void onNext(Response<SearchDoodleResponse> searchDoodleResponseResponse) {
                if (searchDoodleResponseResponse.isSuccessful()){
                    searchDoodleItem = new ArrayList<>();
                    searchDoodleList = new ArrayList<>();
                    searchDoodleItem = searchDoodleResponseResponse.body().result;

                    if(searchDoodleItem.size()==0){
                        binding.searchLayoutResult.setVisibility(View.VISIBLE);
                    }else{
                        binding.searchLayoutResult.setVisibility(View.GONE);
                    }

                    for(int i=0;i<searchDoodleItem.size();i++){
                        searchDoodleList.add(new SearchDoodleModel(searchDoodleItem.get(i).text,searchDoodleItem.get(i).image,searchDoodleItem.get(i).idx));
                    }
                    recyclerView.setLayoutManager(layoutManager_doodle);
                    adapter_doodle = new SearchDoodleAdapter(searchDoodleList,getApplicationContext(),requestManager);
                    recyclerView.setAdapter(adapter_doodle);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(searchdoodleObserver);
        SearchApi.searchdoodle(token, keyword).subscribeWith(searchdoodleObserver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
