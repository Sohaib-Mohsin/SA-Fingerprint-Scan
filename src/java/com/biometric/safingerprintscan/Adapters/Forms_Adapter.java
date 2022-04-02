package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.biometric.safingerprintscan.Models.Custom_user_model;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.View_Form;
import com.bumptech.glide.Glide;
import com.rajat.pdfviewer.PdfViewerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Forms_Adapter extends RecyclerView.Adapter<Forms_Adapter.viewHolder>{

    Context context;
    ArrayList<Custom_user_model> custom_user_modelArrayList;

    public Forms_Adapter(Context context, ArrayList<Custom_user_model> custom_user_modelArrayList) {
        this.context = context;
        this.custom_user_modelArrayList = custom_user_modelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.form_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Custom_user_model custom_user_model = custom_user_modelArrayList.get(position);
        holder.username.setText(custom_user_model.getFirst_name());

        long t = Long.parseLong(custom_user_model.getTimeStamp());
        Date date = new Date(t);
        SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
        String st = simple.format(date);

        holder.time.setText(st);

        try{
            Glide.with(context).load(custom_user_model.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(holder.imageView);
        }
        catch (Exception ex){
            holder.imageView.setImageResource(R.mipmap.ic_avatar_round);
        }

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, View_Form.class);
                intent.putExtra("form_url", custom_user_model.getForm_url());
                context.startActivity(intent);

            }
        });

        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);
        String AS = sharedPreferences.getString("AS", "null");
         if (AS.equals("User")) {
            holder.download.setVisibility(View.GONE);
        }

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(custom_user_model.getForm_url()));
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
        TextView username, viewButton, download, time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            viewButton = itemView.findViewById(R.id.view);
            download = itemView.findViewById(R.id.download);

        }
    }

}
