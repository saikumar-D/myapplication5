package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
private EditText fpass,femail;
private Button fbtn;
private ProgressDialog fprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fpass=findViewById(R.id.log_pass);
        femail=findViewById(R.id.log_email);
        fbtn=findViewById(R.id.log_sub);
        mAuth = FirebaseAuth.getInstance();
    //    fprogress=findViewById(R.id.log_progress);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String mail = femail.getText().toString();
                String pass = fpass.getText().toString();

                fprogress = new ProgressDialog(login.this);
                fprogress.setTitle("Logining");
                fprogress.setMessage("please Wait While working");
                fprogress.show();
                fprogress.setCanceledOnTouchOutside(false);
                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)) {
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                fprogress.dismiss();
                                Intent home_Inent = new Intent(login.this, Navigatoin.class);
                                startActivity(home_Inent);
                                finish();
                            } else {
                                String error = "";
                                try {

                                    throw task.getException();


                                } catch (FirebaseAuthInvalidUserException e) {
                                    error = "InvalidEmail";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    error = "Invalidpassword";
                                    // e.printStackTrace();
                                } catch (Exception e) {
                                    error = "Default error";
                                    e.printStackTrace();
                                }
                                // fprogress.setVisibility(view.GONE);
                                Toast.makeText(login.this, "Enter correct details", Toast.LENGTH_LONG).show();
                                fprogress.dismiss();
                            }


                        }


                    });
                }
                else
                {
                    Toast.makeText(login.this,"RE CHECK",Toast.LENGTH_LONG).show();
                }

            }

        });

     }
    }
