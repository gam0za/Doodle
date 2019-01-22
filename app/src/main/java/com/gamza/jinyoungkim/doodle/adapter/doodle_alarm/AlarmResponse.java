package com.gamza.jinyoungkim.doodle.adapter.doodle_alarm;

import java.util.ArrayList;

public class AlarmResponse {
    public int status;
    public String message;
    public Result result;

    public class Result{
        public ArrayList<AlarmModel> not_read;
        public ArrayList<AlarmModel> is_read;
    }
}
