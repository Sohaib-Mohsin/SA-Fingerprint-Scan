package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biometric.safingerprintscan.Models.Custom_user_model;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.User_MainScreen;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Custom_User_Adapter extends RecyclerView.Adapter<Custom_User_Adapter.viewHolder>{

    Context context;
    ArrayList<Custom_user_model> custom_user_modelArrayList;

    public Custom_User_Adapter(Context context, ArrayList<Custom_user_model> custom_user_modelArrayList) {
        this.context = context;
        this.custom_user_modelArrayList = custom_user_modelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_user_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Custom_user_model custom_user_model = custom_user_modelArrayList.get(position);
        holder.username.setText(custom_user_model.getFirst_name());

        try{
            Glide.with(context).load(custom_user_model.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(holder.imageView);
        }
        catch (Exception ex){
            holder.imageView.setImageResource(R.mipmap.ic_avatar_round);
        }

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("ID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uid", custom_user_model.getUid());
                editor.commit();

                Intent intent = new Intent(context, User_MainScreen.class);
                intent.putExtra("form","form");
                context.startActivity(intent);

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
        Button viewButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.username);
            viewButton = itemView.findViewById(R.id.view);

        }
    }

}
