package com.badfish.jobber.Fragments.MainFragments.EmployerFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.badfish.jobber.Activities.LoginActivity;
import com.badfish.jobber.Fragments.MainFragments.ProfileFragment;
import com.badfish.jobber.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployerAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerAccountFragment extends Fragment {

    ImageView closeButton;
    TextView logoutButton, profileButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeButton=getView().findViewById(R.id.closeButton);
        logoutButton=getView().findViewById(R.id.logoutButton);
        profileButton=getView().findViewById(R.id.profileButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_right_to_left)
                        .replace(R.id.mainFrameLayout,new EmployerFragment()).commit();
            }
        });

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyPrefsFile",0);
        TextView nameText = getView().findViewById(R.id.nameText);
        nameText.setText(sharedPreferences.getString("firstName","")+" "+ sharedPreferences.getString("lastName",""));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("codeSent","false");
                editor.commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still2,R.anim.exit_stay_still2)
                        .replace(R.id.mainFrameLayout,new ProfileFragment()).commit();
            }
        });
    }

    public EmployerAccountFragment() {
    }

    public static EmployerAccountFragment newInstance(String param1, String param2) {
        EmployerAccountFragment fragment = new EmployerAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
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
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_stay_still,R.anim.exit_left_to_right)
                            .replace(R.id.mainFrameLayout,new EmployerFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}