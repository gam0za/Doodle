package com.gamza.jinyoungkim.doodle.view.signin;

public class SigninModel {

    public String email;
    public String pw;
    public String token;

    public SigninModel(String email, String pw, String token) {
        this.email = email;
        this.pw = pw;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
