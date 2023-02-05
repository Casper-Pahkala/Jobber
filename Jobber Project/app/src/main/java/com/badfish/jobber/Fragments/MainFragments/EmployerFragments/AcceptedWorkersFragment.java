package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badfish.jobber.Activities.ApplyForEmployer.ApplyForEmployerActivity;
import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Adapters.AcceptedWorkersAdapter;
import com.badfish.jobber.Models.WorkPlaceModel2;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AcceptedWorkersFragment extends Fragment {
    TextView applyButton;
    ArrayList<WorkPlaceModel2> workerModelList;
    RelativeLayout noAdLayout;
    RecyclerView recyclerView;
    AcceptedWorkersAdapter adapter;
    boolean expanded=false;
    TextView jobDescriptionText, addressText, timeText, dateText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyButton=getView().findViewById(R.id.applyHereButton);
        workerModelList=new ArrayList<>();
        recyclerView=getView().findViewById(R.id.myWorkPlaceRecycler);
        noAdLayout=getView().findViewById(R.id.noAdLayout);
        adapter=new AcceptedWorkersAdapter(getContext(),workerModelList,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        jobDescriptionText=getView().findViewById(R.id.jobDescriptionText);
        addressText=getView().findViewById(R.id.addresstext);
        timeText=getView().findViewById(R.id.timeText);
        dateText=getView().findViewById(R.id.dateText);

        jobDescriptionText.setText(MainActivity.jobDescription);
        addressText.setText(MainActivity.jobAddress);
        timeText.setText(MainActivity.jobTime);
        dateText.setText(MainActivity.jobDate);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ApplyForEmployerActivity.class));
            }
        });

        FirebaseDatabase.getInstance().getReference("MyWorkPlaces").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    checkForAcceptedWorkers();
                    noAdLayout.setVisibility(View.GONE);
                }else{
                    noAdLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ImageView showMoreButton=getView().findViewById(R.id.showMoreButton);
        LinearLayout expandedLayout=getView().findViewById(R.id.expandedLayout);
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded){
                    expanded=false;
                    expandedLayout.setVisibility(View.GONE);
                    showMoreButton.setRotation(270);
                }else{
                    showMoreButton.setRotation(90);
                    expanded=true;
                    expandedLayout.setVisibility(View.VISIBLE);
                }
            }
        });




    }

    private void checkForAcceptedWorkers(){
        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(MainActivity.jobID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        WorkPlaceModel2 model = snapshot1.getValue(WorkPlaceModel2.class);
                        workerModelList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    recyclerView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public AcceptedWorkersFragment() {
    }

    public static AcceptedWorkersFragment newInstance(String param1, String param2) {
        AcceptedWorkersFragment fragment = new AcceptedWorkersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_workers, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        adapter.notifyDataSetChanged();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still, R.anim.exit_stay_still).replace(R.id.mainFrameLayout, new EmployerFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}