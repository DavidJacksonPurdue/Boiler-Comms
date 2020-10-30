package com.example.boiler_commslogin.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<String> username, topic, title, body, image, time, votecount, postId, topicId, userId;
    Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemSelected(int position, View view, ArrayList<Object> object);
    }

    public void setListener(OnItemClickListener l) {
        this.listener = l;
    }

    public MyAdapter(Context context, ArrayList<String> username, ArrayList<String> topic, ArrayList<String> title, ArrayList<String> body,
                     ArrayList<String> image, ArrayList<String> time, ArrayList<String> votecount, ArrayList<String> postId,
                     ArrayList<String> topicId, ArrayList<String> userId) {
        this.context = context;
        this.username = username;
        this.topic = topic;
        this.body = body;
        this.time = time;
        this.title = title;
        this.image = image;
        this.votecount = votecount;
        this.postId = postId;
        this.topicId = topicId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(username.get(position));
        holder.topic.setText(topic.get(position));
        holder.body.setText(body.get(position));
        holder.time.setText(time.get(position));
        holder.title.setText(title.get(position));
        holder.topicId.setText(topicId.get(position));
        holder.postId.setText(postId.get(position));
        holder.userId.setText(userId.get(position));
        String img = image.get(position).toString();
        if (votecount.get(position).equals("null")) {
            holder.votecount.setText("0");
        }
        else {
            holder.votecount.setText(votecount.get(position).toString());
        }
        if(!img.equals("null")) {
            String base64Image = img.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.image.setImageBitmap(decodedByte);
        }
    }

    @Override
    public int getItemCount() {
        return body.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time, title, body, username, topic, votecount, topicId, postId, userId;
        ImageView image;
        Button upvote;
        Button downvote;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            topic = itemView.findViewById(R.id.topic);
            image = itemView.findViewById(R.id.image);
            upvote = itemView.findViewById(R.id.upvote_button);
            downvote = itemView.findViewById(R.id.downvote_button);
            votecount = itemView.findViewById(R.id.vote_count);
            topicId = itemView.findViewById(R.id.topic_id_post);
            postId = itemView.findViewById(R.id.post_id_post);
            userId = itemView.findViewById(R.id.user_id_post);

            final int upvote_id = 0;
            final int downvote_id = 1;
            final int user_pos = 2;

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        ArrayList<Object> upvote = new ArrayList<>();
                        upvote.add(postId.getText().toString());
                        upvote.add(userId.getText().toString());
                        votecount.setText(Integer.toString(Integer.parseInt(votecount.getText().toString()) + 1));
                        listener.onItemSelected(upvote_id, v, upvote);
                    }
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        ArrayList<Object> downvote = new ArrayList<>();
                        downvote.add(postId.getText().toString());
                        downvote.add(userId.getText().toString());
                        votecount.setText(Integer.toString(Integer.parseInt(votecount.getText().toString()) - 1));
                        listener.onItemSelected(downvote_id, v, downvote);
                    }
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        ArrayList<Object> user = new ArrayList<>();
                        user.add(userId.getText().toString());
                        listener.onItemSelected(user_pos, v, user);
                    }
                }
            });

        }
    }
}