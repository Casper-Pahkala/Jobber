package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.ChatFragment;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpComingJobsAdapter extends RecyclerView.Adapter<UpComingJobsAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<WorkPlaceModel> list;
    View v;

    public UpComingJobsAdapter(Context context, ArrayList<WorkPlaceModel> list, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.accepted_job_message, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.distance.setText(" "+model.getJobAddress());
        holder.date.setText(" "+model.getDate());
        holder.time.setText(" "+model.getTime());
        holder.name.setText(model.getEmployerName());
        holder.jobDescription.setText(model.getJobDescription());

        Long currentTime = System.currentTimeMillis();
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = date.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        String tomorrowDate = dateFormat.format(cal.getTime());

        String jobDate = model.getDate();
        if(jobDate.equals(currentDate)){
            holder.alertText.setText("Today!");
            holder.alertText.setVisibility(View.VISIBLE);
        }
        if(jobDate.equals(tomorrowDate)){
            holder.alertText.setText("Tomorrow!");
            holder.alertText.setVisibility(View.VISIBLE);
        }




        final boolean[] expanded = {false};
        holder.showMoreButton.setOnClickListener(new View.OnClickListener() {
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
        final String[] pictureRef = new String[1];

                if(model.getEmployerProfilePicture().contains("noProfilePicture")) {
                    StorageReference storageRef= FirebaseStorage.getInstance().getReference("noProfilePictures");
                    pictureRef[0] =model.getEmployerProfilePicture();
                    storageRef.child(pictureRef[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context)
                                    .load(uri)
                                    .into(holder.imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            String firstLetter = model.getEmployerName().substring(0,1);
                                            holder.noPictureText.setText(firstLetter);
                                            holder.noPictureText.setVisibility(View.VISIBLE);
                                            holder.cardView.setVisibility(View.VISIBLE);
                                            holder.progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });


                        }
                    });

                }else{
                    pictureRef[0] =model.getEmployerProfilePicture();

                    StorageReference storageRef= FirebaseStorage.getInstance().getReference(model.getWorkerUserID()).child("profilePicture");

                    storageRef.child(pictureRef[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context)
                                    .load(uri)
                                    .into(holder.imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.cardView.setVisibility(View.VISIBLE);
                                            holder.progressBar.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

                }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.jobID=model.getId();
                MainActivity.otherName=model.getEmployerName();
                MainActivity.date=model.getDate();
                MainActivity.address=model.getJobAddress();
                MainActivity.time=model.getTime();
                MainActivity.employerUID=model.getEmployerUID();
                MainActivity.workerUID=model.getWorkerUserID();
                MainActivity.mJobDescription=model.getJobDescription();
                MainActivity.jobId=model.getId();
                MainActivity.pictureRef=pictureRef[0];
                MainActivity.mRole="worker";
                MainActivity.otherToken=model.getJobToken();


                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new ChatFragment()).commit();

            }
        });

        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(FirebaseAuth.getInstance().getUid()).child(model.getId())
                .child("lastText").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().exists()){
                            holder.lastText.setVisibility(View.VISIBLE);
                            holder.lastText.setText(task.getResult().getValue().toString()+"   ");

                        }else{

                            holder.lastText.setText("Contact your employer!");
                            holder.lastText.setCompoundDrawables(null,null,null,null);

                            holder.lastText.setVisibility(View.VISIBLE);
                        }
                    }
                });
        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(FirebaseAuth.getInstance().getUid()).child(model.getId())
                .child("lastTextTime").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().exists()) {
                                Long timeStamp = (Long) task.getResult().getValue();
                                Long currentTime = System.currentTimeMillis();

                                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat day = new SimpleDateFormat("dd");

                                String day_name = day.format(timeStamp);
                                String month_name = month_date.format(timeStamp);
                                String lastTextDate = date.format(timeStamp);
                                String currentDate = date.format(currentTime);
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);
                                String yesterdayDate = dateFormat.format(cal.getTime());;
                                int minutes = (int) ((timeStamp / (1000 * 60)) % 60);
                                int hours = (int) ((timeStamp / (1000 * 60 * 60)) % 24);
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
                                if (lastTextDate.equals(currentDate)) {

                                    holder.lastTextTime.setText(hour + ":" + minute + " today");
                                }else if(lastTextDate.equals(yesterdayDate)){
                                    holder.lastTextTime.setText(hour + ":" + minute + " yesterday");
                                }
                                else {
                                    holder.lastTextTime.setText(day_name + ". " + month_name);

                                }

                        }else {
                            holder.lastTextTime.setText("");
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobDescription,distance,name,date,time,lastText,lastTextTime, noPictureText, alertText;
        RelativeLayout cardView;
        ImageView showMoreButton;
        LinearLayout expandedLayout;
        CircleImageView imageView;
        ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card);
            jobDescription=itemView.findViewById(R.id.jobDescriptionText);
            distance=itemView.findViewById(R.id.distanceText);
            date=itemView.findViewById(R.id.dateText);
            time=itemView.findViewById(R.id.timeText);
            name=itemView.findViewById(R.id.name);
            showMoreButton=itemView.findViewById(R.id.showMoreButton);
            expandedLayout=itemView.findViewById(R.id.expandedLayout);
            lastText=itemView.findViewById(R.id.lastText);
            lastTextTime=itemView.findViewById(R.id.lastTextTime);
            imageView=itemView.findViewById(R.id.imageView);
            noPictureText=itemView.findViewById(R.id.noPictureText);
            progressBar=itemView.findViewById(R.id.progressBar);
            alertText=itemView.findViewById(R.id.alertText);
        }

        @Override
        public void onClick(View view) {



        }
    }

}

