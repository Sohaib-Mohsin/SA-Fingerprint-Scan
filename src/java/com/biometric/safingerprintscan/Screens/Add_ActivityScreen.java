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
import android.widget.Spinner;
import android.widget.Toast;

import com.biometric.safingerprintscan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Add_ActivityScreen extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);

        Name = findViewById(R.id.full_name);
        CNIC = findViewById(R.id.cnic);
        Phone = findViewById(R.id.phone);
        Address = findViewById(R.id.address);
        City = findViewById(R.id.city);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        Month = findViewById(R.id.month);
        Date = findViewById(R.id.date);
        Year = findViewById(R.id.year);

        progressDialog = new ProgressDialog(Add_ActivityScreen.this);
        progressDialog.setMessage("Creating New User");
        progressDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(Add_ActivityScreen.this);
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        ID = firebaseAuth.getUid();

        monthAdp = new ArrayAdapter<String>(Add_ActivityScreen.this, android.R.layout.simple_spinner_item, mString);
        Month.setAdapter(monthAdp);

        dateAdp = new ArrayAdapter<String>(Add_ActivityScreen.this, android.R.layout.simple_spinner_item, dString);
        Date.setAdapter(dateAdp);

        yearAdp = new ArrayAdapter<String>(Add_ActivityScreen.this, android.R.layout.simple_spinner_item, yString);
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

    }

    public void create(View view) {

        if (!Name.getText().toString().isEmpty() && !month.equals("Month") && !date.equals("Date") && !year.equals("Year")
                && CNIC.getText().toString().length() == 13 && Phone.getText().toString().length() == 12 && !Address.getText().toString().isEmpty()
                && !City.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()) {

            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String timeStamp = String.valueOf(System.currentTimeMillis());

                        HashMap<String, String> map = new HashMap<>();
                        map.put("username", Name.getText().toString());
                        map.put("month", month);
                        map.put("date", date);
                        map.put("year", year);
                        map.put("uid", firebaseAuth.getUid());
                        map.put("cnic", CNIC.getText().toString());
                        map.put("address", Address.getText().toString());
                        map.put("phone", Phone.getText().toString());
                        map.put("city", City.getText().toString());
                        map.put("email", Email.getText().toString());
                        map.put("password", Password.getText().toString());
                        map.put("imageURL", "");
                        map.put("timeStamp", timeStamp);

                        databaseReference.child("Users_Data").child(firebaseAuth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    HashMap<String, String> map2 = new HashMap<>();
                                    map2.put("imageURL", "");
                                    map2.put("time", timeStamp);
                                    map2.put("Username", Name.getText().toString());
                                    map2.put("Last_msg", "Tap to Chat");
                                    map2.put("status", timeStamp);
                                    map2.put("uid", firebaseAuth.getUid());

                                    databaseReference.child("User_Status").child(firebaseAuth.getUid()).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(Add_ActivityScreen.this, Admin_MainScreen.class));
                                                finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(Add_ActivityScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Add_ActivityScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Add_ActivityScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(Add_ActivityScreen.this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

}