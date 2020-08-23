package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_status extends AppCompatActivity {
    private EditText meditstatus;
    private Button meditstatusbtn;
    private DatabaseReference statusdatabase;
    private FirebaseUser statususer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        meditstatus=findViewById(R.id.edit_status);
        meditstatusbtn=findViewById(R.id.editstatusbtn);
        statususer= FirebaseAuth.getInstance().getCurrentUser();
      //  statusdatabase.keepSynced(true);
       String status_id = statususer.getUid();
     //  String curr_status = meditstatus.getText().toString();
        statusdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(status_id);

        meditstatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curr_status = meditstatus.getText().toString();

           statusdatabase.child("status").setValue(curr_status).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(change_status.this,"successful",Toast.LENGTH_LONG).show();
                      // Intent backIntent = new Intent(change_status.this,settings.class);
                       //startActivity(backIntent);
                   }
                   else
                   {
                       Toast.makeText(change_status.this,"not successful",Toast.LENGTH_LONG).show();
                   }
               }
           });
            }
        });


    }
}