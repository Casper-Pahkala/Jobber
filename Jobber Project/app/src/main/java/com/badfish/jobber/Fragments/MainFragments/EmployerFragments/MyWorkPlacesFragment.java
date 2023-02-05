package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badfish.jobber.Activities.ApplyForEmployer.ApplyForEmployerActivity;
import com.badfish.jobber.Adapters.MyWorkPlaceAdapter;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyWorkPlacesFragment extends Fragment {

    TextView applyButton;
    ArrayList<WorkPlaceModel> workPlaceList;
    MyWorkPlaceAdapter adapter;
    RelativeLayout noAdLayout;
    public static RecyclerView recyclerView;

    int workerCount;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyButton = getView().findViewById(R.id.applyHereButton);
        workPlaceList = new ArrayList<>();
        recyclerView = getView().findViewById(R.id.myWorkPlaceRecycler);
        noAdLayout = getView().findViewById(R.id.noAdLayout);
        adapter = new MyWorkPlaceAdapter(getContext(), workPlaceList, getActivity(), workerCount);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ApplyForEmployerActivity.class));
            }
        });

        FirebaseDatabase.getInstance().getReference("MyWorkPlaces").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noAdLayout.setVisibility(View.GONE);
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    WorkPlaceModel model = snapshot1.getValue(WorkPlaceModel.class);
                    workPlaceList.add(model);
                }
                Collections.sort(workPlaceList, new Comparator<WorkPlaceModel>() {
                    @Override
                    public int compare(WorkPlaceModel itemModel, WorkPlaceModel t1) {
                        String first;
                        String second;
                        if (itemModel.getDate().length() == 10) {
                            first = itemModel.getDate().charAt(3) + "" + itemModel.getDate().charAt(4)
                                    + itemModel.getDate().charAt(0) + "" + itemModel.getDate().charAt(1);
                        } else {
                            first = itemModel.getDate().charAt(2) + "" + itemModel.getDate().charAt(3)
                                    + 0 + "" + itemModel.getDate().charAt(0);
                        }

                        if (t1.getDate().length() == 10) {
                            second = t1.getDate().charAt(3) + "" + t1.getDate().charAt(4)
                                    + t1.getDate().charAt(0) + "" + t1.getDate().charAt(1);
                        } else {
                            second = t1.getDate().charAt(2) + "" + t1.getDate().charAt(3)
                                    + 0 + "" + t1.getDate().charAt(0);
                        }
                        return first.compareTo(second);
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public MyWorkPlacesFragment() {
    }

    public static MyWorkPlacesFragment newInstance(String param1, String param2) {
        MyWorkPlacesFragment fragment = new MyWorkPlacesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_work_places, container, false);
    }
}