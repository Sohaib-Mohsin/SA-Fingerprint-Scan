package com.biometric.safingerprintscan.Offline;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class offline extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").setPersistenceEnabled(true);
    }
}
