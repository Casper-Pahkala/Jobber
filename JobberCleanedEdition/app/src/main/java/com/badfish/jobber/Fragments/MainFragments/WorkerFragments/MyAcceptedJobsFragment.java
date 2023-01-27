package com.badfish.jobber.Fragments.MainFragments.WorkerFragments;

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

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Adapters.AcceptedJobsAdapter;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAcceptedJobsFragment extends Fragment {

    RecyclerView recyclerView;
    AcceptedJobsAdapter adapter;
    ArrayList<WorkPlaceModel> list;
    ImageView backButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=getView().findViewById(R.id.recycler);
        list=new ArrayList<>();
        adapter=new AcceptedJobsAdapter(getContext(),list,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        backButton=getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.toMoreLayout){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2, R.anim.exit_stay_still2)
                            .replace(R.id.mainFrameLayout, new WorkerMoreFragment()).commit();
                }else {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2, R.anim.exit_stay_still2)
                            .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("MyAcceptedJobs").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    WorkPlaceModel model= snapshot1.getValue(WorkPlaceModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public MyAcceptedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_accepted_jobs, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    if(MainActivity.toMoreLayout){
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2, R.anim.exit_stay_still2)
                                .replace(R.id.mainFrameLayout, new WorkerMoreFragment()).commit();
                    }else {
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2, R.anim.exit_stay_still2)
                                .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }
}