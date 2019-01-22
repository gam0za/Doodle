package com.gamza.jinyoungkim.doodle.view.signin;

public class SigninResult {

    public class Result{
        public class Profile{
            public String email;
            public String nickname;
            public String profile;
            public int idx;
            public String description;
        }
        public Profile profile;
        public String token;

    }

    public Result result;
}
