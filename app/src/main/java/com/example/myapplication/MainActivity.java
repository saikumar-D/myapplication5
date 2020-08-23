package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
private EditText mname,memail,mpassword;
private Button mbtn;
private ProgressBar mprogress;
private Button mlog;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog logprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mname=findViewById(R.id.main_name);
        memail=findViewById(R.id.main_email);
        mpassword=findViewById(R.id.main_pass);
        mbtn=findViewById(R.id.main_btn);
        mprogress=findViewById(R.id.main_progress);
        mAuth = FirebaseAuth.getInstance();
        mlog=findViewById(R.id.main_log);
        mlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              senttologin();

            }
            public void senttologin()
            {
                Intent logintent =new Intent(MainActivity.this,login.class);
                //  logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logintent);
                finish();
                //startActivity(new Intent(getApplicationContext(),Navigation.class));
            }
        });


        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //@Override
                final String email = memail.getText().toString();
                final String password = mpassword.getText().toString();
                final String name = mname.getText().toString();
                mprogress.setVisibility(view.VISIBLE);
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(name)){

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();
                            mdatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            HashMap<String, String> usermap = new HashMap();
                            usermap.put("name", name);
                            usermap.put("status", "Hi eveyOne");
                            usermap.put("image", "default");
                            usermap.put("thumb_image", "default");
                            mdatabase.setValue(usermap);
                            Intent reg_Intent;
                            reg_Intent = new Intent(MainActivity.this, Navigatoin.class);
                            startActivity(reg_Intent);
                            finish();

                        } else {
                            mprogress.setVisibility(view.GONE);
                            Toast.makeText(MainActivity.this, "not valid", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
                else
                {
                    Toast.makeText(MainActivity.this,"Recheck nd type",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}