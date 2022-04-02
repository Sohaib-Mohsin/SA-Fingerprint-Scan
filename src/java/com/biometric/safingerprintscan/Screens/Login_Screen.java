package com.biometric.safingerprintscan.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.biometric.safingerprintscan.Adapters.ViewPagerAdapter;
import com.biometric.safingerprintscan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Screen extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences =  getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            String AS = sharedPreferences.getString("AS", "null");
            if (AS.equals("Admin")) {
                startActivity(new Intent(Login_Screen.this, Admin_MainScreen.class));
                finish();
            } else if (AS.equals("User")) {
                startActivity(new Intent(Login_Screen.this, User_MainScreen.class));
                finish();
            }
        }

    }
}