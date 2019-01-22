package com.gamza.jinyoungkim.doodle.network;

import com.gamza.jinyoungkim.doodle.util.GlobalApplication;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchDoodleResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchNameResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchApi {

    public static Observable<Response<SearchNameResponse>> searchname(String token, String keyword){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return  networkService.searchname(token, keyword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<SearchDoodleResponse>> searchdoodle(String token, String keyword){
        NetworkService networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        return networkService.searchdoodle(token, keyword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
