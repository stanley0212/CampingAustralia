package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Adapter.MessageAdapter;
import com.luvtas.campingau.Model.ChatModel;
import com.luvtas.campingau.Model.UserModel;
import com.luvtas.campingau.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    CircleImageView circleImageView;
    ImageView preview_page;

    ImageButton btn_send;
    EditText text_send;
    String receiver_user_id,receiver_user_photo,receiver_user_name;
    TextView from_personal_name;

    MessageAdapter messageAdapter;
    List<ChatModel> mChat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        circleImageView = findViewById(R.id.from_personal_photo);
        preview_page = findViewById(R.id.cancel);
        from_personal_name = findViewById(R.id.from_personal_name);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        receiver_user_id = getIntent().getStringExtra("profileid");
        receiver_user_photo = getIntent().getStringExtra("userphoto");
        receiver_user_name = getIntent().getStringExtra("username");

        from_personal_name.setText(receiver_user_name);

        Glide.with(getApplication()).load(receiver_user_photo).into(circleImageView);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),receiver_user_id,msg);
                } else {
                    Toast.makeText(getApplication(),getApplicationContext().getResources().getString(R.string.you_cannot_send_empty_message), Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child(receiver_user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                readMessage(firebaseUser.getUid(),receiver_user_id,receiver_user_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        preview_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("lastUpdate", ServerValue.TIMESTAMP);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String userid, String imageurl){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if(chatModel.getReceiver().equals(myid) && chatModel.getSender().equals(userid) ||
                        chatModel.getReceiver().equals(userid) && chatModel.getSender().equals(myid)){
                        mChat.add(chatModel);
                    }
                    messageAdapter = new MessageAdapter(ChatMessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
