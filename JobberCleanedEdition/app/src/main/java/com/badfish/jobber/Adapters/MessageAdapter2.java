package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.ChatFragment;
import com.badfish.jobber.Models.MessageModel;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<MessageModel> list;
    View v;
    int ITEM_RECEIVE = 2;
    int ITEM_SEND = 1;
    int ITEM_SEND_IMAGE = 3;
    int ITEM_RECEIVE_IMAGE = 4;


    public MessageAdapter2(Context context, ArrayList<MessageModel> list, View v) {
        this.context = context;
        this.v = v;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==ITEM_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.sendermessage, parent, false);

        }else{
            view = LayoutInflater.from(context).inflate(R.layout.receivermessage, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MessageModel messages = list.get(position);


        if(messages.getImageRef().equals("") && holder.messageImageView.getVisibility()==View.VISIBLE) {
            holder.messageImageView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.textviewmessage.setPadding(30,0,30,0);

            holder.textviewmessage.setLayoutParams(layoutParams);
        }
        if(!messages.getImageRef().equals("")){
            holder.textviewmessage.getLayoutParams().width=holder.asdLayout.getWidth()-10;

        }else{
            holder.messageImageView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.textviewmessage.setPadding(30,0,30,0);
        }
        int minutes = (int) ((messages.getTimestamp() / (1000 * 60)) % 60);
        int hours = (int) ((messages.getTimestamp() / (1000 * 60 * 60)) % 24);
        hours = hours + 2;
        String minute = String.valueOf(minutes);
        String hour = String.valueOf(hours);
        if (minute.length() == 1) {
            minute = "0" + minutes;
        }
        if (hour.length() == 1) {
            hour = "0" + hours;
        }
        if (hour.equals("24")) {
            hour = "00";
        }
        if (hour.equals("25")) {
            hour = "01";
        }

        if(messages.getMessage().equals("")){
                holder.textviewmessage.setText("");
                holder.textviewmessage.setVisibility(View.VISIBLE);
        }
        if(messages.getImageRef().equals("")) {
            //message doesn't have an image
            holder.textviewmessage.setText(messages.getMessage());
            holder.timeofmessage.setText(hour + ":" + minute);
            holder.textviewmessage.setVisibility(View.VISIBLE);

            holder.textviewmessage.setText(messages.getMessage());
            holder.timeofmessage.setText(hour + ":" + minute);
            holder.textviewmessage.setVisibility(View.VISIBLE);
        }else{
            holder.timeofmessage.setText(hour + ":" + minute);
            holder.messageImageView.setVisibility(View.VISIBLE);

            holder.progressBar.setVisibility(View.VISIBLE);

            if(!messages.getImageRef().equals("")){
                StorageReference storageRef= FirebaseStorage.getInstance().getReference("chatImages").child("MyChatImages")
                        .child(FirebaseAuth.getInstance().getUid()).child(MainActivity.mJobID).child(MainActivity.mOtherUID);
                storageRef.child(messages.getImageRef()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context)
                                .load(uri)
                                .into(holder.messageImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.progressBar.setVisibility(View.GONE);
                                        if(messages.getMessage().equals("")){
                                            holder.textviewmessage.setVisibility(View.GONE);
                                        }else{

                                            holder.textviewmessage.setPadding(30,0,30,0);
                                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            layoutParams.addRule(RelativeLayout.BELOW, R.id.messageImageView);
                                            holder.textviewmessage.setLayoutParams(layoutParams);
                                            holder.textviewmessage.getLayoutParams().width=holder.asdLayout.getWidth()-10;


                                            holder.textviewmessage.setVisibility(View.VISIBLE);
                                            holder.textviewmessage.setText(messages.getMessage());

                                        }
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                });
            }
        }
        holder.messageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.storageRef=FirebaseStorage.getInstance().getReference("chatImages").child("MyChatImages")
                        .child(FirebaseAuth.getInstance().getUid()).child(MainActivity.mJobID).child(MainActivity.mOtherUID);
                MainActivity.pictureRef= messages.getImageRef();


                ChatFragment.getInstance().showDialog();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel messages = list.get(position);

        if(FirebaseAuth.getInstance().getUid().equals(messages.getSenderID()))
        {
            if(messages.getImageRef().equals(""))
            {
                return ITEM_SEND;

            }else{
                return ITEM_SEND_IMAGE;
            }


        }else{
            if(messages.getImageRef().equals(""))
            {
                return ITEM_RECEIVE;

            }else{
                return ITEM_RECEIVE_IMAGE;
            }
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textviewmessage,timeofmessage,nameText;
        ShapeableImageView messageImageView;
        RelativeLayout asdLayout;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewmessage=itemView.findViewById(R.id.senderMessage);
            timeofmessage=itemView.findViewById(R.id.timeText);
            nameText=itemView.findViewById(R.id.nameText);
            messageImageView=itemView.findViewById(R.id.messageImageView);
            asdLayout=itemView.findViewById(R.id.asd);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder{
        TextView textviewmessage,timeofmessage,nameText;
        ShapeableImageView messageImageView;
        RelativeLayout asdLayout;
        ProgressBar progressBar;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            textviewmessage=itemView.findViewById(R.id.senderMessage);
            timeofmessage=itemView.findViewById(R.id.timeText);
            nameText=itemView.findViewById(R.id.nameText);
            messageImageView=itemView.findViewById(R.id.messageImageView);
            asdLayout=itemView.findViewById(R.id.asd);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

    }

}

