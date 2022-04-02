package com.biometric.safingerprintscan.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biometric.safingerprintscan.Adapters.Admin_Notification_Adapter;
import com.biometric.safingerprintscan.Models.Custom_user_model;
import com.biometric.safingerprintscan.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Notification_Fragment extends Fragment {

    View view;
    Admin_Notification_Adapter admin_notification_adapters;
    ArrayList<Custom_user_model> custom_user_modelArrayList;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        MenuItem item = menu.findItem(R.id.chat);
        item.setVisible(true);
        MenuItem item2 = menu.findItem(R.id.notification);
        item2.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_notification, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        custom_user_modelArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        sharedPreferences = getContext().getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        String AS = sharedPreferences.getString("AS", "null");
        if (AS.equals("Admin")) {
            getAdminNotifications();
        } else if (AS.equals("User")) {
            getUserNotification();
        }

        return view;
    }

    private void getAdminNotifications() {

        databaseReference.child("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try {
                        custom_user_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Custom_user_model cm = dataSnapshot.getValue(Custom_user_model.class);
                            custom_user_modelArrayList.add(cm);
                        }
                        admin_notification_adapters = new Admin_Notification_Adapter(getContext(), custom_user_modelArrayList);
                        recyclerView.setAdapter(admin_notification_adapters);
                        admin_notification_adapters.notifyDataSetChanged();

                    } catch (Exception ex) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserNotification() {

        databaseReference.child("U_Notifications").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try {
                        custom_user_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Custom_user_model cm = dataSnapshot.getValue(Custom_user_model.class);
                            custom_user_modelArrayList.add(cm);
                        }
                        admin_notification_adapters = new Admin_Notification_Adapter(getContext(), custom_user_modelArrayList);
                        recyclerView.setAdapter(admin_notification_adapters);
                        admin_notification_adapters.notifyDataSetChanged();

                    } catch (Exception ex) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}