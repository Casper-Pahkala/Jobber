package com.badfish.jobber.Fragments.MainFragments.WorkerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.badfish.jobber.Activities.ApplyForWorkerActivity.ApplyForWorkerActivity;
import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Adapters.UpComingJobsAdapter;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UpcomingJobsFragment extends Fragment {
    TextView applyHereButton,discoverJobsButton;
    public static RecyclerView recyclerView;
    TextView acceptedJobsButton;
    boolean enteredApplication;
    public static RelativeLayout infoLayout,noApplicationLayout,noJobsAvailabeLayout;
    UpComingJobsAdapter adapter;
    ArrayList<WorkPlaceModel> workPlaceList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=getView().findViewById(R.id.recycler);
        noApplicationLayout=getView().findViewById(R.id.noApplicationLayout);
        applyHereButton=getView().findViewById(R.id.applyHereButton);
        discoverJobsButton=getView().findViewById(R.id.discoverJobsButton);
        noJobsAvailabeLayout=getView().findViewById(R.id.noJobsAvailableLayout);
        acceptedJobsButton=getView().findViewById(R.id.myAcceptedJobs);

        workPlaceList= MainActivity.workPlaceList;
        adapter=MainActivity.upComingJobsAdapter;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        applyHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ApplyForWorkerActivity.class));
            }
        });
        String userID= FirebaseAuth.getInstance().getUid();



        discoverJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkerFragment.discover();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.workerFrameLayout,new DiscoverFragment()).commit();
            }
        });

        acceptedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new MyAcceptedJobsFragment()).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                }else{
                    FirebaseDatabase.getInstance().getReference("Workers").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                noJobsAvailabeLayout.setVisibility(View.VISIBLE);
                            }else{
                                noApplicationLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public UpcomingJobsFragment() {
        // Required empty public constructor
    }

    public static UpcomingJobsFragment newInstance(String param1, String param2) {
        UpcomingJobsFragment fragment = new UpcomingJobsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_upcoming_jobs, container, false);
    }

    Boolean delay=false;
    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        try{
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    if(delay){
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        if(delay){
                            startActivity(intent);
                        }
                    }
                    delay=true;
                    Toast.makeText(getActivity(), "Click back again to exit", Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                              delay=false;
                        }
                    }, 2000);
                    return true;
                }
                return false;
            }
        });
    }
}