package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.Models.WorkPlaceModel2;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyWorkPlaceApplicationsAdapter extends RecyclerView.Adapter<MyWorkPlaceApplicationsAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    int workerCount;
    ArrayList<WorkPlaceModel> list;
    ArrayList<String> countList;
    AcceptedWorkersAdapter adapter;
    ArrayList<WorkPlaceModel2> workerModelList;


    public MyWorkPlaceApplicationsAdapter(Context context, ArrayList<WorkPlaceModel> list, Activity activity, int workerCount) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        this.workerCount=workerCount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_workplace_model2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.jobDescription.setText(model.getJobDescription());
        holder.location.setText(model.getJobAddress());
        holder.date.setText(model.getDate());
        holder.timeText.setText(" "+model.getTime());

        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        count=count+1;

                    }
                }
                holder.acceptedWorkersText.setText(" Accepted workers "+count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("MyAvailableWorkers").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        count=count+1;

                    }
                }
                holder.availableWorkersText.setText(" Available workers "+count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final boolean[] expanded = {false};
        holder.workplaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded[0]){
                    expanded[0] =false;
                    holder.expandedLayout.setVisibility(View.GONE);
                    holder.showMoreButton.setRotation(270);
                }else{
                    holder.showMoreButton.setRotation(90);
                    expanded[0] =true;
                    holder.expandedLayout.setVisibility(View.VISIBLE);


                }

            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobDescription,date,location, timeText, availableWorkersText, showWorkersButton,acceptedWorkersText,removeButton;
        ImageView showMoreButton;
        CardView cardView;
        RelativeLayout expandedLayout;
        RelativeLayout workplaceLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobDescription =itemView.findViewById(R.id.jobDescriptionText);
            date=itemView.findViewById(R.id.dateText);
            location=itemView.findViewById(R.id.locationText);
            timeText=itemView.findViewById(R.id.timeText);
            availableWorkersText=itemView.findViewById(R.id.availableWorkersText);
            acceptedWorkersText=itemView.findViewById(R.id.acceptedWorkers);

            showMoreButton=itemView.findViewById(R.id.showMoreButton);
            expandedLayout=itemView.findViewById(R.id.expandedLayout);
            showWorkersButton=itemView.findViewById(R.id.showWorkersButton);
            workplaceLayout=itemView.findViewById(R.id.infoLayout);
            removeButton=itemView.findViewById(R.id.removeButton);

        }

        @Override
        public void onClick(View view) {



        }
    }

}

