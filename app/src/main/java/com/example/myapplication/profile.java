package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {
private TextView mprofile2_name,mfriend_status;
private CircleImageView mfriend_image;
private Button mrequest,mDecline_Request;
private DatabaseReference mdatabase;
private DatabaseReference mrequestdatabase;
private FirebaseUser user;
private String req_state;
private DatabaseReference mfrienddatabase;
private DatabaseReference mnotificationdatabase;
private DatabaseReference prootref;
private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String user_id =getIntent().getStringExtra("user_id");
        mprofile2_name=findViewById(R.id.profile2_name);

        mfriend_image=findViewById(R.id.friend_image);
        mfriend_status=findViewById(R.id.friend_status);
        prootref=FirebaseDatabase.getInstance().getReference();
        mnotificationdatabase=FirebaseDatabase.getInstance().getReference().child("Notification");
        //mnotificationdatabase.keepSynced(true);
        mrequest=findViewById(R.id.request);
        mfrienddatabase=FirebaseDatabase.getInstance().getReference().child("Friends");
        //mfrienddatabase.keepSynced(true);
        req_state="notfriend";
        mDecline_Request=findViewById(R.id.Decline_request);
        user= FirebaseAuth.getInstance().getCurrentUser();
//prootref.keepSynced(true);
//mdatabase.keepSynced(true);


       // String user_id2 = user.getUid();
        mrequestdatabase=FirebaseDatabase.getInstance().getReference().child("Request");
        mdatabase= FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dis_name = snapshot.child("name").getValue().toString();
                String dis_status = snapshot.child("status").getValue().toString();
                String dis_img =snapshot.child("image").getValue().toString();
                mprofile2_name.setText(dis_name);
                mfriend_status.setText(dis_status);
               // Picasso.with(profile.this).load(dis_img).placeholder(R.drawable.profile).into(mfriend_image);
                Picasso.get().load(dis_img).placeholder(R.drawable.profile).into(mfriend_image);
                mrequestdatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(user_id))
                        {
                            String req_type = snapshot.child(user_id).child("Request_type").getValue().toString();
                            if(req_type.equals("Recieved")) {
                                req_state="req_recieved";
                                mrequest.setText("AcceptRequest");
                                mDecline_Request.setVisibility(View.VISIBLE);
                                mDecline_Request.setEnabled(true);
                            }
                            else if(req_type.equals("sent"))
                                {
                                    mrequest.setText("cancelRequest");
                                    req_state="req_sent";
                                    mDecline_Request.setVisibility(View.INVISIBLE);
                                    mDecline_Request.setEnabled(false);
                                }

                        }
                        else
                        {
                            mfrienddatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(user_id))
                                    {
                                        req_state="friend";
                                        mrequest.setText("unfriend");
                                        mDecline_Request.setVisibility(View.INVISIBLE);
                                        mDecline_Request.setEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        mrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //*******NOT FRIEND
                if (req_state.equals("notfriend")) {
                    progress = new ProgressDialog(profile.this);
                    //uprogress.setTitle("updating");
                    progress.setMessage("please Wait ");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    DatabaseReference newnotificationref = prootref.child("Notification").child(user_id).push();
                    String newnotificationId = newnotificationref.getKey();
                    HashMap<String, String> notificationdata = new HashMap();
                    notificationdata.put("from", user.getUid());
                    notificationdata.put("type", "Request");
                    Map requestmap = new HashMap();
                    requestmap.put("Request/" + user.getUid() + "/" + user_id+ "/Request_type", "sent");
                    requestmap.put("Request/" + user_id + "/" + user.getUid() + "/Request_type", "Recieved");
                    requestmap.put("Notification/" + user_id + "/" + newnotificationId, notificationdata);
                    prootref.updateChildren(requestmap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                progress.hide();
                                Toast.makeText(profile.this, "error in profile", Toast.LENGTH_LONG).show();
                            }
                            else
                            {progress.hide();
                                mrequest.setText("cancelRequest");
                                mrequest.setEnabled(true);
                                req_state="req_sent";
                                mDecline_Request.setVisibility(View.INVISIBLE);
                                mDecline_Request.setEnabled(false);
                                Toast.makeText(profile.this, "Request sent Sucessful", Toast.LENGTH_LONG).show();
                            }


                        }


                    });
                }

                //---------------CANCEL REQUEST --------
                if(req_state.equals("req_sent")) {
                    progress = new ProgressDialog(profile.this);
                    //uprogress.setTitle("updating");
                    progress.setMessage("please Wait ");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    Map cancelreqmap = new HashMap();
                    cancelreqmap.put("Request/" + user.getUid() + "/" + user_id, null);
                    cancelreqmap.put("Request/" + user_id + "/" + user.getUid(), null);
                    prootref.updateChildren(cancelreqmap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                progress.hide();
                                mrequest.setEnabled(true);
                                req_state = "notfriend";
                                mrequest.setText("SendRequest");
                                mDecline_Request.setVisibility(View.INVISIBLE);
                                mDecline_Request.setEnabled(false);
                            } else {
                                String error2 = error.getMessage();
                                progress.dismiss();
                                Toast.makeText(profile.this, error2, Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                //---------recieved-------
              if(req_state.equals("req_recieved"))
              {
              final      String currentdate = DateFormat.getDateTimeInstance().format(new Date());
                  progress = new ProgressDialog(profile.this);
                  //uprogress.setTitle("updating");
                  progress.setMessage("please Wait ");
                  progress.show();
                  progress.setCanceledOnTouchOutside(false);
              Map friendmap =new HashMap();
              friendmap.put("Friends/"+user.getUid()+"/"+user_id+"/date",currentdate);
              friendmap.put("Friends/"+user_id+"/"+user.getUid()+"/date",currentdate);
              friendmap.put("Request/"+user.getUid()+"/"+user_id,null);
              friendmap.put("Request/"+user_id+"/"+user.getUid(),null);
              prootref.updateChildren(friendmap, new DatabaseReference.CompletionListener() {
                  @Override
                  public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                      if(error==null)
                      {
                          progress.hide();
                          mrequest.setEnabled(true);
                          req_state="friend";
                          Toast.makeText(profile.this,"Added to your friends List",Toast.LENGTH_LONG).show();
                          mrequest.setText("UnFriend");
                          mDecline_Request.setVisibility(View.INVISIBLE);
                          mDecline_Request.setEnabled(false);
                      }
                      else
                      {
                          String error2 = error.getMessage();
                          progress.dismiss();
                          Toast.makeText(profile.this,error2,Toast.LENGTH_LONG).show();
                      }
                  }
              });
                }
                //---------unFriend-------
                if(req_state.equals("friend"))
                {
                    progress = new ProgressDialog(profile.this);
                    //uprogress.setTitle("updating");
                    progress.setMessage("please Wait ");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    Map unfriendmap = new  HashMap();
                    unfriendmap.put("Friends/"+user.getUid()+"/"+user_id,null);
                    unfriendmap.put("Friends/"+user_id+"/"+user.getUid(),null);
                    prootref.updateChildren(unfriendmap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error==null)
                            {progress.hide();
                                mrequest.setEnabled(true);
                                req_state="notfriend";
                                mrequest.setText("SendRequest");
                                mDecline_Request.setVisibility(View.INVISIBLE);
                                mDecline_Request.setEnabled(false);
                            }
                            else
                            {progress.dismiss();
                                String error2 = error.getMessage();
                                Toast.makeText(profile.this,error2,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });



    }

}