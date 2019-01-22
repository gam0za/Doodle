package com.gamza.jinyoungkim.doodle.view.doodle_other;

import java.util.ArrayList;

public class OtherResponse {
    public int status;
    public String message;
    public Result result;

    public class Result{
        public User user;
        public ArrayList<OtherModel> doodle;
    }

    public class User{
        public String nickname;
        public String profile;
        public String description;
        public int scrap_count;
        public int doodle_count;
    }

}

