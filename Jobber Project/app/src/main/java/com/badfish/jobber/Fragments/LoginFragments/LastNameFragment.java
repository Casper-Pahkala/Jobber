package com.badfish.jobber.Fragments.LoginFragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.badfish.jobber.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LastNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LastNameFragment extends Fragment {

    String lastName;
    public LastNameFragment() {

    }
    public static LastNameFragment newInstance(String param1, String param2) {
        LastNameFragment fragment = new LastNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_last_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText lastNameEditText= getActivity().findViewById(R.id.lastNameEditText);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new FirstNameFragment()).commit();
                    return true;
                }
                return false;            }
        };
        lastNameEditText.setOnKeyListener(keyListener);

        if(sharedPreferences!=null){
            if(sharedPreferences.getString("lastName","")!=null){
                lastName = sharedPreferences.getString("lastName","");
                lastNameEditText.setText(lastName);
            }
        }
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastName = String.valueOf(editable);

            }
        });

        RelativeLayout nextLayout = getView().findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastNameEditText.getText().toString().trim().equals("")){
                    lastNameEditText.setError("Enter a valid lastname");
                    return;
                }
                lastName = lastName.substring(0,1).toUpperCase()+lastName.substring(1).toLowerCase();
                sharedPreferences.edit().putString("lastName", lastName).commit();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_right_to_left).replace(R.id.frameLayout, new PhoneNumberFragment()).commit();
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right).replace(R.id.frameLayout, new FirstNameFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}