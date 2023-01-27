package com.badfish.jobber.Activities.ApplyForEmployer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.badfish.jobber.Activities.ApplyForWorkerActivity.MapsActivity;
import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Models.EmployerModel;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.Models.WorkerModel;
import com.badfish.jobber.R;
import com.badfish.jobber.Services.FcmNotificationsSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ApplyForEmployerInfo2Fragment extends Fragment {

    TextView shareLocationButton, insertLocationManuallyButton;
    RelativeLayout calendarLayout,orLayout, timeLayout, nextButton;
    ArrayList<String> selectedList, usedList, idList;
    int year,month,day,hour,minute;
    String date,address,min, time, id, token, name, profilePicture;
    Double lat,lng;
    boolean gotAddress=false;
    TextView dateText;
    ProgressBar progressBar;
    ArrayList<WorkerModel> workersList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedList=ApplyForEmployerActivity.selectedList;
        shareLocationButton=getView().findViewById(R.id.shareLocationButton);
        insertLocationManuallyButton=getView().findViewById(R.id.insertLocationManually);
        calendarLayout=getView().findViewById(R.id.calendarLayout);
        TextView locationText=getView().findViewById(R.id.locationText);
        orLayout=getView().findViewById(R.id.orLayout);
        timeLayout =getView().findViewById(R.id.timeLayout);
        nextButton=getView().findViewById(R.id.readyButton);
        progressBar=getView().findViewById(R.id.progressBar);
        workersList=new ArrayList<>();
        usedList=new ArrayList<>();
        idList=new ArrayList<>();

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        profilePicture=sharedPreferences.getString("profilePicture","");


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();


                    }
                });

        FirebaseDatabase.getInstance().getReference("Workers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workersList=new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if(!snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        for (int i = 0; i < selectedList.size(); i++) {
                            String jobDescription = selectedList.get(i);
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                if (snapshot2.getKey().equals(jobDescription)) {
                                    WorkerModel model = snapshot2.getValue(WorkerModel.class);
                                    workersList.add(model);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                name = task.getResult().child("firstName").getValue()+" "+ task.getResult().child("lastName").getValue();
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
        shareLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                intent.putExtra("to","employer");
                startActivity(intent);

            }
        });

        insertLocationManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                intent.putExtra("search","true");
                intent.putExtra("to","employer");

                startActivity(intent);
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("list",selectedList);
                intent.putExtra("address", locationText.getText().toString());
                intent.putExtra("to","employer");

                startActivity(intent);

            }
        });
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar_sheet);
        CalendarView calendar = dialog.findViewById(R.id.calendar);
        TextView readyButton = dialog.findViewById(R.id.readyButton);
        TextView cancelButton = dialog.findViewById(R.id.cancelButton);
        dateText = getView().findViewById(R.id.dateText);
        calendar.setMinDate((new Date().getTime()));

        SimpleDateFormat sdff = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat sdfff = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat sdffff = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdfffff = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat sdfffffff = new SimpleDateFormat("mm", Locale.getDefault());
        year= Integer.parseInt(sdff.format(new Date()));
        month= Integer.parseInt(sdfff.format(new Date()));
        day= Integer.parseInt(sdffff.format(new Date()));
        hour= Integer.parseInt(sdfffff.format(new Date()));
        minute=Integer.parseInt(sdfffffff.format(new Date()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentdate = sdf.format(new Date());
        date=currentdate;
        dateText.setText(date);
        calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);

                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        i1=i1+1;
                        String correctDay;
                        if(String.valueOf(i2).length()<2){
                            correctDay = "0"+i2;
                        }else{
                            correctDay= String.valueOf(i2);
                        }
                        date=correctDay+"."+i1+"."+i;
                        year=i;
                        month=i1;
                        day=i2;
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });

                readyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateText.setText(date);
                        dialog.hide();
                    }
                });
            }
        });


        final Dialog dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.time_sheet);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.show();
                dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog2.getWindow().setGravity(Gravity.CENTER);
                TimePicker timePicker = dialog2.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                timePicker.setHour(12);
                timePicker.setMinute(0);
                hour=12;
                min="00";
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                        hour=i;
                        minute=i1;


                        if(minute<10){
                            min="0"+minute;
                        }else{
                            min=minute+"";
                        }
                        time=hour+":"+min;

                    }
                });

                TextView ready=dialog2.findViewById(R.id.readyBut);
                ready.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView timeText=getView().findViewById(R.id.timeText);
                        timeText.setText(hour+":"+min);
                        dialog2.hide();
                    }
                });

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!gotAddress) {
                    Toast.makeText(getActivity(), "Choose a location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(workersList.size()>0) {
                    int size =workersList.size();
                    for (int i = 0; i < size; i++) {

                        id = UUID.randomUUID().toString();
                        Double latt = workersList.get(i).getLat();
                        Double lngg = workersList.get(i).getLng();
                        Double dist = distance(lat, lng, latt, lngg);
                        String distt = String.valueOf(dist);
                        String correctstring[] = distt.split("\\.");
                        String etäisyys = workersList.get(i).getDistance();
                        int i1 = Integer.parseInt(correctstring[0]);
                        int i2 = Integer.parseInt(etäisyys);
                        int subtract = i1 - i2;

                        String comp = String.valueOf(subtract);
                        if (subtract == 0) {

                        }
                        if (subtract > 0) {

                        }
                        if (subtract < 0) {
                            String workerToken = workersList.get(i).getToken();
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "TEEST", "TAST"
                                    , getActivity().getApplicationContext(), getActivity());
                            notificationsSender.SendNotifications(token, correctstring[0], "employer");

                            String osoite = workersList.get(i).getAddress();
                            String jobDescription = workersList.get(i).getJobDescriptions();
                            String workerUID = workersList.get(i).getUserid();
                            String workerName = workersList.get(i).getName();
                            String workerProfilePicture=workersList.get(i).getWorkerProfilePicture();
                            WorkPlaceModel model = new WorkPlaceModel(correctstring[0], name,
                                    jobDescription, id, osoite, workerUID, workerToken, FirebaseAuth.getInstance().getUid(), address, token,
                                    date, String.valueOf(hour),workerName,profilePicture, workerProfilePicture,"open");
                            FirebaseDatabase.getInstance().getReference().child("MyAvailableJobs").child(workersList.get(i).getUserid()).child(id).setValue(model);
                        }

                        for (int ii = 0; ii < selectedList.size(); ii++) {
                            if(!usedList.contains(selectedList.get(ii)) && !idList.contains(id)) {
                                usedList.add(selectedList.get(ii));
                                idList.add(id);
                                EmployerModel model = new EmployerModel(lat, lng, name, selectedList.get(ii), FirebaseAuth.getInstance().getUid()
                                        , token, id, address, date, hour + ":" + minute,profilePicture);
                                FirebaseDatabase.getInstance().getReference("WorkPlaces").child(id).setValue(model);
                                FirebaseDatabase.getInstance().getReference("MyWorkPlaces").child(FirebaseAuth.getInstance().getUid()).child(id).setValue(model);
                            }
                        }
                    }

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finishAffinity();
                    Toast.makeText(getActivity(), "Created succesfully", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i=0;i<selectedList.size();i++) {
                        id = UUID.randomUUID().toString();
                        EmployerModel model = new EmployerModel(lat,lng,name,
                                selectedList.get(i),FirebaseAuth.getInstance().getUid(),token,id,address,date,hour+":"+minute,profilePicture);
                        FirebaseDatabase.getInstance().getReference("WorkPlaces").child(id).setValue(model);
                        FirebaseDatabase.getInstance().getReference("MyWorkPlaces").child(FirebaseAuth.getInstance().getUid()).child(id).setValue(model);
                    }
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finishAffinity();
                    Toast.makeText(getActivity(), "Created succesfully", Toast.LENGTH_SHORT).show();

                }




            }
        });

    }

    public ApplyForEmployerInfo2Fragment() {
    }

    public static ApplyForEmployerInfo2Fragment newInstance(String param1, String param2) {
        ApplyForEmployerInfo2Fragment fragment = new ApplyForEmployerInfo2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apply_for_employer_info2, container, false);
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