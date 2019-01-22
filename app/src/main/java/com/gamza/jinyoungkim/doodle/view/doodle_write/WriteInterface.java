package com.gamza.jinyoungkim.doodle.view.doodle_write;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface WriteInterface {
    interface View {
        void setText(String text);
        void setFont(String font);
        void setFontSize(int fontSize);
        void setlineSpacing(int lineSpacing);
        void setFontColor(String fontColor);
        void setFilterName(String filterName);
        void setFilterAlpha(int filterAlpha);
        void writeResult(String result);
    }
    interface  Presenter {

        void setText(String text);
        void setFont(String font);
        void setFontSize(int fontSize);
        void setLineSpacing(int lineSpacing);
        void setFontColor(String fontColor);
        void setFilterName(String filterName);
        void setFilterAlpha(int filterAlpha);

        void WritePost(String token, RequestBody text, MultipartBody.Part image);
    }
}
