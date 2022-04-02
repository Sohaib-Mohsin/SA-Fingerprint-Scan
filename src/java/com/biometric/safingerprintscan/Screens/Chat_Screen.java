package com.biometric.safingerprintscan.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.biometric.safingerprintscan.Adapters.Chat_Adapter;
import com.biometric.safingerprintscan.Fragments.Admin_Messages_Fragment;
import com.biometric.safingerprintscan.Models.Chat_Model;
import com.biometric.safingerprintscan.Models.custom_messages_model;
import com.biometric.safingerprintscan.Notifications.AllConstants;
import com.biometric.safingerprintscan.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Screen extends AppCompatActivity {

    TextView Name, Status;
    CircleImageView Image;
    ImageView Send, Back;
    EditText Message;
    Chat_Adapter chat_adapter;
    ArrayList<Chat_Model> chat_modelArrayList;
    RecyclerView recyclerView;
    String R_Name = "", R_ID = "", R_Img = "", S_ID, S_Room = "", R_Room = "", My_Image = "";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        getWindow().setStatusBarColor(getResources().getColor(R.color.l_purple));

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(Chat_Screen.this);
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();

        R_Name = getIntent().getStringExtra("username");
        R_ID = getIntent().getStringExtra("uid");
        R_Img = getIntent().getStringExtra("imageURL");
        S_ID = firebaseAuth.getUid();

        S_Room = S_ID + R_ID;
        R_Room = R_ID + S_ID;

        Name = findViewById(R.id.name);
        Status = findViewById(R.id.status);
        Send = findViewById(R.id.send);
        Message = findViewById(R.id.msg);
        Back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        Image = findViewById(R.id.image);

        chat_modelArrayList = new ArrayList<>();

        Name.setText(R_Name);

        try {
            Glide.with(Chat_Screen.this).load(R_Img).placeholder(R.mipmap.ic_avatar_round).into(Image);
        } catch (Exception ex) {
            Image.setImageResource(R.mipmap.ic_avatar_round);
        }

        getLastSeen();
        getMyImage();
        getMessages();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Message.getText().toString().length() < 1) {
                    Message.setError("Unable to send empty text!");
                } else {
                    SendMessage(Message.getText().toString());
                    Message.setText(null);
                }

            }
        });

    }

    private void getMyImage() {

       SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        String AS = sharedPreferences.getString("AS", "null");

        if (AS.equals("Admin")) {

        FirebaseDatabase.getInstance().getReference().child("Admin_Status").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                My_Image = snapshot.child("imageURL").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        } else if (AS.equals("User")) {

            FirebaseDatabase.getInstance().getReference().child("User_Status").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    My_Image = snapshot.child("imageURL").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void getLastSeen() {

        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);

        String AS = sharedPreferences.getString("AS", "null");
        if (AS.equals("Admin")) {
            FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference("User_Status").child(R_ID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                String lastSeen = snapshot.child("status").getValue().toString();

                                if (lastSeen.equals("online")) {
                                    Status.setText("Active Now");
                                } else {

                                    long t = Long.parseLong(lastSeen);

                                    Date date = new Date(t);
                                    SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
                                    String st = simple.format(date);

                                    Status.setText("Last seen at " + st);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else if (AS.equals("User")) {
            FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference("Admin_Status").child(R_ID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                String lastSeen = snapshot.child("status").getValue().toString();

                                if (lastSeen.equals("online")) {
                                    Status.setText("Active Now");
                                } else {

                                    long t = Long.parseLong(lastSeen);

                                    Date date = new Date(t);
                                    SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
                                    String st = simple.format(date);

                                    Status.setText("Last seen at " + st);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }


    }

    private void getMessages() {

        databaseReference.child("Chats").child(S_Room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try {
                        chat_modelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Chat_Model chat_model = dataSnapshot.getValue(Chat_Model.class);
                            chat_modelArrayList.add(chat_model);
                        }

                        chat_adapter = new Chat_Adapter(Chat_Screen.this, chat_modelArrayList);
                        recyclerView.setAdapter(chat_adapter);
                        chat_adapter.notifyDataSetChanged();

                    } catch (Exception ex) {

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SendMessage(String message) {

        String time = String.valueOf(System.currentTimeMillis());

        Chat_Model chat_model = new Chat_Model(message, time, "true", My_Image, FirebaseAuth.getInstance().getUid());

        databaseReference.child("Chats").child(S_Room).child(time).setValue(chat_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("Chats").child(R_Room).child(time).setValue(chat_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Chat_Screen.this, "Sent", Toast.LENGTH_SHORT).show();
                                getToken(message, R_ID, R_Img);

                            } else {
                                Toast.makeText(Chat_Screen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(Chat_Screen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void getToken(String Message, String hisID, String myImage) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(hisID);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String token = snapshot.child("token_id").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("title", "New Message");
                    data.put("message", Message);
                    data.put("hisID", hisID);
                    data.put("hisImage", myImage);
//                    data.put("chatID", chatID);

                    to.put("to", token);
                    to.put("data", data);

                    sendNotification(to);

                } catch (Exception e) {
                    Toast.makeText(Chat_Screen.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL, to, response -> {
//            Toast.makeText(Chat_Screen.this, "sendNotification: " + response, Toast.LENGTH_SHORT).show();
            Log.d("notification", "sendNotification: " + response);
        }, error -> {
            Toast.makeText(Chat_Screen.this, "sendNotification: " + error, Toast.LENGTH_SHORT).show();

            Log.d("notification", "sendNotification: " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + AllConstants.SERVER_KEY);
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}