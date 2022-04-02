package com.biometric.safingerprintscan.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.biometric.safingerprintscan.Fragments.Admin_Messages_Fragment;
import com.biometric.safingerprintscan.Fragments.Admin_Notification_Fragment;
import com.biometric.safingerprintscan.Fragments.Profile_Fragment;
import com.biometric.safingerprintscan.Fragments.SubmittedForm_Fragment;
import com.biometric.safingerprintscan.Fragments.User_Settings_Fragment;
import com.biometric.safingerprintscan.Notifications.Util;
import com.biometric.safingerprintscan.R;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_MainScreen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.headerName);
        TextView navUserMail = headerView.findViewById(R.id.headerEmail);
        CircleImageView navImage = headerView.findViewById(R.id.profile_image);

        try {

            String FORM = getIntent().getStringExtra("form");

            if (FORM.equals("form")) {

                getSupportActionBar().setTitle("Submitted Forms");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relative, new SubmittedForm_Fragment()).commit();

            }

        } catch (Exception ex) {

            toolbar.setTitle(null);
            FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction5.replace(R.id.relative, new User_Settings_Fragment()).commit();

        }

        FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference().child("Users_Data").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    try {

                        String username = snapshot.child("username").getValue().toString();
                        String imageURL = snapshot.child("imageURL").getValue().toString();

                        navUsername.setText(username);

                        try {
                            Glide.with(User_MainScreen.this).load(imageURL).placeholder(R.mipmap.ic_avatar_round).into(navImage);
                        } catch (Exception ex) {
                            navImage.setImageResource(R.mipmap.ic_avatar_round);
                        }
                    } catch (Exception ex) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile:

                        toolbar.setTitle("Profile");
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.relative, new Profile_Fragment()).addToBackStack("1").commit();

                        break;
                    case R.id.setting:

                        toolbar.setTitle(null);
                        FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction5.replace(R.id.relative, new User_Settings_Fragment()).commit();

                        break;
                    case R.id.logout:

                        AlertDialog.Builder alert = new AlertDialog.Builder(User_MainScreen.this);
                        alert.setCancelable(false).setMessage("Are you sure to logout?");
                        alert.setNegativeButton("Stay here", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Util util = new Util();
                                        util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()), "User_Status");

                                        FirebaseAuth.getInstance().signOut();
                                        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_AS", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("AS", "null");
                                        editor.commit();
                                        startActivity(new Intent(User_MainScreen.this, Login_Screen.class));
                                        finish();
                                    }
                                }).show();


                        break;
                    case R.id.help:

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(User_MainScreen.this);
                        alertDialog.setMessage("SA Fingerprint Scan");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.chat) {

            toolbar.setTitle("Messages");
            FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction5.replace(R.id.relative, new Admin_Messages_Fragment()).addToBackStack("3").commit();
        }
        if (item.getItemId() == R.id.notification) {

            toolbar.setTitle("Notifications");
            FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction5.replace(R.id.relative, new Admin_Notification_Fragment()).addToBackStack("4").commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Util util = new Util();
        util.updateOnlineStatus("online", "User_Status");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Util util = new Util();
            util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()), "User_Status");

        } catch (Exception ex) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util util = new Util();
        util.updateOnlineStatus("online", "User_Status");
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Util util = new Util();
            util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()), "User_Status");

        } catch (Exception ex) {

        }
    }
}