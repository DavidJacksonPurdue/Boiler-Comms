package com.example.boiler_commslogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.data.MyAdapter;

import java.util.ArrayList;

public class MyUserCommentsAdapter extends RecyclerView.Adapter<MyUserCommentsAdapter.MyUserCommentsViewHolder> {
    ArrayList<String> commentIDs;
    ArrayList<String> postIDs;
    ArrayList<String> parentCommentIDs;
    ArrayList<String> bodys;
    ArrayList<String> userNames;
    ArrayList<String> userIDs;
    ArrayList<String> dates;
    ArrayList<String> titles;
    ArrayList<String> topics;
    Context context;

    public MyUserCommentsAdapter(Context context, ArrayList<String> commentIDs, ArrayList<String> postIDs, ArrayList<String> parentCommentIDs, ArrayList<String> bodys, ArrayList<String> userNames, ArrayList<String> userIDs, ArrayList<String> dates, ArrayList<String> titles, ArrayList<String> topics){
        this.context = context;
        this.commentIDs = commentIDs;
        this.postIDs = postIDs;
        this.parentCommentIDs = parentCommentIDs;
        this.bodys = bodys;
        this.userNames = userNames;
        this.userIDs = userIDs;
        this.dates = dates;
        this.titles = titles;
        this.topics = topics;
    }

    @NonNull
    @Override
    public MyUserCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_layout, parent, false);
        return new MyUserCommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserCommentsViewHolder holder, int position) {
        holder.postName.setText(titles.get(position));
        holder.topicName.setText(topics.get(position));
        holder.userName.setText(userNames.get(position));
        holder.dateOfComment.setText(dates.get(position));
        holder.body.setText(bodys.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class MyUserCommentsViewHolder extends RecyclerView.ViewHolder{
        TextView postName, topicName, userName, dateOfComment, body;
        public MyUserCommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            postName = itemView.findViewById(R.id.postName);
            topicName = itemView.findViewById(R.id.topicName);
            userName = itemView.findViewById(R.id.userName5);
            dateOfComment = itemView.findViewById(R.id.dateofComment);
            body = itemView.findViewById(R.id.commentBody);
        }
    }
}

