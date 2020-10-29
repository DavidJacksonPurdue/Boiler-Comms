package com.example.boiler_commslogin.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
    ArrayList<String> username, topic, title, body, image, time, votecount;
    Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemSelected(int position, View view, Object object);
    }

    public void setListener(OnItemClickListener l) {
        this.listener = l;
    }

    public MyAdapter(Context context, ArrayList<String> username, ArrayList<String> topic, ArrayList<String> title, ArrayList<String> body,
                     ArrayList<String> image, ArrayList<String> time, ArrayList<String> votecount) {
        this.context = context;
        this.username = username;
        this.topic = topic;
        this.body = body;
        this.time = time;
        this.title = title;
        this.image = image;
        this.votecount = votecount;
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
        String img = image.get(position).toString();
        holder.votecount.setText(votecount.get(position).toString());
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

        TextView time, title, body, username, topic, votecount;
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

            final int upvote_id = 0;
            final int downvote_id = 1;

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(upvote_id, v, null);
                    }
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(downvote_id, v, null);
                    }
                }
            });

        }
    }
}