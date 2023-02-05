package com.badfish.jobber.Fragments;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Models.MessageModel;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class ConfirmPictureFragment extends Fragment {
    ImageView confirmImageView;
    ImageView sendButton;
    Uri imageUri;
    StorageReference storageRef;
    boolean employer;
    String userID,jobID,otherUID,workerUID;
    String imageID;
    ProgressBar progressBar;
    RelativeLayout mainLayout;
    EditText captionET;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmImageView=getView().findViewById(R.id.confirmImageView);
        imageUri=MainActivity.imageUri;
        sendButton=getView().findViewById(R.id.sendButton);
        Picasso.with(getContext()).load(imageUri).into(confirmImageView);
        storageRef=MainActivity.storageRef;
        employer=MainActivity.mEmployer;
        captionET=getView().findViewById(R.id.captionET);
        userID=FirebaseAuth.getInstance().getUid();
        jobID=MainActivity.mJobID;
        otherUID=MainActivity.mOtherUID;
        workerUID=MainActivity.mWorkerUID;
        progressBar= getView().findViewById(R.id.progressBar);
        mainLayout=getView().findViewById(R.id.mainLayout);
        imageID = UUID.randomUUID().toString();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Long tsLong = System.currentTimeMillis();
                MessageModel model = new MessageModel(captionET.getText().toString().trim(), userID , tsLong, imageID+"."+getFileExtension(imageUri));
                String randomID = UUID.randomUUID().toString();
                if(employer){
                    //user is employer
                    String caption;
                    if(!captionET.getText().toString().trim().equals("")){
                        caption=captionET.getText().toString().trim();
                    }else{
                        caption="Image";
                    }

                    FirebaseDatabase.getInstance().getReference("MyChats").child(userID).child(jobID).child(otherUID).child(randomID)
                            .setValue(model);
                    FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(MainActivity.jobID)
                            .child(workerUID)
                            .child("lastText").setValue(caption);
                    FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(MainActivity.jobID).child(workerUID)
                            .child("lastTextTime").setValue(tsLong);

                    FirebaseDatabase.getInstance().getReference("MyChats").child(workerUID).child(jobID).child(userID).child(randomID)
                            .setValue(model);

                    FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                            .child("lastText").setValue(caption);
                    FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                            .child("lastTextTime").setValue(tsLong);
                    uploadFile();
                }else{
                    //user is worker
                    String caption;
                    if(!captionET.getText().toString().trim().equals("")){
                        caption=captionET.getText().toString().trim();
                    }else{
                        caption="Image";
                    }
                    FirebaseDatabase.getInstance().getReference("MyChats")
                            .child(userID)
                            .child(jobID)
                            .child(otherUID).child(randomID)
                            .setValue(model);
                    FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers")
                            .child(otherUID)
                            .child(MainActivity.jobID)
                            .child(workerUID)
                            .child("lastText").setValue(caption);
                    FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(otherUID).child(MainActivity.jobID).child(workerUID)
                            .child("lastTextTime").setValue(tsLong);

                    FirebaseDatabase.getInstance().getReference("MyChats").child(otherUID).child(jobID).child(userID).child(randomID)
                            .setValue(model);

                    FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                            .child("lastText").setValue(caption);
                    FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                            .child("lastTextTime").setValue(tsLong);
                    uploadFile();
                }
            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR= getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(imageUri != null){
            StorageReference fileReference= storageRef.child(imageID+"."+ getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference fileReference2= MainActivity.otherStorageRef.child(imageID+"."+ getFileExtension(imageUri));
                    fileReference2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                    .replace(R.id.chatFrameLayout,new ChatFragment()).commit();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                }
            });




        }else{
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public ConfirmPictureFragment() {
        // Required empty public constructor
    }

    public static ConfirmPictureFragment newInstance(String param1, String param2) {
        ConfirmPictureFragment fragment = new ConfirmPictureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_picture, container, false);
    }
}