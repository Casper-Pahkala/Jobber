package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<WorkPlaceModel> list;

    public WorkerAdapter(Context context, ArrayList<WorkPlaceModel> list, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public WorkerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.available_worker_model, parent, false);
        return new WorkerAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerAdapter.MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.name.setText(model.getWorkerName());

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.acceptBtn.setClickable(false);
                holder.progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("MyAvailableWorkers").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).child(model.getWorkerUserID())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).child(model.getWorkerUserID()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(model.getWorkerUserID()).child(model.getId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("MyAcceptedJobs")
                                                        .child(model.getWorkerUserID()).child(model.getId()).child("status").setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                holder.progressBar.setVisibility(View.GONE);

                                                                holder.acceptBtn.setText("Accepted");
                                                                holder.acceptBtn.setTextColor(Color.parseColor("#1EC31A"));
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });

                            }
                        });


            }
        });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
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
        TextView name;
        TextView acceptBtn,declineBtn;
        ProgressBar progressBar;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.workerName);
            acceptBtn=itemView.findViewById(R.id.hyväksyButton);
            declineBtn=itemView.findViewById(R.id.kieltäydyButton);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View view) {



        }
    }

}
