package com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment;

import java.util.ArrayList;

public class CommentResponse {
    public int status;
    public String message;
    public Result result;

    public class Result{

        public ArrayList<CommentModel> comments;
        public Doodle doodle;

    }
    public class Doodle{
        public String nickname;
        public String image;
        public int scrap_count;
        public int comment_count;
        public int like_count;
        public String created;

        public Doodle(String nickname, String image, int scrap_count, int comment_count, int like_count, String created) {
            this.nickname = nickname;
            this.image = image;
            this.scrap_count = scrap_count;
            this.comment_count = comment_count;
            this.like_count = like_count;
            this.created = created;
        }
    }
}
