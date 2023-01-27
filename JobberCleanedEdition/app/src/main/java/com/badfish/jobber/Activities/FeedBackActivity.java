package com.badfish.jobber.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badfish.jobber.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        ImageView backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextInputEditText feedBackText=findViewById(R.id.feedBackText);
        TextView sendButton=findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = feedBackText.getText().toString();
                String randomID= UUID.randomUUID().toString();
                FirebaseDatabase.getInstance().getReference("FeedBack").child(randomID).child("FeedBack").setValue(text);
                FirebaseDatabase.getInstance().getReference("FeedBack").child(randomID).child("UserID").setValue(FirebaseAuth.getInstance().getUid());
                Toast.makeText(FeedBackActivity.this, "Feedback sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FeedBackActivity.this, MainActivity.class));
                finishAffinity();
            }
        });
    }
}