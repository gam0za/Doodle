package com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment;

public class CommentModel {

    public int idx;
    public String content;
    public String created;
    public String updated;
    public int user_idx;
    public int doodle_idx;
    public int is_read;
    public String nickname;
    public String profile;

    public CommentModel(int idx, String content, String created, String updated, int user_idx, int doodle_idx, int is_read, String nickname, String profile) {
        this.idx = idx;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.user_idx = user_idx;
        this.doodle_idx = doodle_idx;
        this.is_read = is_read;
        this.nickname = nickname;
        this.profile = profile;
    }
}
