package com.badfish.jobber.Fragments.LoginFragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badfish.jobber.Activities.LaunchActivity;
import com.badfish.jobber.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class FirstNameFragment extends Fragment {
    String name;
    FirebaseFunctions mFunctions;


    public FirstNameFragment() {

    }

    public static FirstNameFragment newInstance(String param1, String param2) {
        FirstNameFragment fragment = new FirstNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_name, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFunctions = FirebaseFunctions.getInstance();
        addMessage("inputMessage")
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), task.getResult(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        FirebaseApp.initializeApp(getContext());
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());


        EditText nameEditText= getActivity().findViewById(R.id.firstNameEditText);
        if(sharedPreferences!=null){
            if(sharedPreferences.getString("firstName","")!=null){
                name = sharedPreferences.getString("firstName","");
                nameEditText.setText(name);
            }
        }
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = String.valueOf(editable);

            }
        });

        RelativeLayout nextLayout = getView().findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditText.getText().toString().trim().equals("")){
                    nameEditText.setError("Enter a valid name");
                    return;
                }
                name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
                sharedPreferences.edit().putString("firstName", name).commit();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_right_to_left).replace(R.id.frameLayout, new LastNameFragment()).commit();
            }
        });
    }


    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
}