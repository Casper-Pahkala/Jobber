package com.badfish.jobber.Activities.ApplyForEmployer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.badfish.jobber.R;

import java.util.ArrayList;

public class ApplyForEmployerActivity extends AppCompatActivity {
    public static ArrayList<String> selectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_employer);
        selectedList=new ArrayList<>();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ApplyForEmployerInfo1Fragment()).commit();

        if(getIntent().getExtras()!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ApplyForEmployerInfo2Fragment()).commit();
            return;
        }
    }
}