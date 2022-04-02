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

import com.biometric.safingerprintscan.Adapters.Admin_Messages_Adapter;
import com.biometric.safingerprintscan.Models.custom_messages_model;
import com.biometric.safingerprintscan.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Messages_Fragment extends Fragment {

    View view;
    Admin_Messages_Adapter admin_messages_adapter;
    ArrayList<custom_messages_model> custom_messages_modelArrayList;
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
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.notification);
        item2.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_messages, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        custom_messages_modelArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        sharedPreferences = getContext().getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        String AS = sharedPreferences.getString("AS", "null");
        if (AS.equals("Admin")) {
            getAdminChat();

        } else if (AS.equals("User")) {
            getUserChat();
        }

        return view;

    }

    private void getAdminChat() {
        databaseReference.child("User_Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    custom_messages_modelArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        custom_messages_model cm = dataSnapshot.getValue(custom_messages_model.class);
                        custom_messages_modelArrayList.add(cm);

                    }
                    admin_messages_adapter = new Admin_Messages_Adapter(getContext(), custom_messages_modelArrayList);
                    recyclerView.setAdapter(admin_messages_adapter);
                    admin_messages_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserChat() {

        databaseReference.child("Admin_Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try {
                        custom_messages_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            custom_messages_model cm = dataSnapshot.getValue(custom_messages_model.class);
                            custom_messages_modelArrayList.add(cm);

                        }
                        admin_messages_adapter = new Admin_Messages_Adapter(getContext(), custom_messages_modelArrayList);
                        recyclerView.setAdapter(admin_messages_adapter);
                        admin_messages_adapter.notifyDataSetChanged();

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