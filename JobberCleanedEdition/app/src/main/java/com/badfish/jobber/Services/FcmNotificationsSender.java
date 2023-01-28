package com.badfish.jobber.Services;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.badfish.jobber.BuildConfig;
import com.badfish.jobber.Models.User;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

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


    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = mContext.getResources().getString(com.badfish.jobber.R.string.SERVER_KEY);

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }

    public void SendNotifications(String token, String distance,String sender) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);


                if(userProfile != null){
                    firstName = userProfile.firstName;
                    lastName = userProfile.lastName;
                    name = firstName +" " + lastName;

                    requestQueue = Volley.newRequestQueue(mActivity);
                    JSONObject mainObj = new JSONObject();
                    try {
                        if(sender.equals("worker")){
                            mainObj.put("to", token);
                            notiObject = new JSONObject();
                            notiObject.put("title", "Uusi työntekijä");
                            notiObject.put("body", firstName+" "+lastName +" hyväksyi työtarjouksesi");

                        }else {
                            mainObj.put("to", token);
                            notiObject = new JSONObject();
                            notiObject.put("title", "Uusi työtarjous");
                            notiObject.put("body", "Olet saanut uuden työtarjouksen " + distance + "km päässä");
                        }
                         // enter icon that exists in drawable only



                        mainObj.put("notification", notiObject);


                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                // code run is got response

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // code run is got error

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {


                                Map<String, String> header = new HashMap<>();
                                header.put("content-type", "application/json");
                                header.put("authorization", "key=" + fcmServerKey);
                                return header;


                            }
                        };
                        requestQueue.add(request);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void SendChatNotifications(String token,String message,String tag,String jobName) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    firstName = userProfile.firstName;
                    lastName = userProfile.lastName;
                    name = firstName +" " + lastName;
                    requestQueue = Volley.newRequestQueue(mActivity);
                    JSONObject mainObj = new JSONObject();
                    try {
                            mainObj.put("to", token);
                            notiObject = new JSONObject();
                            notiObject.put("title", jobName);
                            notiObject.put("body", name+": "+message);
                            notiObject.put("tag",tag);
                        mainObj.put("notification", notiObject);


                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                // code run is got response

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // code run is got error

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {


                                Map<String, String> header = new HashMap<>();
                                header.put("content-type", "application/json");
                                header.put("authorization", "key=" + fcmServerKey);
                                return header;


                            }
                        };
                        requestQueue.add(request);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
