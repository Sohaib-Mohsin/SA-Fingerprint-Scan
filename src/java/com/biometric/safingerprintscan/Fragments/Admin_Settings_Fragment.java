package com.biometric.safingerprintscan.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biometric.safingerprintscan.Adapters.Custom_Admin_Adapter;
import com.biometric.safingerprintscan.Models.Custom_user_model;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Add_ActivityScreen;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Settings_Fragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    Custom_Admin_Adapter custom_admin_adapter;
    ArrayList<Custom_user_model> custom_user_modelArrayList;
    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView Username;
    CircleImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin__settings_, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        imageView = view.findViewById(R.id.image);
        Username = view.findViewById(R.id.username);
        fab = view.findViewById(R.id.floating);
        recyclerView = view.findViewById(R.id.recyclerView);
        custom_user_modelArrayList = new ArrayList<>();

        getMyInfo();
        getUsers();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Add_ActivityScreen.class));
            }
        });

        return view;
    }

    private void getUsers() {

        databaseReference.child("Users_Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {

                        custom_user_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Custom_user_model cm = dataSnapshot.getValue(Custom_user_model.class);
                            custom_user_modelArrayList.add(cm);
                        }
                        custom_admin_adapter = new Custom_Admin_Adapter(getContext(), custom_user_modelArrayList);
                        recyclerView.setAdapter(custom_admin_adapter);
                        custom_admin_adapter.notifyDataSetChanged();
                    } catch (Exception ex) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMyInfo() {

        databaseReference.child("Admin").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        String username = snapshot.child("username").getValue().toString();
                        String imageURL = snapshot.child("imageURL").getValue().toString();

                        Username.setText(username);
                        try {
                            Glide.with(getContext()).load(imageURL).placeholder(R.mipmap.ic_avatar_round).into(imageView);
                        } catch (Exception ex) {
                            imageView.setImageResource(R.mipmap.ic_avatar_round);
                        }
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