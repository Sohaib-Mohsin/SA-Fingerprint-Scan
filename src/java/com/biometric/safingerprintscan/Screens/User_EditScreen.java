package com.biometric.safingerprintscan.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.biometric.safingerprintscan.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_EditScreen extends AppCompatActivity {

    Spinner Month, Date, Year;
    EditText Name, CNIC, Phone, Address, City, Email, Password;
    ArrayAdapter<String> monthAdp, dateAdp, yearAdp;
    String[] mString = {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"},
            dString = {"Date", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                    "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"},
            yString = {"Year", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001",
                    "2002", "2003", "2004"};
    String date, month, year;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String ID = "";
    TextView title;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_screen);

        Name = findViewById(R.id.full_name);
        CNIC = findViewById(R.id.cnic);
        Phone = findViewById(R.id.phone);
        Address = findViewById(R.id.address);
        City = findViewById(R.id.city);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        title = findViewById(R.id.reg_ttl);
        imageView = findViewById(R.id.image);

        Month = findViewById(R.id.month);
        Date = findViewById(R.id.date);
        Year = findViewById(R.id.year);

        progressDialog = new ProgressDialog(User_EditScreen.this);
        progressDialog.setMessage("Updating User");
        progressDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(User_EditScreen.this);
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        ID = getIntent().getStringExtra("uid");

        monthAdp = new ArrayAdapter<String>(User_EditScreen.this, android.R.layout.simple_spinner_item, mString);
        Month.setAdapter(monthAdp);

        dateAdp = new ArrayAdapter<String>(User_EditScreen.this, android.R.layout.simple_spinner_item, dString);
        Date.setAdapter(dateAdp);

        yearAdp = new ArrayAdapter<String>(User_EditScreen.this, android.R.layout.simple_spinner_item, yString);
        Year.setAdapter(yearAdp);

        Month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = mString[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                date = dString[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = yString[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getData();

    }

    private void getData() {

        databaseReference.child("Users_Data").child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        title.setText(snapshot.child("username").getValue().toString());
                        Name.setText(snapshot.child("username").getValue().toString());
                        Address.setText(snapshot.child("address").getValue().toString());
                        CNIC.setText(snapshot.child("cnic").getValue().toString());
                        Phone.setText(snapshot.child("phone").getValue().toString());
                        City.setText(snapshot.child("city").getValue().toString());
                        Email.setText(snapshot.child("email").getValue().toString());
                        Password.setText(snapshot.child("password").getValue().toString());

                        String m = snapshot.child("month").getValue().toString();
                        String d = snapshot.child("date").getValue().toString();
                        String y = snapshot.child("year").getValue().toString();

                        month = m;
                        date = d;
                        year = y;

                        try {
                            Glide.with(User_EditScreen.this).load(snapshot.child("imageURL").getValue().toString()).placeholder(R.mipmap.ic_avatar_round).into(imageView);

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

    public void update(View view) {

        if (!Name.getText().toString().isEmpty() && !month.equals("Month") && !date.equals("Date") && !year.equals("Year")
                && CNIC.getText().toString().length() == 13 && Phone.getText().toString().length() == 12 && !Address.getText().toString().isEmpty()
                && !City.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()) {

            progressDialog.show();

            String timeStamp = String.valueOf(System.currentTimeMillis());

            HashMap<String, Object> map = new HashMap<>();
            map.put("username", Name.getText().toString());
            map.put("month", month);
            map.put("date", date);
            map.put("year", year);
            map.put("cnic", CNIC.getText().toString());
            map.put("address", Address.getText().toString());
            map.put("phone", Phone.getText().toString());
            map.put("city", City.getText().toString());
            map.put("email", Email.getText().toString());
            map.put("password", Password.getText().toString());
            map.put("imageURL", "");
            map.put("timeStamp", timeStamp);

            databaseReference.child("Users_Data").child(ID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("first_name", Name.getText().toString());
                        map2.put("uid", ID);

                        databaseReference.child("U_Notifications").child(ID).child(timeStamp).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(User_EditScreen.this, Admin_MainScreen.class));
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(User_EditScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                        progressDialog.dismiss();
                        startActivity(new Intent(User_EditScreen.this, Admin_MainScreen.class));
                        finish();
                    } else {
                        Toast.makeText(User_EditScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(User_EditScreen.this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

}