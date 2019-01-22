package com.gamza.jinyoungkim.doodle.view.tutorial;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.gamza.jinyoungkim.doodle.R;
import com.gamza.jinyoungkim.doodle.databinding.ActivityTutorialBinding;
import com.gamza.jinyoungkim.doodle.util.SharedPreferenceController;
import com.gamza.jinyoungkim.doodle.view.splash.SplashActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    ActivityTutorialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_tutorial);

        binding.tutorialViewpager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        binding.tutorialViewpager.setCurrentItem(0);

        binding.indicator.setupWithViewPager(binding.tutorialViewpager);

        //permission check
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> arrayList) {

            }
        };

        TedPermission tedPermission = new TedPermission(this);
        tedPermission.setPermissionListener(permissionListener)
                .setDeniedMessage("[설정]>[권한] 에서 추후 허용하실 수 있습니다 :)")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();



        if (SharedPreferenceController.getTutorial(getApplicationContext()).equals("")){
            SharedPreferenceController.setTutorial(getApplicationContext(),"done");
        }else{
            Intent i = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(i);
            finish();
        }


    }

    private class pagerAdapter extends FragmentStatePagerAdapter{

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    WriteFragment writeFragment = new WriteFragment();
                    return writeFragment;

                case 1:
                    FeelFragment feelFragment = new FeelFragment();
                    return feelFragment;

                case 2:
                    MyFragment myFragment = new MyFragment();
                    return myFragment;

                case 3:
                    BookFragment bookFragment = new BookFragment();
                    return  bookFragment;

                case 4:
                    ChooseFragment chooseFragment = new ChooseFragment();
                    return  chooseFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }


    }
}
