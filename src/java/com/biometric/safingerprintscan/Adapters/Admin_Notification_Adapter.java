package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Notification_Adapter extends RecyclerView.Adapter<Admin_Notification_Adapter.viewHolder>{

    Context context;
    ArrayList<Custom_user_model> custom_user_modelArrayList;

    public Admin_Notification_Adapter(Context context, ArrayList<Custom_user_model> custom_user_modelArrayList) {
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
        holder.Txt.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);
        String AS = sharedPreferences.getString("AS", "null");
        if (AS.equals("Admin")) {
            holder.Txt.setText("Form Submitted");
        } else if (AS.equals("User")) {
            holder.Txt.setText("Profile Updated");
        }


        try{
            Glide.with(context).load(custom_user_model.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(holder.imageView);
        }
        catch (Exception ex){
            holder.imageView.setImageResource(R.mipmap.ic_avatar_round);
        }

        holder.viewButton.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return custom_user_modelArrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView username, Txt;
        Button viewButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.username);
            viewButton = itemView.findViewById(R.id.view);
            Txt = itemView.findViewById(R.id.txt);

        }
    }

}
