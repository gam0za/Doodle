package com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search;

public class SearchNameModel {
    public String nickname;
    public String description;
    public String image;
    public int idx;

    public SearchNameModel(String nickname, String description, String image, int idx) {
        this.nickname = nickname;
        this.description = description;
        this.image = image;
        this.idx = idx;
    }
}
