package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.badfish.jobber.Activities.ApplyForEmployer.ApplyForEmployerActivity;
import com.badfish.jobber.Activities.FeedBackActivity;
import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.MainFragments.WorkerFragments.WorkerFragment;
import com.badfish.jobber.Fragments.MainFragments.YourApplicationsFragment;
import com.badfish.jobber.R;

public class EmployerMoreFragment extends Fragment {
    TextView changeButton, enterApplicationButton, yourApplicationsButton;;
    String role;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        changeButton=getView().findViewById(R.id.changeButton);
        enterApplicationButton=getView().findViewById(R.id.applicationButton);

        if(sharedPreferences!=null){
            role=sharedPreferences.getString("role","");
            if(role.equals("employee")){
                changeButton.setText("Change to employer");
            }else{
                changeButton.setText("Change to worker");
            }
        }
        ImageView closeButton=getView().findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_left_to_right)
                        .replace(R.id.mainFrameLayout, new EmployerFragment()).commit();
            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("worker")){
                    sharedPreferences.edit().putString("role","employer").commit();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                            .replace(R.id.mainFrameLayout,new EmployerFragment()).commit();
                }
               if(role.equals("employer")){
                   sharedPreferences.edit().putString("role","worker").commit();
                   getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                           .replace(R.id.mainFrameLayout,new WorkerFragment()).commit();
                   ((MainActivity)getActivity()).upComingJobs();

               }
            }
        });
        enterApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ApplyForEmployerActivity.class));
            }
        });

        TextView giveFeedbackButton=getView().findViewById(R.id.giveFeedbackButton);
        giveFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), FeedBackActivity.class));
            }
        });

        yourApplicationsButton= getView().findViewById(R.id.yourApplicationsButton);
        yourApplicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new YourApplicationsFragment()).commit();
            }
        });

    }

    public EmployerMoreFragment() {
        // Required empty public constructor
    }

    public static EmployerMoreFragment newInstance(String param1, String param2) {
        EmployerMoreFragment fragment = new EmployerMoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_left_to_right)
                            .replace(R.id.mainFrameLayout, new EmployerFragment()).commit();                    return true;
                }
                return false;
            }
        });
    }
}