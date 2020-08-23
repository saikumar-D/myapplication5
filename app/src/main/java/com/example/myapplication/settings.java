package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class settings extends AppCompatActivity {
    private FirebaseUser curr_user;
    private DatabaseReference muserdata;
    private StorageReference mStorageRef;

    private TextView dname;
    private CircleImageView uimage;
    private TextView ustatus;
    private Button chgeimg, chgestatus;
    private static final int GALLARY_PICK = 1;
    private ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        dname = findViewById(R.id.profile_name);
        uimage = findViewById(R.id.profile_image);
        ustatus = findViewById(R.id.profile_status);
        chgeimg = findViewById(R.id.profile_change_image);
        chgestatus = findViewById(R.id.profile_change_status);
        mStorageRef = FirebaseStorage.getInstance().getReference();

       mprogress = new ProgressDialog(settings.this);
        //progress.setTitle("updating");
        mprogress.setMessage("please Wait");
        mprogress.show();
        mprogress.setCanceledOnTouchOutside(false);

        String curr_uid = curr_user.getUid();

        muserdata = FirebaseDatabase.getInstance().getReference().child("users").child(curr_uid);
      muserdata.keepSynced(true);
        muserdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                final String image = snapshot.child("image").getValue().toString();
                String thumb_image = snapshot.child("thumb_image").getValue().toString();
                dname.setText(name);
                ustatus.setText(status);
                mprogress.dismiss();
                if(!image.equals("default")) {
                   // Picasso.with(settings.this).load(image).placeholder(R.drawable.profile).into(uimage);
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile).into(uimage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(uimage);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chgestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusintent = new Intent(settings.this, change_status.class);
                startActivity(statusintent);
            }
        });

        // Intent gallaryintent;
        chgeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallaryintent = new Intent();
                gallaryintent.setType("image/*");
                gallaryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallaryintent, "SELECT IMAGE"), GALLARY_PICK);
            }

        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    mprogress= new ProgressDialog(settings.this);
                    mprogress.setTitle("changing image");
                    mprogress.setMessage("please Wait");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    final Uri resultUri = result.getUri();

                    final File thumb_Filepath = new File(resultUri.getPath());
                    String dp_id =curr_user.getUid();
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)

                            .compressToBitmap(thumb_Filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    final byte[] thumb_byte = baos.toByteArray();

                    final StorageReference filepath = mStorageRef.child("profile_images").child(dp_id+".jpg");
                    final StorageReference thumbsto_filepath = mStorageRef.child("profile_images").child("thumbs").child(dp_id+".jpg");
                    filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                               final String download_Url = resultUri.toString();
                               UploadTask uploadTask=thumbsto_filepath.putBytes(thumb_byte);
                               uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                       final String thumb_url = thumb_task.getResult().getStorage().getDownloadUrl().toString();
                                       if (thumb_task.isSuccessful()) {
                                           Map update_map = new HashMap();
                                           update_map.put("image",download_Url);
                                           update_map.put("thumb_image", thumb_url);



                                           muserdata.updateChildren(update_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       Toast.makeText(settings.this, "updated Sucessfully", Toast.LENGTH_LONG).show();
                                                       mprogress.dismiss();
                                                   }

                                               }


                                           });
                                       } else {
                                           Toast.makeText(settings.this, " not working", Toast.LENGTH_LONG).show();
                                           mprogress.dismiss();
                                       }
                                   }

                               });
                            }
                            else{
                                Toast.makeText(settings.this,"Not sucessful",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }



        }
    }







