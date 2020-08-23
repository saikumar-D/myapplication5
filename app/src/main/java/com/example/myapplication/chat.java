package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat extends AppCompatActivity {
    private Toolbar ntoolbar;
   private String mchatUser;
   private DatabaseReference mrootref;
   private ImageView mchat_dp;
   private TextView mtop_name;
   private FirebaseAuth cauth;
   private String cur_user;
   private EditText meditmsg;
   private ImageButton msendbtn;
   private ImageButton maddbtn;
   private RecyclerView mmessagelist;
   private messageadapter madapter;
   private final List<message> messageList=new ArrayList<>();
private LinearLayoutManager mlinearlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ntoolbar = (Toolbar) findViewById(R.id.mtoolbar);
   setSupportActionBar(ntoolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mrootref= FirebaseDatabase.getInstance().getReference();
        cauth=FirebaseAuth.getInstance();
        cur_user=cauth.getUid();
        msendbtn=findViewById(R.id.send);
        maddbtn=findViewById(R.id.plus);
        madapter=new messageadapter(messageList);
        meditmsg=findViewById(R.id.chat_msg);
        mmessagelist=findViewById(R.id.chat_recycle);
        mlinearlayout=new LinearLayoutManager(this);
       mmessagelist.setHasFixedSize(true);
      mmessagelist.setLayoutManager(mlinearlayout);
        mmessagelist.setAdapter(madapter);


        mchatUser=getIntent().getStringExtra("user_id");
        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View action_bar_view =inflater.inflate(R.layout.chat_bar_custom,null);
        actionBar.setCustomView(action_bar_view);
     //   mrootref.keepSynced(true);
        loadmessage();

        mrootref.child("users").child(mchatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mtop= snapshot.child("name").getValue().toString();
                String mctop_dp=snapshot.child("image").getValue().toString();
                mtop_name=findViewById(R.id.top_name);
                mchat_dp=findViewById(R.id.chat_dp);
                mtop_name.setText(mtop);
              // Picasso.with(chat.this).load(mctop_dp).placeholder(R.drawable.profile).into(mchat_dp);
                Picasso.get().load(mctop_dp).placeholder(R.drawable.profile).into(mchat_dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
Toast.makeText(chat.this,"error",Toast.LENGTH_LONG).show();;
            }
        });

    mrootref.child("chat").child(cur_user).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(!snapshot.hasChild(cur_user))
            {
                Map chatuser =new HashMap<>();
                chatuser.put("seen","false");
                chatuser.put("timeStamp", ServerValue.TIMESTAMP);
                Map adduser =new HashMap();
                adduser.put("chat/"+cur_user+"/"+mchatUser,chatuser);
                adduser.put("chat/"+mchatUser+"/"+cur_user,chatuser);
                mrootref.updateChildren(adduser, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                       if(error!=null)
                        Log.d("chatlog",error.getMessage().toString());
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(chat.this,"error in chat",Toast.LENGTH_LONG).show();
        }
    });

msendbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        sendmessage();
    }
});


    }
    private void loadmessage() {

        mrootref.child("messages").child(cur_user).child(mchatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message message=snapshot.getValue(com.example.myapplication.message.class);
                messageList.add(message);
                madapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loadmsg",error.getMessage().toString());
            }
        });
    }


    private void sendmessage() {
        String message = meditmsg.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            String user_ref = "messages/" + cur_user + "/" + mchatUser;
            String chat_ref = "messages/" + mchatUser + "/" + cur_user;

            DatabaseReference mref = mrootref.child("messages").child(cur_user).child(mchatUser).push();
            String pushid = mref.getKey();
            Map messagemap = new HashMap<>();
            messagemap.put("message", message);
            messagemap.put("seen", "false");
            messagemap.put("type", "text");
            messagemap.put("timestamp", ServerValue.TIMESTAMP);
            messagemap.put("from",cur_user);
            Map msgusermap = new HashMap();
            msgusermap.put(user_ref + "/" + pushid, messagemap);
            msgusermap.put(chat_ref + "/" + pushid, messagemap);
            meditmsg.setText("");
            mrootref.updateChildren(msgusermap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error!=null)
                    Log.d("chatlog", error.getMessage().toString());
                }
            });


        }

    }

}