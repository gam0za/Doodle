package com.gamza.jinyoungkim.doodle.view.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.gamza.jinyoungkim.doodle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteFragment extends Fragment {

    RelativeLayout layout_write;

    public WriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_write, container, false);
        layout_write = (RelativeLayout)v.findViewById(R.id.layout_write);
        final Animation fade = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade);
        layout_write.startAnimation(fade);
        return v;
    }

}
