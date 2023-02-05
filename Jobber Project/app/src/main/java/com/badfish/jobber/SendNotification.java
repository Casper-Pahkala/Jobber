package com.badfish.jobber;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.badfish.jobber.Activities.LaunchActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {
    String userFcmToken;
    String title, firstName, lastName;
    String body;
    Context mContext;
    Activity mActivity;
    JSONObject notiObject;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    String name;

    public SendNotification(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public static void SendChatNotification(String title, String body, String tokenId){
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("token", tokenId);

        FirebaseFunctions.getInstance().getHttpsCallable("sendChatNotification")
                .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult result) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void SendEmployerNotifications(String jobDescription, String distance, String tokenId){
        Map<String, Object> data = new HashMap<>();
        data.put("jobDescription", jobDescription);
        data.put("distance", distance);
        data.put("token", tokenId);
        FirebaseFunctions.getInstance().getHttpsCallable("sendEmployerNotification")
                .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult result) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void SendAcceptedJobNotification(String jobDescription, String name, String tokenId){
        Map<String, Object> data = new HashMap<>();
        data.put("jobDescription", jobDescription);
        data.put("name", name);
        data.put("token", tokenId);
        FirebaseFunctions.getInstance().getHttpsCallable("sendAcceptedJobNotification")
                .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult result) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void SendAcceptedWorkerNotification(String jobDescription, String name, String tokenId){
        Map<String, Object> data = new HashMap<>();
        data.put("jobDescription", jobDescription);
        data.put("name", name);
        data.put("token", tokenId);
        FirebaseFunctions.getInstance().getHttpsCallable("sendAcceptedWorkerNotification")
                .call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult result) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
