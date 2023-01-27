package com.badfish.jobber.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedJobsAdapter extends RecyclerView.Adapter<AcceptedJobsAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<WorkPlaceModel> list;
    View v;

    public AcceptedJobsAdapter(Context context, ArrayList<WorkPlaceModel> list, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.accepted_jobs_model, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final WorkPlaceModel model = list.get(position);
        holder.etäisyys.setText("Noin "+model.getDistance()+" kilometrin päässä");
        holder.name.setText(model.getEmployerName());
        holder.työnkuva.setText(model.getJobDescription());
        holder.timeText.setText(model.getTime());
        holder.dateText.setText(model.getDate());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView työnkuva,etäisyys,name, dateText,timeText, noPictureText;
        CardView cardView;
        CircleImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.työpaikkaView);
            työnkuva=itemView.findViewById(R.id.työnkuvaText);
            etäisyys=itemView.findViewById(R.id.etäisyystext);
            name=itemView.findViewById(R.id.name);
            timeText=itemView.findViewById(R.id.timeText);
            dateText=itemView.findViewById(R.id.dateText);
            imageView=itemView.findViewById(R.id.imageView);
            noPictureText=itemView.findViewById(R.id.noPictureText);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View view) {



        }
    }

}

