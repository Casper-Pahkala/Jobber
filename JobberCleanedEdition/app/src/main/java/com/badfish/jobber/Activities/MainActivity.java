package com.badfish.jobber.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badfish.jobber.Adapters.UpComingJobsAdapter;
import com.badfish.jobber.Fragments.LoginFragments.ChooseRoleFragment;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.EmployerFragment;
import com.badfish.jobber.Fragments.MainFragments.WorkerFragments.UpcomingJobsFragment;
import com.badfish.jobber.Fragments.MainFragments.WorkerFragments.WorkerFragment;
import com.badfish.jobber.Models.WorkPlaceModel;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FrameLayout mainFrameLayout;
    String role;
    public static String jobID, jobDescription, jobDate, jobAddress, jobTime;
    public static boolean toAvailable=false, employer=false;
    String userID;
    public static ArrayList<WorkPlaceModel> workPlaceList;
    public static Uri imageUri;
    public static StorageReference storageRef, otherStorageRef;
    public static boolean mEmployer;
    public static UpComingJobsAdapter upComingJobsAdapter;
    boolean enteredApplication;
    public static String token;
    public static boolean loading=false, toMoreLayout=false;
    public static String mJobID,mOtherUID,mWorkerUID, otherName, date, address, time, employerUID, workerUID, mJobDescription, jobId,pictureRef, mRole,otherToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("Token", 0);
        if(pref!=null) {
            token = pref.getString("token", null);
        }else{
            Toast.makeText(this, "Token fetching from cache failed", Toast.LENGTH_SHORT).show();
        }

        userID=FirebaseAuth.getInstance().getUid();
        mainFrameLayout=findViewById(R.id.mainFrameLayout);
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefsFile",0);
        if(sharedPreferences!=null){
            role=sharedPreferences.getString("role","");
        }
        if(role.equals("employer")){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new EmployerFragment()).commit();

        }else if(role.equals("worker")){

            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
            upComingJobs();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new ChooseRoleFragment()).commit();
        }

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("profilePicture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("noProfilePicture")){
                    final int random = new Random().nextInt(8);
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid())
                            .child("profilePicture").setValue("noProfilePicture"+random+".png");
                    sharedPreferences.edit().putString("profilePicture","noProfilePicture"+random+".png").commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    public void upComingJobs(){
        workPlaceList=new ArrayList<>();
        upComingJobsAdapter=new UpComingJobsAdapter(this, workPlaceList,this);
        FirebaseDatabase.getInstance().getReference("Workers").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UpcomingJobsFragment.recyclerView.setVisibility(View.VISIBLE);
                    enteredApplication=true;
                }else{
                    UpcomingJobsFragment.noApplicationLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        WorkPlaceModel model = snapshot1.getValue(WorkPlaceModel.class);
                        workPlaceList.add(model);
                    }upComingJobsAdapter.notifyDataSetChanged();

                }else{
                    if(enteredApplication){
                        UpcomingJobsFragment.noJobsAvailabeLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public static void getProfilePicture(String ref){

    }

    @Override
    public void onBackPressed() {
        if(loading){

        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateParticularField("onlineStatus","online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateParticularField("onlineStatus","offline");
        Long tsLong = System.currentTimeMillis();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        FirebaseDatabase.getInstance().getReference("users").child(userID).child("lastOnline").setValue(date+"/"+tsLong);
    }

    public void updateParticularField(String fieldName,String fieldValue){
            if (token != null) {
                if (!token.equals("null")) {
                    FirebaseDatabase.getInstance().getReference("users").child(userID).child(fieldName).setValue(fieldValue);
                }
            }
    }
}