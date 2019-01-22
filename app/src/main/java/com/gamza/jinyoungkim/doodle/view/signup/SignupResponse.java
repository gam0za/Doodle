package com.gamza.jinyoungkim.doodle.view.signup;

public class SignupResponse {
    public int status;
    public String message;
    public Result result;

    public class Result{
        public int idx;
        public String email;
        public String nickname;
        public String created;
        public String image;

        }
}
