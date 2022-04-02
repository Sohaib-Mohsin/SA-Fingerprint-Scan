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

import com.biometric.safingerprintscan.Adapters.Forms_Adapter;
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

public class SubmittedForm_Fragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Forms_Adapter forms_adapter;
    ArrayList<Custom_user_model> custom_user_modelArrayList;

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
        item2.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_submitted_form, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();
        custom_user_modelArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ID", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("uid", "null");

        if (id.equals("null")) {
            getForms(firebaseAuth.getUid());
        } else {
            getForms(id);
        }
        return view;
    }

    private void getForms(String id) {

        databaseReference.child("Users_Form").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        custom_user_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Custom_user_model cm = dataSnapshot.getValue(Custom_user_model.class);
                            custom_user_modelArrayList.add(cm);
                        }
                        forms_adapter = new Forms_Adapter(getContext(), custom_user_modelArrayList);
                        recyclerView.setAdapter(forms_adapter);
                        forms_adapter.notifyDataSetChanged();
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