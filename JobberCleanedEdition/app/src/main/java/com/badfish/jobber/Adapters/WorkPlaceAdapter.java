package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.badfish.jobber.Services.FcmNotificationsSender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkPlaceAdapter extends RecyclerView.Adapter<WorkPlaceAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<WorkPlaceModel> list;
    View v;

    public WorkPlaceAdapter(Context context, ArrayList<WorkPlaceModel> list,Activity activity) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.vapaattyopaikatmodel, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.etäisyys.setText("Noin "+model.getDistance()+" kilometrin päässä");
        holder.name.setText(model.getEmployerName());
        holder.työnkuva.setText(model.getJobDescription());

        holder.hyväksyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.hyväksyBtn.setClickable(false);
                String token = model.getJobToken();
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"TEEST","TAST",context,activity);
                notificationsSender.SendNotifications(token,"0","worker");
                FirebaseDatabase.getInstance().getReference().child("MyAcceptedJobs").child(FirebaseAuth.getInstance().getUid())
                        .child(model.getId()).setValue(model);
                FirebaseDatabase.getInstance().getReference().child("MyAvailableWorkers")
                        .child(model.getEmployerUID())
                        .child(model.getId())
                        .child(FirebaseAuth.getInstance().getUid()).setValue(model);
                FirebaseDatabase.getInstance().getReference("MyAvailableJobs").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).removeValue();
                holder.hyväksyBtn.setText("Accepted");
                holder.hyväksyBtn.setTextColor(Color.parseColor("#1EC31A"));
                list.remove(model);
            }
        });

        holder.kieltäydyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView työnkuva,etäisyys,name;
        TextView hyväksyBtn,kieltäydyBtn;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.työpaikkaView);
            työnkuva=itemView.findViewById(R.id.työnkuvaText);
            etäisyys=itemView.findViewById(R.id.etäisyystext);
            name=itemView.findViewById(R.id.name);
            hyväksyBtn=itemView.findViewById(R.id.hyväksyButton);
            kieltäydyBtn=itemView.findViewById(R.id.kieltäydyButton);
        }

        @Override
        public void onClick(View view) {



        }
    }

}

