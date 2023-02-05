package com.badfish.jobber.Fragments.MainFragments.WorkerFragments;

import static com.badfish.jobber.Activities.MainActivity.token;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badfish.jobber.Adapters.WorkPlaceAdapter;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.badfish.jobber.SendNotification;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment {

    RecyclerView recyclerView;
    RelativeLayout infoLayout,noApplicationLayout;
    TextView jobDescriptionsText, locationText,distanceText;
    ArrayList<String> jobDescriptionList;
    ArrayList<WorkPlaceModel> workPlaceList;
    WorkPlaceAdapter adapter;
    ImageView filterButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        infoLayout=getView().findViewById(R.id.infoLayout);
        noApplicationLayout=getView().findViewById(R.id.noApplicationLayout);
        recyclerView=getView().findViewById(R.id.recycler);
        jobDescriptionsText=getView().findViewById(R.id.jobDescriptionsText);
        jobDescriptionList= WorkerFragment.jobDescriptionList;
        locationText=getView().findViewById(R.id.locationText);
        filterButton=getView().findViewById(R.id.filterButton);
        locationText.setText(WorkerFragment.location);
        distanceText=getView().findViewById(R.id.distanceText);
        distanceText.setText(WorkerFragment.distance+" km");
        workPlaceList=new ArrayList<>();
        adapter=new WorkPlaceAdapter(getContext(), workPlaceList,getActivity());
        String listString = String.join(", ", jobDescriptionList);
        jobDescriptionsText.setText(listString);
        String userID=FirebaseAuth.getInstance().getUid();
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("Workers").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    infoLayout.setVisibility(View.VISIBLE);
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("MyAvailableJobs").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    workPlaceList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    TextView noAvailableJobsText=getView().findViewById(R.id.noAvailableJobsText);
                    noAvailableJobsText.setVisibility(View.GONE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (!snapshot1.child("jobUserID").equals(FirebaseAuth.getInstance().getUid())) {
                            WorkPlaceModel workPlaceModel = snapshot1.getValue(WorkPlaceModel.class);
                            workPlaceList.add(workPlaceModel);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    CollapsingToolbarLayout toolbar=getView().findViewById(R.id.collapsing_toolbar);
                    AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                    p.setScrollFlags(0);
                    toolbar.setLayoutParams(p);
                    recyclerView.setVisibility(View.GONE);
                    TextView noAvailableJobsText=getView().findViewById(R.id.noAvailableJobsText);
                    noAvailableJobsText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}