package com.badfish.jobber.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badfish.jobber.Models.UpdateModel;
import com.badfish.jobber.Models.User;
import com.badfish.jobber.R;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class LaunchActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    FirebaseFunctions mFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_launch);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ProgressBar progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
        mFunctions = FirebaseFunctions.getInstance();

        FirebaseDatabase.getInstance().getReference("update").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                   UpdateModel model = snapshot.getValue(UpdateModel.class);
                    PackageInfo pInfo = null;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String verName = pInfo.versionName;
                        int verCode = pInfo.versionCode;
                        String c=String.valueOf(verCode);
                        if(model.getVersionCode().equals(c) && model.getVersionName().equals(verName)){
                            if(hasLoggedIn){
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("users");
                                userID = user.getUid();
                                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        User userProfile = snapshot.getValue(User.class);
                                        if(userProfile != null){
                                            String locked = userProfile.getLocked();
                                            Boolean deleted = snapshot.child("deleted").exists();
                                            if(deleted){
                                                Toast.makeText(LaunchActivity.this, "Current account has been removed", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                                                return;
                                            }
                                            if(locked.equals("false")) {
                                                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                                                finishAffinity();
                                            }else{
                                                Toast.makeText(LaunchActivity.this, "Account is locked", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                                                finishAffinity();
                                            }
                                        }else{
                                            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                                            finishAffinity();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }else{
                                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                                finishAffinity();
                            }

                        }else{
                            progressBar.setVisibility(View.GONE);
                            final Dialog dialog = new Dialog(LaunchActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.update_sheet);
                            dialog.show();
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                            dialog.getWindow().setGravity(Gravity.CENTER);
                            dialog.setCancelable(false);
                            RelativeLayout button = dialog.findViewById(R.id.updateButton);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(model.getUrl()!=null) {
                                        if(model.getUrl().contains("https://")||model.getUrl().contains("http://")) {
                                            Uri uri = Uri.parse(model.getUrl()); // missing 'http://' will cause crashed
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(LaunchActivity.this, "Invalid url", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(LaunchActivity.this, "Url is empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(LaunchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LaunchActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}