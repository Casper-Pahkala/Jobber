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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.AcceptedWorkersFragment;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.Models.WorkPlaceModel2;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyWorkPlaceAdapter extends RecyclerView.Adapter<MyWorkPlaceAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    int workerCount;
    ArrayList<WorkPlaceModel> list;
    ArrayList<String> countList;
    AcceptedWorkersAdapter adapter;
    ArrayList<WorkPlaceModel2> workerModelList;


    public MyWorkPlaceAdapter(Context context, ArrayList<WorkPlaceModel> list, Activity activity,int workerCount) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        this.workerCount=workerCount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_workplace_model, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.jobDescription.setText(model.getJobDescription());
        holder.location.setText(model.getJobAddress());
        holder.date.setText(model.getDate());
        holder.timeText.setText(" "+model.getTime());
        countList=new ArrayList<>();
        ArrayList<WorkPlaceModel2> workerModelList;
        workerModelList=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workerModelList.clear();
                countList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        countList.add(snapshot1.getKey());

                        WorkPlaceModel2 model = snapshot1.getValue(WorkPlaceModel2.class);
                        workerModelList.add(model);
                    }
                    String count = String.valueOf(countList.size());
                    holder.availableWorkersText.setText("  "+count);
                    adapter.notifyDataSetChanged();
                }else{
                    holder.availableWorkersText.setText("  0");
                    holder.showWorkersButton.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter=new AcceptedWorkersAdapter(context,workerModelList,activity);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        holder.recyclerView.setHasFixedSize(true);


        final boolean[] expanded = {false};
        holder.workplaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded[0]){
                    expanded[0] =false;
                    holder.expandedLayout.setVisibility(View.GONE);
                    holder.showMoreButton.setRotation(270);
                }else{
                    MainActivity.jobAddress=model.getJobAddress();
                    MainActivity.jobDescription=model.getJobDescription();
                    MainActivity.jobDate=model.getDate();
                    MainActivity.jobTime=model.getTime();
                    MainActivity.jobID=model.getId();
                    MainActivity.toAvailable=false;
                    holder.showMoreButton.setRotation(90);
                    expanded[0] =true;
                    holder.expandedLayout.setVisibility(View.VISIBLE);


                }

            }
        });

        holder.showWorkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.jobAddress=model.getJobAddress();
                MainActivity.jobDescription=model.getJobDescription();
                MainActivity.jobDate=model.getDate();
                MainActivity.jobTime=model.getTime();
                MainActivity.jobID=model.getId();
                MainActivity.toAvailable=false;

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new AcceptedWorkersFragment()).commit();


            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobDescription,date,location, timeText, availableWorkersText, showWorkersButton;
        ImageView showMoreButton;
        CardView cardView;
        RelativeLayout expandedLayout;
        RelativeLayout workplaceLayout;
        RecyclerView recyclerView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobDescription =itemView.findViewById(R.id.jobDescriptionText);
            date=itemView.findViewById(R.id.dateText);
            location=itemView.findViewById(R.id.locationText);
            timeText=itemView.findViewById(R.id.timeText);
            availableWorkersText=itemView.findViewById(R.id.availableWorkersText);
            showMoreButton=itemView.findViewById(R.id.showMoreButton);
            expandedLayout=itemView.findViewById(R.id.expandedLayout);
            showWorkersButton=itemView.findViewById(R.id.showWorkersButton);
            workplaceLayout=itemView.findViewById(R.id.infoLayout);
            recyclerView=itemView.findViewById(R.id.myWorkPlaceRecycler);
        }

        @Override
        public void onClick(View view) {



        }
    }

}

