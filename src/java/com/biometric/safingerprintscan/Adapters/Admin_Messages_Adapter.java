package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biometric.safingerprintscan.Models.custom_messages_model;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Chat_Screen;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Messages_Adapter extends RecyclerView.Adapter<Admin_Messages_Adapter.ViewHolder> {

    Context context;
    ArrayList<custom_messages_model> custom_user_modelArrayList;

    public Admin_Messages_Adapter(Context context, ArrayList<custom_messages_model> custom_user_modelArrayList) {
        this.context = context;
        this.custom_user_modelArrayList = custom_user_modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_messages_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        custom_messages_model cm = custom_user_modelArrayList.get(position);

        holder.username.setText(cm.getUsername());

        String st = cm.getStatus();

        if (st.equals("online")) {
            holder.Dot.setVisibility(View.VISIBLE);
        } else {
            holder.Dot.setVisibility(View.GONE);

        }

        try {
            Glide.with(context).load(cm.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(holder.imageView);
        } catch (Exception ex) {
            holder.imageView.setImageResource(R.mipmap.ic_avatar_round);
        }

        FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference().child("Chats").
                child(FirebaseAuth.getInstance().getUid() + cm.getUid()).orderByChild("time").limitToLast(1).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        holder.last_msg.setText(dataSnapshot.child("message").getValue().toString());

                        long t = Long.parseLong(dataSnapshot.child("time").getValue().toString());

                        Date date = new Date(t);
                        SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
                        String st = simple.format(date);

                        holder.time.setText(st);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chat_Screen.class);
                intent.putExtra("username", cm.getUsername());
                intent.putExtra("uid", cm.getUid());
                intent.putExtra("imageURL", cm.getImageURL());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return custom_user_modelArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView, Dot;
        TextView username, last_msg, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.name);
            last_msg = itemView.findViewById(R.id.last_message);
            time = itemView.findViewById(R.id.time);
            Dot = itemView.findViewById(R.id.dot);

        }
    }
}
