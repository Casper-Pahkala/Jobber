package com.badfish.jobber.Activities.ApplyForEmployer;

import static com.badfish.jobber.Activities.ApplyForEmployer.ApplyForEmployerActivity.jobDescription;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badfish.jobber.R;

import java.util.ArrayList;

public class ApplyForEmployerInfo1Fragment extends Fragment{
    boolean checked=false;
    ArrayList<String> countlist;
    ArrayList<String> arrayList;
    ArrayList<String> selectedList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getView().findViewById(R.id.listView);

        arrayList = new ArrayList<>();
        arrayList.add("Valitse kaikki");
        arrayList.add("Nurmikonleikkuu");
        arrayList.add("Lastenhoito");
        arrayList.add("Lemmikkienhoito");
        arrayList.add("Siivous");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);
        countlist = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jobDescription=arrayList.get(i);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left).replace(R.id.frameLayout
                        , new ApplyForEmployerInfo2Fragment()).commit();
            }
        });


        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        RelativeLayout nextLayout=getView().findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedList.size()>0) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left).replace(R.id.frameLayout
                            , new ApplyForEmployerInfo2Fragment()).commit();
                }else{
                    Toast.makeText(getActivity(), "Select your line of work", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView backButton = getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

    }

    public ApplyForEmployerInfo1Fragment() {
        // Required empty public constructor
    }

    public static ApplyForEmployerInfo1Fragment newInstance(String param1, String param2) {
        ApplyForEmployerInfo1Fragment fragment = new ApplyForEmployerInfo1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_for_worker_info1, container, false);
    }

}