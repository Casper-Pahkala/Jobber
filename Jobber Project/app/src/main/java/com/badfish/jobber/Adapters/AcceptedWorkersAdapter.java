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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.ChatFragment;
import com.badfish.jobber.Models.WorkPlaceModel2;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedWorkersAdapter extends RecyclerView.Adapter<AcceptedWorkersAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<WorkPlaceModel2> list;
    View v;


    public AcceptedWorkersAdapter(Context context, ArrayList<WorkPlaceModel2> list, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.accepted_worker_message, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel2 model = list.get(position);
        if(model.getLastText()!=null){
            holder.lastText.setVisibility(View.VISIBLE);
            holder.lastText.setText(model.getLastText());
        }else{
            holder.lastText.setVisibility(View.VISIBLE);
            holder.lastTextTime.setVisibility(View.GONE);

            holder.lastText.setCompoundDrawables(null,null,null,null);
            holder.lastText.setText("Contact your worker!");

        }
        if(model.getLastTextTime()!=null){
            Long timeStamp = (Long) model.getLastTextTime();
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
        } else {
            holder.lastTextTime.setText("");
        }




        holder.name.setText(model.getWorkerName());
        holder.jobDescription.setText(model.getJobDescription());
        final String[] pictureRef = new String[1];

        if(model.getWorkerProfilePicture().contains("noProfilePicture")) {
            StorageReference storageRef= FirebaseStorage.getInstance().getReference("noProfilePictures");
            pictureRef[0] =model.getWorkerProfilePicture();
            storageRef.child(pictureRef[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .into(holder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    String firstLetter = model.getWorkerName().substring(0,1);
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
            pictureRef[0] =model.getWorkerProfilePicture();

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
                MainActivity.mRole="employer";
                MainActivity.otherToken=model.getWorkerToken();


                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new ChatFragment()).commit();
            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,jobDescription,lastText, lastTextTime, noPictureText;
        RelativeLayout cardView;
        CircleImageView imageView;
        ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.workerName);
            jobDescription=itemView.findViewById(R.id.workerJobDescription);
            lastText=itemView.findViewById(R.id.lastText);
            lastTextTime=itemView.findViewById(R.id.lastTextTime);
            cardView=itemView.findViewById(R.id.card);
            imageView=itemView.findViewById(R.id.imageView);
            noPictureText=itemView.findViewById(R.id.noPictureText);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View view) {



        }
    }

}

