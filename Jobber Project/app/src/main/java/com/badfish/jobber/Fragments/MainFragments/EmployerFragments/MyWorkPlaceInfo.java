package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badfish.jobber.R;

public class MyWorkPlaceInfo extends Fragment {



    public MyWorkPlaceInfo() {
    }

    public static MyWorkPlaceInfo newInstance(String param1, String param2) {
        MyWorkPlaceInfo fragment = new MyWorkPlaceInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_work_place_info, container, false);
    }
}