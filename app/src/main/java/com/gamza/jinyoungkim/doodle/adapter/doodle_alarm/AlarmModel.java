package com.gamza.jinyoungkim.doodle.adapter.doodle_alarm;

public class AlarmModel {
    public int flag;
    public String image;
    public String created;
    public int doodle_idx;
    public String nickname;
    public int count;
    public int idx;

    public AlarmModel(int flag, String image, String created, int doodle_idx, String nickname, int count, int idx) {
        this.flag = flag;
        this.image = image;
        this.created = created;
        this.doodle_idx = doodle_idx;
        this.nickname = nickname;
        this.count = count;
        this.idx = idx;
    }
}
