package com.badfish.jobber.Fragments.MainFragments;

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

import com.badfish.jobber.Adapters.MyWorkPlaceApplicationsAdapter;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.EmployerMoreFragment;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourApplicationsFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView backButton;
    ArrayList<WorkPlaceModel> list;
    MyWorkPlaceApplicationsAdapter adapter;
    int workerCount=0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list=new ArrayList<>();
        adapter= new MyWorkPlaceApplicationsAdapter(getContext(), list, getActivity(),workerCount);
        recyclerView=getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        backButton=getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left
                        , R.anim.exit_left_to_right).replace(R.id.mainFrameLayout, new EmployerMoreFragment()).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("MyWorkPlaces").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    WorkPlaceModel model = snapshot1.getValue(WorkPlaceModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public YourApplicationsFragment() {
        // Required empty public constructor
    }

    public static YourApplicationsFragment newInstance(String param1, String param2) {
        YourApplicationsFragment fragment = new YourApplicationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_applications, container, false);
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.mainFrameLayout, new EmployerMoreFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}