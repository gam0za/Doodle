package com.gamza.jinyoungkim.doodle.view.doodle_other;


import java.io.Serializable;

public class OtherModel implements Serializable {
    public int idx;
    public String text;
    public String image;
    public int comment_count;
    public int scrap_count;
    public int like_count;
    public String created;
    public String updated;
    public int user_idx;
    public int scraps;
    public int like;

    public OtherModel(int idx, String text, String image, int comment_count, int scrap_count, int like_count, String created, String updated, int user_idx, int scraps, int like) {
        this.idx = idx;
        this.text = text;
        this.image = image;
        this.comment_count = comment_count;
        this.scrap_count = scrap_count;
        this.like_count = like_count;
        this.created = created;
        this.updated = updated;
        this.user_idx = user_idx;
        this.scraps = scraps;
        this.like = like;
    }

}
