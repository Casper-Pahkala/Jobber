package com.badfish.jobber.Activities.ApplyForWorkerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplyForWorkerActivity extends AppCompatActivity{
    public static ArrayList<String> selectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_worker);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        selectedList=new ArrayList<>();

        if(getIntent().getExtras()!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ApplyForWorkerInfo2Fragment()).commit();
            return;
        }

        FirebaseDatabase.getInstance().getReference("Workers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DeletePreviousActivity()).commit();

                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ApplyForWorkerInfo1Fragment()).commit();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}