package com.gamza.jinyoungkim.doodle.view.doodle_myfeed;

public class MyfeedProfileResponse {
    public int status;
    public String message;
    public Result result;

    public class Result{
        public int idx;
        public String nickname;
        public String description;
        public String image;
        public int doodle_count;
        public int scrap_count;
    }
}
