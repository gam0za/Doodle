package com.gamza.jinyoungkim.doodle.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceController {

    private static final String TOKEN = "token"; // for header
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";
    private static final String PROFILE = "profile";
    private static final String TUTORIAL = "tutorial";
    private static final String PW = "password";

    // profile
    public static void setProfile(Context context, String profile){
        SharedPreferences pref = context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PROFILE,profile);
        editor.commit();
    }

    public static String getProfile(Context context){
        SharedPreferences pref = context.getSharedPreferences(PROFILE, context.MODE_PRIVATE);
        String profile = pref.getString(PROFILE, "");
        return profile;
    }

    // email
    public static void setEmail(Context context, String email){
        SharedPreferences pref = context.getSharedPreferences(EMAIL,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public static String getEmail(Context context){
        SharedPreferences pref = context.getSharedPreferences(EMAIL, context.MODE_PRIVATE);
        String email = pref.getString(EMAIL,"");
        return email;
    }

    // pw
    public static void setPw(Context context, String pw){
        SharedPreferences pref = context.getSharedPreferences(PW, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PW,pw);
        editor.commit();
    }

    public static String getPw(Context context){
        SharedPreferences pref = context.getSharedPreferences(PW, context.MODE_PRIVATE);
        String pw = pref.getString(PW,"");
        return  pw;
    }


    //  token
    public static void setToken(Context context,String token){
        SharedPreferences pref = context.getSharedPreferences(TOKEN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN,token);
        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences pref = context.getSharedPreferences(TOKEN,context.MODE_PRIVATE);
        String token = pref.getString(TOKEN,"");
        return token;
    }

    // nickname
    public static void setNickname(Context context, String nickname){
        SharedPreferences pref = context.getSharedPreferences(NICKNAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(NICKNAME,nickname);
        editor.commit();
    }

    public static String getNickname(Context context){
        SharedPreferences pref = context.getSharedPreferences(NICKNAME,context.MODE_PRIVATE);
        String nickname = pref.getString(NICKNAME,"");
        return nickname;
    }

    // tutorial
    public static void setTutorial(Context context, String tutorial){
        SharedPreferences pref = context.getSharedPreferences(TUTORIAL,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TUTORIAL,tutorial);
        editor.commit();
    }

    public static String getTutorial(Context context){
        SharedPreferences pref = context.getSharedPreferences(TUTORIAL,context.MODE_PRIVATE);
        String tutorial = pref.getString(TUTORIAL,"");
        return tutorial;
    }


   }

