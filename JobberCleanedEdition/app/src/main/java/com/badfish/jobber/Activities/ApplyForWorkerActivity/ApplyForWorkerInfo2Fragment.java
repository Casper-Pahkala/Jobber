package com.badfish.jobber.Activities.ApplyForWorkerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.Models.WorkerModel;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplyForWorkerInfo2Fragment extends Fragment {
    ArrayList<String> selectedList;
    TextView locationText,shareLocationButton, insertLocationManuallyButton;
    RelativeLayout orLayout;
    String address,distance,firstName,lastName, profilePicture;
    Boolean gotAddress=false;
    EditText distanceText;
    ArrayList<WorkPlaceModel> workPlaceList;
    ArrayList<String> acceptedList;
    Double lat,lng;
    ProgressBar progressBar;
    RelativeLayout mainContent;
    TextView successText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedList= ApplyForWorkerActivity.selectedList;

        successText=getView().findViewById(R.id.successText);
        progressBar=getView().findViewById(R.id.progressBar);
        mainContent=getView().findViewById(R.id.mainContent);
        orLayout=getView().findViewById(R.id.orLayout);
        locationText=getView().findViewById(R.id.locationText);
        shareLocationButton=getView().findViewById(R.id.shareLocationButton);
        insertLocationManuallyButton=getView().findViewById(R.id.insertLocationManually);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        firstName=sharedPreferences.getString("firstName","");
        lastName=sharedPreferences.getString("lastName","");
        profilePicture=sharedPreferences.getString("profilePicture","");
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                intent.putExtra("address", locationText.getText().toString());
                startActivity(intent);

            }
        });

        insertLocationManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                intent.putExtra("search","true");
                startActivity(intent);

            }
        });

        if(getActivity().getIntent().getExtras()!=null){
            lat = getActivity().getIntent().getExtras().getDouble("lat");
            lng = getActivity().getIntent().getExtras().getDouble("lng");
            if(getActivity().getIntent().getExtras().getString("address")!=null){
                address=getActivity().getIntent().getExtras().getString("address");
                gotAddress=true;
            }

            if(getActivity().getIntent().getExtras().get("list")!=null){
                selectedList= (ArrayList<String>) getActivity().getIntent().getExtras().get("list");
            }
            locationText.setVisibility(View.VISIBLE);
            locationText.setText(address);
            shareLocationButton.setVisibility(View.GONE);
            insertLocationManuallyButton.setVisibility(View.GONE);
            orLayout.setVisibility(View.GONE);
        }
        distanceText=getView().findViewById(R.id.distanceText);
        distanceText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    distanceText.clearFocus();
                }
                return false;
            }
        });

        RelativeLayout nextLayout=getView().findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(distanceText.getText().toString().equals("")){
                    distanceText.setError("Enter a distance");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mainContent.setVisibility(View.GONE);
                Action();
            }
        });

        ImageView backButton = getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                        .replace(R.id.frameLayout, new ApplyForWorkerInfo1Fragment()).commit();
            }
        });
        TextView shareLocationButton=getView().findViewById(R.id.shareLocationButton);
        shareLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                startActivity(intent);
            }
        });


        FirebaseDatabase.getInstance().getReference().child("AcceptedWorkPlaces").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptedList=new ArrayList<>();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    acceptedList.add(snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("MyWorkPlaces").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workPlaceList=new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for(int i=0;i<selectedList.size();i++) {
                        String työnkuva = selectedList.get(i);
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            if (snapshot2.child("jobDescription").getValue().equals(työnkuva)& !snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                                WorkPlaceModel model = snapshot2.getValue(WorkPlaceModel.class);
                                workPlaceList.add(model);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ApplyForWorkerInfo2Fragment() {
        // Required empty public constructor
    }

    public static ApplyForWorkerInfo2Fragment newInstance(String param1, String param2) {
        ApplyForWorkerInfo2Fragment fragment = new ApplyForWorkerInfo2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apply_for_worker_info2, container, false);
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new ApplyForWorkerInfo1Fragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    public void Action(){
        if(!gotAddress) {
            Toast.makeText(getActivity(), "Enter a valid address", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
            return;
        }
        String firstName = getActivity().getSharedPreferences("MyPrefsFile",0).getString("firstName","");
        String lastName = getActivity().getSharedPreferences("MyPrefsFile",0).getString("lastName","");
        String name = firstName+" "+lastName;
        String token = getActivity().getSharedPreferences("Token",0).getString("token","");
        distance=distanceText.getText().toString().trim();

        if(workPlaceList.size()>0) {

            for (int i = 0; i < workPlaceList.size(); i++) {
                Double latt = workPlaceList.get(i).getLat();
                Double lngg = workPlaceList.get(i).getLng();
                Double dist = distance(latt, lngg, lat, lng);
                String distt = String.valueOf(dist);
                String correctstring[] = distt.split("\\.");
                String distanceFromWorkPlace = correctstring[0];
                int i1 = Integer.parseInt(correctstring[0]);
                int i2 = Integer.parseInt(distance);
                int subtract = i1 - i2;
                String comp = String.valueOf(subtract);

                if (subtract == 0) {

                }
                if (subtract > 0) {

                }
                if (subtract < 0) {
                    String userID=FirebaseAuth.getInstance().getUid();
                    String workPlaceToken = workPlaceList.get(i).getJobToken();
                    String id = workPlaceList.get(i).getId();
                    String jobaddress = workPlaceList.get(i).getJobAddress();
                    String date = workPlaceList.get(i).getDate();
                    String time = workPlaceList.get(i).getTime();
                    Toast.makeText(getActivity(), workPlaceList.get(i).getEmployerName(), Toast.LENGTH_SHORT).show();
                    if (!acceptedList.contains(id)) {
                        String jobDescription = workPlaceList.get(i).getJobDescription();
                        WorkPlaceModel model = new WorkPlaceModel(distanceFromWorkPlace, workPlaceList.get(i).getEmployerName(),
                                jobDescription, id, address, userID,token, workPlaceList.get(i).getEmployerUID(), jobaddress, workPlaceToken, date, time,firstName +" "+lastName
                        ,workPlaceList.get(i).getEmployerProfilePicture(),profilePicture,"open");
                        FirebaseDatabase.getInstance().getReference().child("MyAvailableJobs").child(FirebaseAuth.getInstance().getUid()).child(id).setValue(model);
                    }
                }
            }
        }

        for (int i = 0; i < selectedList.size(); i++) {
            String työnkuva = selectedList.get(i);

            WorkerModel model = new WorkerModel(lat, lng, distance, token, FirebaseAuth.getInstance().getUid(), työnkuva,address,name,profilePicture);

            FirebaseDatabase.getInstance().getReference("Workers").child(FirebaseAuth.getInstance().getUid()).child(työnkuva).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                successText.setVisibility(View.VISIBLE);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finishAffinity();

                                    }
                                }, 1500);
                            }
                        }, 500);

                    } else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}