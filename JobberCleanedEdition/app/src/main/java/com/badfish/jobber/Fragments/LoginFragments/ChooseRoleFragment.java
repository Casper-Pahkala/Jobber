package com.badfish.jobber.Fragments.LoginFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseRoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseRoleFragment extends Fragment {
    RelativeLayout employeeButton,employerButton;

    public ChooseRoleFragment() {
        // Required empty public constructor
    }

    public static ChooseRoleFragment newInstance(String param1, String param2) {
        ChooseRoleFragment fragment = new ChooseRoleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_role, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        employeeButton=getView().findViewById(R.id.employeeButton);
        employerButton=getView().findViewById(R.id.employerButton);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile", 0);


        employerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("role","employer").commit();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("role","worker").commit();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

    }
}