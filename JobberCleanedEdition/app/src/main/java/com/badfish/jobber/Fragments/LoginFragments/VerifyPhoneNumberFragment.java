package com.badfish.jobber.Fragments.LoginFragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badfish.jobber.Models.User;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyPhoneNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyPhoneNumberFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String c1,c2,c3,c4,c5,c6;
    String verificationID;
    String phoneNumber;
    String tokenId;
    RelativeLayout nextLayout,mainLayout;
    ProgressBar progressBar;
    PhoneAuthProvider.ForceResendingToken tokenn;
    boolean codeSent=false;

    public VerifyPhoneNumberFragment() {
        // Required empty public constructor
    }

    public static VerifyPhoneNumberFragment newInstance(String param1, String param2) {
        VerifyPhoneNumberFragment fragment = new VerifyPhoneNumberFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_phone_number, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber","");
        progressBar=getView().findViewById(R.id.progressBar);
        mainLayout=getView().findViewById(R.id.verifyCodeLayout);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!codeSent){
                    Toast.makeText(getActivity(), "Code sending failed", Toast.LENGTH_SHORT).show();
                    try{
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }, 10000);
        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                return false;            }
        };



        if(!codeSent) {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(0L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential credential) {
                                    // This callback will be invoked in two situations:
                                    // 1 - Instant verification. In some cases the phone number can be instantly
                                    //     verified without needing to send or enter a verification code.
                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                    //     detect the incoming verification SMS and perform verification without
                                    //     user action.
                                    Log.d(TAG, "onVerificationCompleted:" + credential);

                                    signInWithPhoneAuthCredential(credential);
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w(TAG, "onVerificationFailed", e);

                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();

                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                        Toast.makeText(getActivity(), "Too many requests", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    // Show a message and update the UI
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId,
                                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    // The SMS verification code has been sent to the provided phone number, we
                                    // now need to ask the user to enter the code and then construct a credential
                                    // by combining the code with a verification ID.
                                    Log.d(TAG, "onCodeSent:" + verificationId);
                                    Toast.makeText(getContext(), "Code sent", Toast.LENGTH_SHORT).show();
                                    // Save verification ID and resending token so we can use them later
                                    verificationID = verificationId;
                                    mainLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    codeSent = true;

                                }
                            })          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }

        EditText verifyCode1,verifyCode2,verifyCode3,verifyCode4,verifyCode5,verifyCode6;
        verifyCode1=getView().findViewById(R.id.verifyCode1);
        verifyCode2=getView().findViewById(R.id.verifyCode2);
        verifyCode3=getView().findViewById(R.id.verifyCode3);
        verifyCode4=getView().findViewById(R.id.verifyCode4);
        verifyCode5=getView().findViewById(R.id.verifyCode5);
        verifyCode6=getView().findViewById(R.id.verifyCode6);

        verifyCode1.setOnKeyListener(keyListener);
        verifyCode2.setOnKeyListener(keyListener);
        verifyCode3.setOnKeyListener(keyListener);
        verifyCode4.setOnKeyListener(keyListener);
        verifyCode5.setOnKeyListener(keyListener);
        verifyCode6.setOnKeyListener(keyListener);


        verifyCode1.requestFocus();
        verifyCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyCode1.getText().toString().isEmpty()){
                    verifyCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!verifyCode1.getText().toString().isEmpty()){
                    verifyCode2.requestFocus();
                    c1=verifyCode1.getText().toString();
                }

            }
        });

        verifyCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyCode2.getText().toString().isEmpty()){
                    verifyCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!verifyCode2.getText().toString().isEmpty()){
                    verifyCode3.requestFocus();
                    c2=verifyCode2.getText().toString();

                }else{
                    verifyCode1.requestFocus();
                }
            }
        });

        verifyCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyCode3.getText().toString().isEmpty()){
                    verifyCode4.requestFocus();
                    c3=verifyCode3.getText().toString();

                }else{
                    verifyCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        verifyCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!verifyCode4.getText().toString().isEmpty()){
                    verifyCode5.requestFocus();
                    c4=verifyCode4.getText().toString();

                }else{
                    verifyCode3.requestFocus();
                }
            }
        });
        verifyCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!verifyCode5.getText().toString().isEmpty()){
                    verifyCode6.requestFocus();
                    c5=verifyCode5.getText().toString();

                }else{
                    verifyCode4.requestFocus();
                }
            }
        });
        verifyCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!verifyCode6.getText().toString().isEmpty()){
                    c6=verifyCode6.getText().toString();
                    String verificationCode=c1+c2+c3+c4+c5+c6;

                    if(verificationID!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, verificationCode);
                        signInWithPhoneAuthCredential(credential);

                    }else{
                        Toast.makeText(getActivity(), "verifi null", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    verifyCode5.requestFocus();
                }
            }
        });
        nextLayout=getView().findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificationID!=null){
                    String verificationCode=c1+c2+c3+c4+c5+c6;
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        TextView resendButton = getView().findViewById(R.id.resendButton);

        verifyCode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                return false;
            }
        });

        verifyCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if(verifyCode2.getText().toString().isEmpty()){
                        verifyCode1.requestFocus();
                        verifyCode1.setText("");
                    }
                }
                return false;
            }
        });
        verifyCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if(verifyCode3.getText().toString().isEmpty()){
                        verifyCode2.requestFocus();
                        verifyCode2.setText("");
                    }
                }
                return false;
            }
        });
        verifyCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if(verifyCode4.getText().toString().isEmpty()){
                        verifyCode3.requestFocus();
                        verifyCode3.setText("");
                    }                }
                return false;
            }
        });
        verifyCode5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if(verifyCode5.getText().toString().isEmpty()){
                        verifyCode4.requestFocus();
                        verifyCode4.setText("");
                    }                }
                return false;
            }
        });
        verifyCode6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if(verifyCode6.getText().toString().isEmpty()){
                        verifyCode5.requestFocus();
                        verifyCode5.setText("");
                    }
                }
                return false;
            }
        });

        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendButton.setText("Resend code in " + millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }
            public void onFinish() {
                resendButton.setText("Resend code");
                resendButton.setTextColor(Color.parseColor("#FFFFFF"));
                resendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resendVerificationCode(phoneNumber,tokenn);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }

        }.start();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tokenId = task.getResult();
                        SharedPreferences pref = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", tokenId);
                        editor.commit();
                    }
                });

    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        TextView resendButton = getView().findViewById(R.id.resendButton);
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendButton.setText("Resend code in " + millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }
            public void onFinish() {
                resendButton.setText("Resend code");
                resendButton.setTextColor(Color.parseColor("#FFFFFF"));
                resendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resendVerificationCode(phoneNumber,tokenn);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }

        }.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                (new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        Log.d(TAG, "onVerificationCompleted:" + credential);

                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(getActivity(), "Too many requests", Toast.LENGTH_SHORT).show();
                        }

                        // Show a message and update the UI
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Code sent", Toast.LENGTH_SHORT).show();
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        // Save verification ID and resending token so we can use them later
                        verificationID = verificationId;

                    }
                }) ,         // OnVerificationStateChangedCallbacks
                token);



    }

    

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("phoneNumber", phoneNumber).commit();
                            FirebaseUser user = task.getResult().getUser();
                            String name = sharedPreferences.getString("firstName","");
                            String lastName = sharedPreferences.getString("lastName","");
                            User model = new User(name,lastName,"","false",user.getUid(),tokenId,"","noProfilePicture");
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sharedPreferences.edit().putString("codeSent", "false").commit();
                                    sharedPreferences.edit().putBoolean("hasLoggedIn", true).commit();
                                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_right_to_left).replace(R.id.frameLayout, new ChooseRoleFragment()).commit();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getActivity(), "Check your code", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}