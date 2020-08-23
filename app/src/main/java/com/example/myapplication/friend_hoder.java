package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class friend_hoder extends RecyclerView.ViewHolder {
    TextView mfriends_name, mfriends_status;
    CircleImageView mfriends_dp;

    public friend_hoder(@NonNull View itemView) {
        super(itemView);


        mfriends_dp=itemView.findViewById(R.id.friends_dp);
        mfriends_name=itemView.findViewById(R.id.friends_name);
        mfriends_status=itemView.findViewById(R.id.friends_status);
    }
    }
