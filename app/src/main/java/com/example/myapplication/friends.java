package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class friends extends AppCompatActivity {
    private DatabaseReference frddatabase;
    private DatabaseReference muserdatabase;
    private FirebaseAuth mdatabase;
    private RecyclerView frecycler;
    private FirebaseRecyclerOptions<friend_model> foptions;
    private FirebaseRecyclerAdapter<friend_model, friend_hoder> fadapter;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        frecycler = findViewById(R.id.con_recycle);
        frecycler.setLayoutManager(new LinearLayoutManager(this));
        frecycler.setHasFixedSize(true);
        mdatabase= FirebaseAuth.getInstance();
        String cur = mdatabase.getUid();
        frddatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(cur);
        muserdatabase=FirebaseDatabase.getInstance().getReference().child("users");
        progress = new ProgressDialog(friends.this);
        //progress.setTitle("updating");
        progress.setMessage("please Wait");
        progress.show();
        progress.setCanceledOnTouchOutside(false);
        frienddata();

    }
    private void frienddata() {

        foptions=new FirebaseRecyclerOptions.Builder<friend_model>().setQuery(frddatabase,friend_model.class).build();
        fadapter= new FirebaseRecyclerAdapter<friend_model, friend_hoder>(foptions) {
            @Override
            protected void onBindViewHolder(@NonNull final friend_hoder holder, int position, @NonNull final friend_model model) {
               // holder.mfriends_status.setText(model.getDate());
                final String list_frd = getRef(position).getKey();
                muserdatabase.child(list_frd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String frd_nane =snapshot.child("name").getValue().toString();
                        String frd_dp =snapshot.child("image").getValue().toString();
                        String frd = snapshot.child("status").getValue().toString();
                        holder.mfriends_status.setText(frd);
                        holder.mfriends_name.setText(frd_nane);
                        Picasso.get().load(frd_dp).placeholder(R.drawable.profile).into(holder.mfriends_dp);
                      //  Picasso.get().load(frd_dp).into(holder.mfriends_dp);
                        progress.hide();
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[] {"chat","profile"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(friends.this);
                                builder.setTitle("select");
                                // builder.show();
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0)
                                        {progress.show();
                                            Intent chatIn =new Intent(friends.this,chat.class);
                                            chatIn.putExtra("user_id",list_frd);
                                            startActivity(chatIn);
                                        }
                                        else if(i==1)
                                        {progress.show();
                                            Intent proIn =new Intent(friends.this,profile.class);
                                            proIn.putExtra("user_id",list_frd);
                                            proIn.putExtra("user_name",frd_nane);
                                            startActivity(proIn);
                                        }
                                    }

                                });
                                builder.show();
                            }

                        });

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(friends.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @NonNull
            @Override
            public friend_hoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends,parent,false);
                return new friend_hoder(vi);
            }
        };
        fadapter.startListening();
        frecycler.setAdapter(fadapter);
    }
}