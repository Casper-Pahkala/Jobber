package com.badfish.jobber.Activities.ApplyForWorkerActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeletePreviousActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeletePreviousActivity extends Fragment {

    TextView deletePreviousButton;
    ImageView backButton;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deletePreviousButton=getView().findViewById(R.id.deletePreviousButton);
        backButton=getView().findViewById(R.id.backButton);

        deletePreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("MyAvailableJobs").child(FirebaseAuth.getInstance().getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("MyAcceptedJobs").child(FirebaseAuth.getInstance().getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("MyUpcomingJobs").child(FirebaseAuth.getInstance().getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Workers").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ApplyForWorkerInfo1Fragment()).commit();
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public DeletePreviousActivity() {
    }

    public static DeletePreviousActivity newInstance(String param1, String param2) {
        DeletePreviousActivity fragment = new DeletePreviousActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_previous_activity, container, false);
    }
}