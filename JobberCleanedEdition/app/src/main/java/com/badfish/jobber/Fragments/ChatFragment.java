package com.badfish.jobber.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Adapters.MessageAdapter;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.EmployerFragment;
import com.badfish.jobber.Fragments.MainFragments.ProfileFragment;
import com.badfish.jobber.Fragments.MainFragments.WorkerFragments.WorkerFragment;
import com.badfish.jobber.Models.MessageModel;
import com.badfish.jobber.R;
import com.badfish.jobber.Services.FcmNotificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.CharMatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {
    EditText messageEditText;
    ImageView sendButton;
    RelativeLayout mainLayout, infoLayout;
    ImageView showMoreButton, backButton;
    String senderRoom;
    boolean expanded=false;
    LinearLayout expandedLayout;
    String workerName,jobDescription, address, time, date, workerUID,userID, jobID,token, otherUID;
    TextView workerNameText, jobDescriptionText, addressText, timeText, dateText;
    RecyclerView recyclerView;
    ArrayList<MessageModel> list;
    MessageAdapter adapter;
    boolean employer;
    CircleImageView imageView;
    TextView noPictureText;
    String pictureRef;
    ArrayList<String> usedMessagesList;
    private static ChatFragment instance = null;
    private static  final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    StorageReference storageRef;
    boolean imageChanged =false;
    Dialog dialog;
    boolean dialogShown=false;
    boolean toChat=false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageEditText=getView().findViewById(R.id.messageEditText);
        sendButton=getView().findViewById(R.id.sendButton);
        mainLayout=getView().findViewById(R.id.mainLayout);
        showMoreButton=getView().findViewById(R.id.showMoreButton);
        infoLayout=getView().findViewById(R.id.infoLayout);
        expandedLayout=getView().findViewById(R.id.expandedLayout);
        backButton=getView().findViewById(R.id.backButton);
        imageView=getView().findViewById(R.id.imageView);
        noPictureText=getView().findViewById(R.id.noPictureText);
        Bundle extras = getActivity().getIntent().getExtras();
        usedMessagesList=new ArrayList<>();
        pictureRef=MainActivity.pictureRef;
        workerName=MainActivity.otherName;
        jobDescription=MainActivity.mJobDescription;
        address=MainActivity.address;
        time=MainActivity.time;
        date=MainActivity.date;
        workerUID=MainActivity.workerUID;
        MainActivity.mWorkerUID=workerUID;
        userID= FirebaseAuth.getInstance().getUid();
        jobID = MainActivity.jobId;
        MainActivity.mJobID=jobID;
        otherUID=MainActivity.mOtherUID;
        instance=this;
        if(MainActivity.mRole.equals("employer")){
            //user is employer
            otherUID=workerUID;
            MainActivity.mOtherUID=otherUID;
            employer=true;
            MainActivity.mEmployer=true;

        }else{
            //user is worker
            otherUID=MainActivity.employerUID;
            MainActivity.mOtherUID=otherUID;
            employer=false;
            MainActivity.mEmployer=false;

        }

        workerNameText=getView().findViewById(R.id.workerNameText);
        jobDescriptionText=getView().findViewById(R.id.jobDescriptionText);
        addressText=getView().findViewById(R.id.addresstext);
        timeText=getView().findViewById(R.id.timeText);
        dateText=getView().findViewById(R.id.dateText);

        workerNameText.setText(workerName);
        jobDescriptionText.setText(jobDescription);
        addressText.setText(" "+address);
        dateText.setText(date);
        timeText.setText(" "+time);

        recyclerView=getView().findViewById(R.id.recycler);
        list=new ArrayList<>();
        View parent = (View) view.getParent();
        adapter= new MessageAdapter(getContext(),list,parent);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        token=getActivity().getSharedPreferences("Token",0).getString("token","");

        FirebaseDatabase.getInstance().getReference("MyChats")
                .child(userID)
                .child(jobID)
                .child(otherUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            if(!usedMessagesList.contains(String.valueOf(model.getTimestamp()))) {
                                list.add(model);
                                usedMessagesList.add(String.valueOf(model.getTimestamp()));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    list.sort(new Comparator<MessageModel>() {
                                        @Override
                                        public int compare(MessageModel messageModel, MessageModel t1) {
                                            return String.valueOf(messageModel.getTimestamp()).compareTo(String.valueOf(t1.getTimestamp()));
                                        }
                                    });
                                }

                            }

                        }adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(list.size());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(messageEditText.getText().toString().length()>0){
                    sendButton.setImageResource(R.drawable.send_message_icon);
                }else{
                    sendButton.setImageResource(R.drawable.paperclip_iconn);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageEditText.getText().toString().trim().length()>0){
                    String message = messageEditText.getText().toString().trim();
                    Long tsLong = System.currentTimeMillis();
                    MessageModel model = new MessageModel(message, userID, tsLong,"");
                    list.add(model);
                    usedMessagesList.add(String.valueOf(tsLong));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        list.sort(new Comparator<MessageModel>() {
                            @Override
                            public int compare(MessageModel messageModel, MessageModel t1) {
                                return String.valueOf(messageModel.getTimestamp()).compareTo(String.valueOf(t1.getTimestamp()));
                            }
                        });
                    }
                    adapter.notifyItemChanged(usedMessagesList.size()-1);
                    recyclerView.smoothScrollToPosition(list.size());

                    String randomID = UUID.randomUUID().toString();
                    if(employer){
                        FirebaseDatabase.getInstance().getReference("MyChats").child(userID).child(jobID).child(otherUID).child(randomID)
                                .setValue(model);
                        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(MainActivity.jobID)
                                .child(workerUID)
                                .child("lastText").setValue(message);
                        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(FirebaseAuth.getInstance().getUid()).child(MainActivity.jobID).child(workerUID)
                                .child("lastTextTime").setValue(tsLong);

                        FirebaseDatabase.getInstance().getReference("MyChats").child(workerUID).child(jobID).child(userID).child(randomID)
                                .setValue(model);

                        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                                .child("lastText").setValue(message);
                        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                                .child("lastTextTime").setValue(tsLong);

                    }else{

                        FirebaseDatabase.getInstance().getReference("MyChats").child(userID).child(jobID).child(otherUID).child(randomID)
                                .setValue(model);
                        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers")
                                .child(otherUID)
                                .child(MainActivity.jobID)
                                .child(workerUID)
                                .child("lastText").setValue(message);
                        FirebaseDatabase.getInstance().getReference("MyAcceptedWorkers").child(otherUID).child(MainActivity.jobID).child(workerUID)
                                .child("lastTextTime").setValue(tsLong);

                        FirebaseDatabase.getInstance().getReference("MyChats").child(otherUID).child(jobID).child(userID).child(randomID)
                                .setValue(model);

                        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                                .child("lastText").setValue(message);
                        FirebaseDatabase.getInstance().getReference("MyUpcomingJobs").child(workerUID).child(MainActivity.jobID)
                                .child("lastTextTime").setValue(tsLong);
                    }
                    messageEditText.setText("");
                    String jobIdDigits = CharMatcher.inRange('0', '9').retainFrom(jobID).substring(4,8);
                    String tokenDigits = CharMatcher.inRange('0', '9').retainFrom(token).substring(4,7);

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"TEEST","TAST",getActivity().getApplicationContext(),getActivity());
                    notificationsSender.SendChatNotifications(MainActivity.otherToken,message,tokenDigits+jobIdDigits,jobDescription);
                }else{

                    openFileChooser();
                    toChat=true;
                }


            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded){
                    expanded=false;
                    expandedLayout.setVisibility(View.GONE);
                    showMoreButton.setRotation(270);
                }else{
                    showMoreButton.setRotation(90);
                    expanded=true;
                    expandedLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employer){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                            .replace(R.id.mainFrameLayout, new EmployerFragment()).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                            .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
                }

            }
        });

        if(pictureRef.contains("noProfilePicture")) {
            StorageReference storageRef= FirebaseStorage.getInstance().getReference("noProfilePictures");
            storageRef.child(pictureRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext())
                            .load(uri)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    String firstLetter = workerName.substring(0,1);
                                    noPictureText.setText(firstLetter);
                                    noPictureText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {

                                }
                            });


                }
            });

        }else {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference(otherUID).child("profilePicture");

            storageRef.child(pictureRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext())
                            .load(uri)
                            .into(imageView);
                }
            });
        }


        ImageView moreButton = getView().findViewById(R.id.moreButton);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChat=false;
                openFileChooser();
            }
        });
        String path=getActivity().getApplicationContext().getPackageName();
        String root = "data/data/"+path+"/app_imageDir";
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        try{
            String name = sharedPreferences.getString("backgroundName","");
            setBackground(root,name );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setBackground(String path,String child){
        try {
            File f=new File(path, child);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)getView().findViewById(R.id.chatBackground);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            messageEditText.setText(e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            if(toChat) {
                MainActivity.imageUri = data.getData();
                imageUri = data.getData();
                imageChanged = true;
                MainActivity.storageRef = FirebaseStorage.getInstance().getReference("chatImages").child("MyChatImages").child(userID).child(jobID).child(otherUID);
                MainActivity.otherStorageRef = FirebaseStorage.getInstance().getReference("chatImages").child("MyChatImages").child(otherUID).child(jobID).child(userID);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chatFrameLayout, new ConfirmPictureFragment()).commit();
            }else{
                imageUri = data.getData();
                imageChanged = true;
                uploadToDevice();


            }
        }


    }
    private void uploadToDevice(){
        if(imageUri != null) {
            Picasso.with(getContext())
                    .load(imageUri)
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  try {
                                      ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                                      // path to /data/data/yourapp/app_data/imageDir
                                      File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                      // Create imageDir
                                      SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile",0);
                                      String name = System.currentTimeMillis() + ".jpg";
                                      sharedPreferences.edit().putString("backgroundName",name).commit();
                                      File mypath=new File(directory,name);
                                      FileOutputStream out = new FileOutputStream(mypath);

                                      try {

                                          bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                          String path=getActivity().getApplicationContext().getPackageName();
                                          String root = "data/data/"+path+"/app_imageDir";
                                          SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("MyPrefsFile",0);
                                          try{
                                              String namee = sharedPreferences1.getString("backgroundName","");
                                              setBackground(root,namee );
                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      } finally {
                                          try {
                                              out.close();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }
                                      }


                                  } catch (Exception e) {
                                      messageEditText.setText(e.getMessage());
                                      Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                  }
                              }

                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {

                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                              }
                          }
                    );
        }else{
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void openFileChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR= getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(imageUri != null){

            StorageReference fileReference= FirebaseStorage.getInstance().getReference("MyBackgrounds").child(System.currentTimeMillis()+"."+ getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid())
                            .child("profilePicture").setValue("profilePicture"+"."+ getFileExtension(imageUri));
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                            .replace(R.id.mainFrameLayout,new ProfileFragment()).commit();


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

    public ChatFragment() {
        // Required empty public constructor
    }
    public static ChatFragment getInstance() {
        return instance;
    }
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }

    public void showDialog(){
        dialogShown=true;
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_full_picture);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageView imageView=dialog.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
        try{
            MainActivity.storageRef.child(MainActivity.pictureRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if(employer){
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                                .replace(R.id.mainFrameLayout, new EmployerFragment()).commit();
                    }else{
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
                                .replace(R.id.mainFrameLayout, new WorkerFragment()).commit();
                    }

                    return true;
                }
                return false;
            }
        });
    }
}