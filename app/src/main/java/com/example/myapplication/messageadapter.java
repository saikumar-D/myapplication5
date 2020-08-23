package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageadapter extends  RecyclerView.Adapter<messageadapter.messageViewHolder> {

    private List<message> mmessagelist;
    private FirebaseAuth nauth;
    public messageadapter(List<message> mmessagelist)
    {
        this.mmessagelist=mmessagelist;

    }
    public messageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single,parent,false);
        return new messageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        nauth=FirebaseAuth.getInstance();
        String current_user=nauth.getCurrentUser().getUid();
        message c =mmessagelist.get(position);

      String from_user=c.getFrom();
      if(from_user.equals(current_user))
      {
          holder.messagetext.setBackgroundColor(Color.WHITE);
          holder.messagetext.setTextColor(Color.BLACK);
      }else
      {
          holder.messagetext.setBackgroundResource(R.drawable.message_text_background);
          holder.messagetext.setTextColor(Color.WHITE);
      }
      holder.messagetext.setText(c.getMessage());
    }

    @Override
    public int getItemCount() {
        return mmessagelist.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder{
        public TextView messagetext;
        public CircleImageView mchat_dp;
        public messageViewHolder(View view)
        {
            super(view);
            messagetext=view.findViewById(R.id.conv1);
            mchat_dp=view.findViewById(R.id.msg_dp);

        }
    }
}
