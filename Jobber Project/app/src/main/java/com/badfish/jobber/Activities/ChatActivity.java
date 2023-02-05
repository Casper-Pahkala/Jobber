package com.badfish.jobber.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badfish.jobber.Adapters.MessageAdapter;
import com.badfish.jobber.Fragments.ChatFragment;
import com.badfish.jobber.Models.MessageModel;
import com.badfish.jobber.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
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

    private static  final int PICK_IMAGE_REQUEST = 1;
    boolean imageChanged =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportFragmentManager().beginTransaction().replace(R.id.chatFrameLayout, new ChatFragment()).commit();



    }

}