package com.badfish.jobber.Fragments.MainFragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.Fragments.MainFragments.EmployerFragments.EmployerAccountFragment;
import com.badfish.jobber.Fragments.MainFragments.WorkerFragments.WorkerAccountFragment;
import com.badfish.jobber.Models.User;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    String firstName,lastName,email,bio;
    EditText firstNameEt,lastNameEt,emailEt,bioEt;
    String firstNameOG,lastNameOG,emailOG,bioOG;
    TextView saveChanges;
    String changed1,changed2,changed3,changed4;


    private static  final int PICK_IMAGE_REQUEST = 1;
    RelativeLayout imageLayout;
    CircleImageView imageView;
    Uri imageUri;
    StorageReference storageRef;
    TextView noPictureText;
    boolean imageChanged =false;
    RelativeLayout mainLayout;
    ProgressBar progressBar;
    boolean loading=false;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        storageRef= FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("profilePicture");
        imageLayout = getView().findViewById(R.id.imageLayout);
        imageView=getView().findViewById(R.id.imageView);
        mainLayout=getView().findViewById(R.id.mainContent);
        progressBar=getView().findViewById(R.id.progressBar);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        View.OnKeyListener keyListener = new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    if(MainActivity.employer){
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                .replace(R.id.mainFrameLayout,new EmployerAccountFragment()).commit();

                    }else{
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                .replace(R.id.mainFrameLayout,new WorkerAccountFragment()).commit();
                    }
                    return true;
                }
                return false;
            }
        };




        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyPrefsFile",0);
        firstNameEt=getView().findViewById(R.id.firstNameEditText);
        lastNameEt=getView().findViewById(R.id.lastNameEditText);
        emailEt=getView().findViewById(R.id.emailEditText);
        bioEt=getView().findViewById(R.id.bioEditText);

        imageView.requestFocus();

        firstNameEt.setOnKeyListener(keyListener);
        lastNameEt.setOnKeyListener(keyListener);
        emailEt.setOnKeyListener(keyListener);
        bioEt.setOnKeyListener(keyListener);

        saveChanges=getView().findViewById(R.id.saveChangesButton);
        try{
            firstName=sharedPreferences.getString("firstName","");
            lastName=sharedPreferences.getString("lastName","");

            firstNameEt.setText(firstName);
            lastNameEt.setText(lastName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        firstNameOG=firstNameEt.getText().toString();
        lastNameOG=lastNameEt.getText().toString();


        changed1 = firstNameEt.getText().toString();
        changed2 = lastNameEt.getText().toString();
        changed3 = emailEt.getText().toString();
        changed4 = bioEt.getText().toString();
        noPictureText=getView().findViewById(R.id.noPictureText);
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("profilePicture").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().contains("noProfilePicture")) {
                    StorageReference storageRef= FirebaseStorage.getInstance().getReference("noProfilePictures");
                    storageRef.child(dataSnapshot.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getContext())
                                    .load(uri)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            String firstLetter = firstName.substring(0,1);
                                            noPictureText.setText(firstLetter);
                                            noPictureText.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

                                            mainLayout.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

                }else{
                    StorageReference storageRef= FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("profilePicture");

                    storageRef.child(dataSnapshot.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getContext())
                                    .load(uri)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar.setVisibility(View.GONE);
                                            mainLayout.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

                }
            }
        });

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User model = task.getResult().getValue(User.class);
                email=model.getEmail();
                bio=model.getBio();
                emailEt.setText(email);
                bioEt.setText(bio);
                emailOG = emailEt.getText().toString();
                bioOG=bioEt.getText().toString();
                listenToTextChanges();

            }
        });

        ImageView backButton=getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.employer){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                            .replace(R.id.mainFrameLayout,new EmployerAccountFragment()).commit();

                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                            .replace(R.id.mainFrameLayout,new WorkerAccountFragment()).commit();
                }

            }
        });



        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading=true;
                saveChanges.setClickable(false);
                mainLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_background));
                try{
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(imageChanged){
                    uploadFile();
                }
                SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("MyPrefsFile",0);
                if(!changed1.trim().equals("") && !changed1.equals(firstNameOG)) {
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("firstName").setValue(changed1.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sharedPreferences1.edit().putString("firstName", changed1.trim()).commit();

                            read2();
                        }
                    });
                }else{
                    read2();
                }

            }
        });
    }

    private void listenToTextChanges(){
        firstNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed1 = firstNameEt.getText().toString();
                changed1 = changed1.substring(0,1).toUpperCase()+ changed1.substring(1).toLowerCase();

                if(!changed4.equals(bioOG) || !changed3.equals(emailOG) || !changed2.equals(lastNameOG) || !changed1.equals(firstNameOG)){
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.white_background));
                    saveChanges.setClickable(true);



                }else{
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_background));
                    saveChanges.setClickable(false);

                }
            }
        });
        lastNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastNameEt.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed2 = lastNameEt.getText().toString();
                changed2 = changed2.substring(0,1).toUpperCase()+ changed2.substring(1).toLowerCase();

                if(!changed4.equals(bioOG) || !changed3.equals(emailOG) || !changed2.equals(lastNameOG) || !changed1.equals(firstNameOG)){
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.white_background));
                    saveChanges.setClickable(true);
                }else{
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_background));
                    saveChanges.setClickable(false);
                }
            }
        });

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailEt.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed3 = emailEt.getText().toString();
                if(changed3.equals("")){
                    changed3="-";
                }
                if(!changed4.equals(bioOG) || !changed3.equals(emailOG) || !changed2.equals(lastNameOG) || !changed1.equals(firstNameOG)){
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.white_background));
                    saveChanges.setClickable(true);
                }else{
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_background));
                    saveChanges.setClickable(false);
                }
            }
        });

        bioEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bioEt.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed4 = bioEt.getText().toString();
                if(changed4.equals("")){
                    changed4="-";
                }
                if(!changed4.equals(bioOG) || !changed3.equals(emailOG) || !changed2.equals(lastNameOG) || !changed1.equals(firstNameOG)){
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.white_background));
                    saveChanges.setClickable(true);
                }else{
                    saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_background));
                    saveChanges.setClickable(false);
                }
            }
        });

    }
    private void read2(){
        SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("MyPrefsFile",0);

        if(!changed2.trim().equals("") && !changed2.equals(lastNameOG)){
            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("lastName").setValue(changed2.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sharedPreferences1.edit().putString("lastName",changed2.trim()).commit();
                    read3();
                }
            });
        }else{
            read3();
        }

    }
    private void read3(){
        SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("MyPrefsFile",0);

        if(!changed3.trim().equals("") && !changed3.equals(emailOG)){

            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("email").setValue(changed3.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sharedPreferences1.edit().putString("email",changed3.trim()).commit();
                    read4();

                }
            });
        }else{
            read4();
        }

    }
    private void read4(){
        SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("MyPrefsFile",0);

        if(!changed4.trim().equals("") && !changed4.equals(bioOG)) {
            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("bio").setValue(changed4.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sharedPreferences1.edit().putString("bio", changed4.trim()).commit();
                    if(!imageChanged){
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                .replace(R.id.mainFrameLayout,new ProfileFragment()).commit();
                        loading=false;
                    }
                }
            });
        }else{
            if(!imageChanged){
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                        .replace(R.id.mainFrameLayout,new ProfileFragment()).commit();
                loading=false;

            }
        }
    }

    private void openFileChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            noPictureText.setVisibility(View.GONE);
            Picasso.with(getActivity()).load(imageUri).into(imageView);
            imageChanged=true;
            saveChanges.setBackground(getActivity().getResources().getDrawable(R.drawable.white_background));
            saveChanges.setClickable(true);


        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR= getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(imageUri != null){
            StorageReference fileReference= storageRef.child("profilePicture"+"."+ getFileExtension(imageUri));

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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                }
            });

        }else{
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
                    if(loading){
                        return true;
                    }

                        if(MainActivity.employer){
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                .replace(R.id.mainFrameLayout,new EmployerAccountFragment()).commit();

                    }else{
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_stay_still)
                                .replace(R.id.mainFrameLayout,new WorkerAccountFragment()).commit();
                    }
                    return true;
                }
                return false;
            }
        });

    }
}