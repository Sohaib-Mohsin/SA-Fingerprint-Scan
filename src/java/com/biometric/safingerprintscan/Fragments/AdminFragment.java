package com.biometric.safingerprintscan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Admin_MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment {

    View view;
    String LOGIN_AS = "Admin";
    AlertDialog dialog;
    EditText Email, Password;
    Button Login;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin, container, false);

        Email = view.findViewById(R.id.email);
        Password = view.findViewById(R.id.password);
        Login = view.findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());

        sharedPreferences = getContext().getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        show_selection();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Email.getText().toString().length() > 0 && Password.getText().toString().length() > 0) {
                    Login(Email.getText().toString(), Password.getText().toString(), LOGIN_AS);

                } else {
                    Toast.makeText(getContext(), "Please fill in both fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void Login(String email, String password, String login_as) {

        if (login_as.equals("Admin") && email.equals("Admin@gmail.com")) {
            dialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(data -> {

                            String token = data.getResult().getToken();
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("token_id", token);
                            tokenMap.put("uid", FirebaseAuth.getInstance().getUid());

                            FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference()
                                    .child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("AS", "Admin");
                                        editor.commit();
                                        dialog.dismiss();
                                        startActivity(new Intent(getContext(), Admin_MainScreen.class));
                                        getActivity().finish();

                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        });


                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }

    void show_selection() {

        AlertDialog.Builder alertDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(getContext());
        }
        View v = getLayoutInflater().inflate(R.layout.select_login_layout, null);

        alertDialog.setView(v);
        dialog = alertDialog.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    }


}