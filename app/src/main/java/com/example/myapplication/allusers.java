package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class allusers extends AppCompatActivity {
    private RecyclerView mallusers;
    private DatabaseReference muserdatabase;
    private FirebaseRecyclerOptions<model> options;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<model, MyviewHoler> adapter;
    private ProgressDialog uprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);
        mallusers = findViewById(R.id.users_recycle);
        mallusers.setLayoutManager(new LinearLayoutManager(this));
        mallusers.setHasFixedSize(true);
        mAuth=FirebaseAuth.getInstance();
        String cur_uesr = mAuth.getUid();
        uprogress = new ProgressDialog(allusers.this);
       //uprogress.setTitle("updating");
        uprogress.setMessage("please Wait ");
        uprogress.show();
        uprogress.setCanceledOnTouchOutside(false);

        muserdatabase = FirebaseDatabase.getInstance().getReference().child("users");
//muserdatabase.keepSynced(true);
        LoadData();
    }

private void LoadData()
{
        options = new FirebaseRecyclerOptions.Builder<model>().setQuery(muserdatabase, model.class).build();
        adapter=new FirebaseRecyclerAdapter<model, MyviewHoler>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyviewHoler holder, int position, @NonNull model model) {
              // Picasso.with(allusers.this).load(model.getImage()).placeholder(R.drawable.profile).into(holder.msingle_user_dp);
                Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.msingle_user_dp);
                holder.msingle_user_name.setText(model.getName());
              //  holder.msingle_user_status.setText(""+Userdata.getStatus());
                holder.msingle_user_status.setText(model.getStatus());
                final String user_id =getRef(position).getKey();
                uprogress.dismiss();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(allusers.this,profile.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });


            }

            @NonNull
            @Override
            public MyviewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleuser,parent,false);
                return new MyviewHoler(view);
            }
        };

        adapter.startListening();
        mallusers.setAdapter(adapter);
    }
}