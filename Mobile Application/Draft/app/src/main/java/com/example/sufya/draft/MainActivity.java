package com.example.sufya.draft;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sufya.draft.registration_login_profile_system.LoginFragment;
import com.example.sufya.draft.navigation.HomeNavigation;

/**
 * @Author Ravi Tamada
 * @website http://www.androidhive.info/2016/05/android-build-intro-slider-app/
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("myprefs", 0);
        initFragment();

    }

    private void initFragment(){
        Fragment fragment;
        if(pref.getBoolean(Constants.IS_LOGGED_IN,false)){

            //False
            fragment = new HomeNavigation();
        }else {

            //True
            fragment = new LoginFragment();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }

}
