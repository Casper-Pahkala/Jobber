package com.badfish.jobber.Fragments.MainFragments.WorkerFragments;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkerFragment extends Fragment {
    public static TextView upcomingJobsButton,discoverButton;
    public static ArrayList<String> jobDescriptionList;
    public static String location,distance;
    ImageView accountButton,moreButton;
    public static boolean discoverchecked, upcomingChecked;

    public static void discover(){
        discoverchecked=true;
        upcomingChecked=false;
        discoverButton.setTextColor(Color.parseColor("#FFFFFF"));
        upcomingJobsButton.setTextColor(Color.parseColor("#888888"));

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.employer=false;
        discoverchecked=false;
        upcomingChecked=true;
        jobDescriptionList=new ArrayList<>();
        upcomingJobsButton=getView().findViewById(R.id.upcomingJobsButton);
        discoverButton=getView().findViewById(R.id.discoverButton);
        accountButton=getView().findViewById(R.id.accountButton);
        moreButton=getView().findViewById(R.id.moreButton);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.workerFrameLayout,new UpcomingJobsFragment()).commit();


        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_stay_still)
                        .replace(R.id.mainFrameLayout,new WorkerAccountFragment()).commit();

            }
        });

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                getActivity().obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        int primaryColor = arr.getColor(0, -1);

        upcomingJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!upcomingChecked){
                    upcomingChecked=true;
                    discoverchecked=false;
                    upcomingJobsButton.setTextColor(primaryColor);
                    discoverButton.setTextColor(Color.parseColor("#888888"));

                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                            .replace(R.id.workerFrameLayout,new UpcomingJobsFragment()).commit();

                }
            }
        });

        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discoverchecked){
                    discoverchecked=true;
                    upcomingChecked=false;
                    discoverButton.setTextColor(primaryColor);
                    upcomingJobsButton.setTextColor(Color.parseColor("#888888"));

                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                            .replace(R.id.workerFrameLayout,new DiscoverFragment()).commit();

                }
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_stay_still)
                        .replace(R.id.mainFrameLayout,new WorkerMoreFragment()).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("Workers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobDescriptionList = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    location=snapshot1.child("address").getValue().toString();
                    distance=snapshot1.child("distance").getValue().toString();
                    jobDescriptionList.add(snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public WorkerFragment() {
        // Required empty public constructor
    }

    public static WorkerFragment newInstance(String param1, String param2) {
        WorkerFragment fragment = new WorkerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_worker, container, false);
    }



}