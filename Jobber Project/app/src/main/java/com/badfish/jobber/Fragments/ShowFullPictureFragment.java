package com.badfish.jobber.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badfish.jobber.Activities.MainActivity;
import com.badfish.jobber.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ShowFullPictureFragment extends Fragment {
    ImageView imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=getView().findViewById(R.id.imageView);
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

    public ShowFullPictureFragment() {
        // Required empty public constructor
    }

    public static ShowFullPictureFragment newInstance(String param1, String param2) {
        ShowFullPictureFragment fragment = new ShowFullPictureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_full_picture, container, false);
    }
}