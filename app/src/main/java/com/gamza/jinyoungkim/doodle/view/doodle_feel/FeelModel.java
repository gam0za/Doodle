package com.gamza.jinyoungkim.doodle.view.doodle_feel;

import java.io.Serializable;

public class FeelModel implements Serializable {

    public String image;
    public String text;
    public int idx;
    public int comment_count;
    public int scrap_count;
    public int like_count;
    public int user_idx;
    public String nickname;
    public String profile;

    public int scraps;
    public int like;

    public String created;


    public FeelModel(String image, String text, int idx, int comment_count, int scrap_count, int like_count, int user_idx, String nickname, String profile, int scraps, int like, String created) {
        this.image = image;
        this.text = text;
        this.idx = idx;
        this.comment_count = comment_count;
        this.scrap_count = scrap_count;
        this.like_count = like_count;
        this.user_idx = user_idx;
        this.nickname = nickname;
        this.profile = profile;
        this.scraps = scraps;
        this.like = like;
        this.created = created;
    }

}
