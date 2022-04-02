package com.biometric.safingerprintscan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biometric.safingerprintscan.Models.Chat_Model;
import com.biometric.safingerprintscan.Models.custom_messages_model;
import com.biometric.safingerprintscan.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Chat_Model> chat_modelArrayList;
    int SENDER_ID = 1, RECEIVER_ID=2;

    public Chat_Adapter(Context context, ArrayList<Chat_Model> chat_modelArrayList) {
        this.context = context;
        this.chat_modelArrayList = chat_modelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_ID) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_msg_layout, parent, false);
            return new Sender(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_msg_layout, parent, false);
            return new Receiver(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Chat_Model cm = chat_modelArrayList.get(position);

        if (holder.getClass() == Sender.class) {
           ((Sender) holder).Message.setText(cm.getMessage());

            long t = Long.parseLong(cm.getTime());

            Date date = new Date(t);
            SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
            String st = simple.format(date);

            ((Sender) holder).Time.setText(st);

        }
        else{
            ((Receiver) holder).Message.setText(cm.getMessage());

            long t = Long.parseLong(cm.getTime());

            Date date = new Date(t);
            SimpleDateFormat simple = new SimpleDateFormat("dd MMM hh:mm a");
            String st = simple.format(date);

            ((Receiver) holder).Time.setText(st);

            try{
                Glide.with(context).load(cm.getImageURL()).placeholder(R.mipmap.ic_avatar_round).into(((Receiver) holder).Image);
            }
            catch (Exception ex){
                ((Receiver) holder).Image.setImageResource(R.mipmap.ic_avatar_round);
            }

        }
    }

    @Override
    public int getItemCount() {
        return chat_modelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chat_modelArrayList.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_ID;
        } else {
            return RECEIVER_ID;
        }
    }

    static class Receiver extends RecyclerView.ViewHolder {

        TextView Message, Time;
        CircleImageView Image;

        public Receiver(@NonNull View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.messageView);
            Time = itemView.findViewById(R.id.time);
            Image = itemView.findViewById(R.id.image);

        }
    }

    static class Sender extends RecyclerView.ViewHolder {

        TextView Message, Time;
        CircleImageView Image;

        public Sender(@NonNull View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.messageView);
            Time = itemView.findViewById(R.id.time);
            Image = itemView.findViewById(R.id.image);

        }
    }
}
