package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyviewHoler extends RecyclerView.ViewHolder {

    TextView msingle_user_name,msingle_user_status;
     CircleImageView msingle_user_dp;
    public MyviewHoler(@NonNull View itemView) {

        super(itemView);

        msingle_user_name=itemView.findViewById(R.id.single_user_name);
        msingle_user_dp=itemView.findViewById(R.id.single_user_dp);
        msingle_user_status=itemView.findViewById(R.id.single_user_status);

    }
}
