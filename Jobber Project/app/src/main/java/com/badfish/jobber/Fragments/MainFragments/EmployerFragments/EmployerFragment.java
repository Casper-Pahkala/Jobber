package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

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

public class EmployerFragment extends Fragment {
    TextView workersButton,findWorkersButton;
    ImageView accountButton,moreButton;
    Boolean workers,findWorkers;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workersButton=getView().findViewById(R.id.workersButton);
        findWorkersButton=getView().findViewById(R.id.findWorkersButton);
        accountButton=getView().findViewById(R.id.accountButton);
        moreButton=getView().findViewById(R.id.moreButton);
        MainActivity.employer=true;
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                getActivity().obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        int primaryColor = arr.getColor(0, -1);


        if(!MainActivity.toAvailable){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.employerFrameLayout, new MyWorkPlacesFragment()).commit();
            workers=true;
            findWorkers=false;
        }else{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.employerFrameLayout, new MyWorkPlaces2Fragment()).commit();
            workers=false;
            findWorkers=true;
            findWorkersButton.setTextColor(primaryColor);
            workersButton.setTextColor(Color.parseColor("#888888"));

        }
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_stay_still)
                        .replace(R.id.mainFrameLayout,new EmployerAccountFragment()).commit();
            }
        });
        workersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!workers){
                    workers=true;
                    findWorkers=false;
                    workersButton.setTextColor(primaryColor);
                    findWorkersButton.setTextColor(Color.parseColor("#888888"));
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                            .replace(R.id.employerFrameLayout,new MyWorkPlacesFragment()).commit();
                }
            }
        });

        findWorkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!findWorkers){
                    findWorkers=true;
                    workers=false;
                    findWorkersButton.setTextColor(primaryColor);
                    workersButton.setTextColor(Color.parseColor("#888888"));
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                            .replace(R.id.employerFrameLayout,new MyWorkPlaces2Fragment()).commit();
                }
            }
        });
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_stay_still)
                        .replace(R.id.mainFrameLayout,new EmployerMoreFragment()).commit();
            }
        });
    }

    public EmployerFragment() {
    }

    public static EmployerFragment newInstance(String param1, String param2) {
        EmployerFragment fragment = new EmployerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employer, container, false);
    }
}