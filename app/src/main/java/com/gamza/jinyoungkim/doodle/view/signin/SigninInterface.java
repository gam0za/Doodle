package com.gamza.jinyoungkim.doodle.view.signin;

import android.content.Context;

public interface SigninInterface {

    interface View {
        void login(String result);
    }

    interface Presenter {
        void setSigninPost(String email, String pw, Context context);
    }
}
