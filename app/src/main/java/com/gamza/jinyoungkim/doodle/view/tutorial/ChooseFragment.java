package com.gamza.jinyoungkim.doodle.view.tutorial;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.view.signin.SigninActivity;
import com.gamza.jinyoungkim.doodle.view.signup.SignupActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFragment extends Fragment {


    RelativeLayout signup;
    RelativeLayout login;

    public ChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choose, container, false);

        signup = (RelativeLayout)v.findViewById(R.id.signup);
        login = (RelativeLayout)v.findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),SignupActivity.class);
                startActivity(i);
                ((Activity)getActivity()).finish();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),SigninActivity.class);
                startActivity(i);
                ((Activity)getActivity()).finish();
            }
        });

        return v;
    }

}
