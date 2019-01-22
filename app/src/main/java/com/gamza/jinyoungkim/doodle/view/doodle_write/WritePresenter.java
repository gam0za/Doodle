package com.gamza.jinyoungkim.doodle.view.doodle_write;

import com.gamza.jinyoungkim.doodle.network.DoodleApi;
import com.gamza.jinyoungkim.doodle.network.NetworkService;
import com.gamza.jinyoungkim.doodle.util.GlobalApplication;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class WritePresenter implements WriteInterface.Presenter{

    WriteInterface.View view;
    WriteModel model;
    NetworkService networkService;

    public WritePresenter(WriteInterface.View view) {
        this.view = view;
        this.model = new WriteModel();
        networkService= GlobalApplication.getGlobalApplicationContext().getNetworkService();
    }

    @Override
    public void setText(String text) {
        model.setText(text);
        view.setText(model.getText());
    }

    @Override
    public void setFont(String font) {
        model.setFont(font);
        view.setFont(model.getFont());
    }

    @Override
    public void setFontSize(int fontSize) {
        model.setFontSize(fontSize);
        view.setFontSize(model.getFontSize());
    }

    @Override
    public void setLineSpacing(int lineSpacing) {
        model.setLineSpacing(lineSpacing);
        view.setlineSpacing(model.getLineSpacing());
    }

    @Override
    public void setFontColor(String fontColor) {
        model.setFontColor(fontColor);
        view.setFontColor(model.getFontColor());
    }

    @Override
    public void setFilterName(String filterName) {
        model.setFilterName(filterName);
        view.setFilterName(model.getFilterName());
    }

    @Override
    public void setFilterAlpha(int filterAlpha) {
        model.setFilterAlpha(filterAlpha);
        view.setFilterAlpha(model.getFilterAlpha());
    }

    @Override
    public void WritePost(String token, RequestBody text, MultipartBody.Part image) {

        DisposableObserver<Response<WriteResponse>> writeObserver = new DisposableObserver<Response<WriteResponse>>() {
            @Override
            public void onNext(Response<WriteResponse> writeResponseResponse) {
                if(writeResponseResponse.isSuccessful()){
                    view.writeResult("success");
                }
            }

            @Override
            public void onError(Throwable e) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }

            @Override
            public void onComplete() {

            }
        };

        DoodleApi.write(token,text,image).subscribeWith(writeObserver);

    }
}
