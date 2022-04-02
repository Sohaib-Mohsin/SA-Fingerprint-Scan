package com.biometric.safingerprintscan.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Add_ActivityScreen;
import com.biometric.safingerprintscan.Screens.Admin_MainScreen;
import com.biometric.safingerprintscan.Screens.Login_Screen;
import com.biometric.safingerprintscan.Screens.User_MainScreen;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends Fragment {

    View view;
    Button Save;
    TextView Username, St;
    EditText Name, CNIC, Address, Email, Password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    CircleImageView imageView;
    SharedPreferences sharedPreferences;
    String AS = "";
    Uri img = null;
    String imageURL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_, container, false);

        Save = view.findViewById(R.id.save);
        Username = view.findViewById(R.id.username);
        Name = view.findViewById(R.id.full_name);
        CNIC = view.findViewById(R.id.cnic);
        imageView = view.findViewById(R.id.image);
        Address = view.findViewById(R.id.address);
        Email = view.findViewById(R.id.email);
        Password = view.findViewById(R.id.password);
        St = view.findViewById(R.id.st);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating User");
        progressDialog.setCancelable(false);

        sharedPreferences = getContext().getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);
        AS = sharedPreferences.getString("AS", "null");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();
        storageReference = FirebaseStorage.getInstance("gs://sa-fingerprint-scan.appspot.com").getReference();

        if (AS.equals("Admin")) {
            St.setText("Admin");
            retrieveData("Admin");

        } else if (AS.equals("User")) {
            St.setText("Employee");
            retrieveData("Users_Data");
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                } else {
                    Pick();
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Name.getText().toString().isEmpty() && !Address.getText().toString().isEmpty() &&
                        !Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()) {

                    progressDialog.show();

                    if (img != null) {

                        StorageReference reference = storageReference.child("Profile_images").child(firebaseAuth.getUid() + ".jpg");
                        reference.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                Uri uri = uriTask.getResult();

                                if (uriTask.isSuccessful()) {

                                    String timeStamp = String.valueOf(System.currentTimeMillis());

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("username", Name.getText().toString());
                                    map.put("address", Address.getText().toString());
                                    map.put("email", Email.getText().toString());
                                    map.put("password", Password.getText().toString());
                                    map.put("imageURL", uri.toString());
                                    map.put("timeStamp", timeStamp);

                                    if (AS.equals("Admin")) {

                                        databaseReference.child("Admin").child(firebaseAuth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    HashMap<String, Object> myMap = new HashMap<>();
                                                    myMap.put("imageURL", uri.toString());
                                                    myMap.put("username", Name.getText().toString());

                                                    databaseReference.child("Admin_Status").child(firebaseAuth.getUid()).updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else if (AS.equals("User")) {

                                        databaseReference.child("Users_Data").child(firebaseAuth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    HashMap<String, Object> myMap = new HashMap<>();
                                                    myMap.put("imageURL", uri.toString());
                                                    myMap.put("username", Name.getText().toString());

                                                    databaseReference.child("User_Status").child(firebaseAuth.getUid()).updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }

                                }
                            }
                        });

                    } else {

                        String timeStamp = String.valueOf(System.currentTimeMillis());

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("username", Name.getText().toString());
                        map.put("address", Address.getText().toString());
                        map.put("email", Email.getText().toString());
                        map.put("password", Password.getText().toString());
                        map.put("imageURL", imageURL);
                        map.put("timeStamp", timeStamp);

                        if (AS.equals("Admin")) {

                            databaseReference.child("Admin").child(firebaseAuth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HashMap<String, Object> myMap = new HashMap<>();
                                        myMap.put("imageURL", imageURL);
                                        myMap.put("username", Name.getText().toString());

                                        databaseReference.child("Admin_Status").child(firebaseAuth.getUid()).updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else if (AS.equals("User")) {

                            databaseReference.child("Users_Data").child(firebaseAuth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HashMap<String, Object> myMap = new HashMap<>();
                                        myMap.put("imageURL", imageURL);
                                        myMap.put("username", Name.getText().toString());

                                        databaseReference.child("User_Status").child(firebaseAuth.getUid()).updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }

                } else {
                    Toast.makeText(getContext(), "Please fill all the fields correctly", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0) {
            boolean StorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (StorageAccepted) {
                Pick();
            } else {
                Toast.makeText(getContext(), "Please Enable Storage Permission!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            img = data.getData();
            imageView.setImageURI(img);
            Toast.makeText(getContext(), "Captured", Toast.LENGTH_SHORT).show();
        }
    }

    private void Pick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    private void retrieveData(String data) {

        databaseReference.child(data).child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    try {
                        Name.setText(snapshot.child("username").getValue().toString());
                        Username.setText(snapshot.child("username").getValue().toString());
                        CNIC.setText(snapshot.child("cnic").getValue().toString());
                        Address.setText(snapshot.child("address").getValue().toString());
                        Email.setText(firebaseAuth.getCurrentUser().getEmail());
                        Password.setText(snapshot.child("password").getValue().toString());
                        imageURL = snapshot.child("imageURL").getValue().toString();

                        try {

                            Glide.with(getContext()).load(imageURL).placeholder(R.mipmap.ic_avatar_round).into(imageView);

                        } catch (Exception ex) {
                            imageView.setImageResource(R.mipmap.ic_avatar_round);
                        }
                    } catch (Exception ex) {

                    }
                } else {
                    CNIC.setEnabled(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}