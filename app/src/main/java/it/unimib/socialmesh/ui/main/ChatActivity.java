package it.unimib.socialmesh.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.MessageAdapter;
import it.unimib.socialmesh.model.Message;
import it.unimib.socialmesh.util.FireBaseUtil;

public class ChatActivity extends AppCompatActivity {

    private EditText messageBox;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    String receiverRoom;
    String senderRoom;
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String receiverEmail = intent.getStringExtra("email");

        String senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //String senderIdToken = FirebaseAuth.getInstance().getCurrentUser().getId

        mDbRef = FirebaseDatabase.getInstance().getReference();

        senderRoom = FireBaseUtil.adjustPath(receiverEmail + senderEmail);
        receiverRoom = FireBaseUtil.adjustPath(senderEmail + receiverEmail);

        Log.d("senderRoom", senderRoom);
        Log.d("receiverRoom", receiverRoom);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        ImageView sendButton = findViewById(R.id.sentButton);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);


        //logic for adding data to recyclerView
        mDbRef.child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message message = postSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //adding the message to the database
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageBox.getText().toString();
                Message messageObject = new Message(message, senderEmail);

                mDbRef.child("chats").child(senderRoom).child("messages")
                        .push().setValue(messageObject)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mDbRef.child("chats").child(receiverRoom)
                                        .child("messages").push().setValue(messageObject);
                                messageBox.setText("");
                            }
                        });
            }
        });


    }
}