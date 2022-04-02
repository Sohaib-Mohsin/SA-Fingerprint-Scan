package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.biometric.safingerprintscan.Models.Custom_user_model;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Admin_MainScreen;
import com.biometric.safingerprintscan.Screens.User_EditScreen;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Custom_Admin_Adapter extends RecyclerView.Adapter<Custom_Admin_Adapter.viewHolder> {

    Context context;
    ArrayList<Custom_user_model> custom_user_modelArrayList;

    public Custom_Admin_Adapter(Context context, ArrayList<Custom_user_model> custom_user_modelArrayList) {
        this.context = context;
        this.custom_user_modelArrayList = custom_user_modelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_edit, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Custom_user_model custom_user_model = custom_user_modelArrayList.get(position);
        holder.username.setText(custom_user_model.getUsername());

        try {
            Glide.with(context).load(custom_user_model.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(holder.imageView);
        } catch (Exception ex) {
            holder.imageView.setImageResource(R.mipmap.ic_avatar_round);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("ID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uid", custom_user_model.getUid());
                editor.commit();

                Intent intent = new Intent(context, Admin_MainScreen.class);
                intent.putExtra("form", "form");
                context.startActivity(intent);

            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, User_EditScreen.class);
                intent.putExtra("uid", custom_user_model.getUid());
                context.startActivity(intent);

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Delete all forms of " + custom_user_model.getUsername() + " ?");
                alert.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference()
                                .child("Users_Form").child(custom_user_model.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "User forms deleted", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();

                                } else {
                                    Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return custom_user_modelArrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView username;
        ImageView editButton, deleteButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.username);
            editButton = itemView.findViewById(R.id.edit);
            deleteButton = itemView.findViewById(R.id.delete);

        }
    }

}
