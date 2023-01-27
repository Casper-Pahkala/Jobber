package com.badfish.jobber.Fragments.MainFragments.WorkerFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badfish.jobber.Activities.ApplyForWorkerActivity.ApplyForWorkerActivity;
import com.badfish.jobber.Activities.FeedBackActivity;
import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.EmployerFragment;
import com.badfish.jobber.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerMoreFragment extends Fragment {
    TextView changeButton,createApplicationButton, yourApplicationsButton,acceptedJobsButton;
    String role;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        changeButton=getView().findViewById(R.id.changeButton);
        if(sharedPreferences!=null){
            role=sharedPreferences.getString("role","");
            if(role.equals("worker")){
                changeButton.setText("Change to employer");
            }else{
                changeButton.setText("Change to worker");
            }
        }
        createApplicationButton=getView().findViewById(R.id.applicationButton);
        createApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ApplyForWorkerActivity.class));

            }
        });
        ImageView closeButton=getView().findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_left_to_right)
                        .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
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
               }
            }
        });

        TextView giveFeedbackButton=getView().findViewById(R.id.giveFeedbackButton);
        giveFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), FeedBackActivity.class));
            }
        });
        acceptedJobsButton=getView().findViewById(R.id.acceptedJobs);
        acceptedJobsButton.setVisibility(View.VISIBLE);
        acceptedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new MyAcceptedJobsFragment()).commit();
                MainActivity.toMoreLayout=true;
            }
        });
    }

    public WorkerMoreFragment() {
        // Required empty public constructor
    }

    public static WorkerMoreFragment newInstance(String param1, String param2) {
        WorkerMoreFragment fragment = new WorkerMoreFragment();
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
                            .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}