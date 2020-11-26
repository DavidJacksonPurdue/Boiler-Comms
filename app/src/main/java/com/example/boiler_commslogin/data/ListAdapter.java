package com.example.boiler_commslogin.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.viewpost.ViewPostActivity;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    ArrayList<String> username, userId;
    Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemSelected(int position, View view, ArrayList<Object> object);
    }

    public void setListener(OnItemClickListener l) {
        this.listener = l;
    }

    public ListAdapter(Context context, ArrayList<String> username, ArrayList<String> userId) {
        this.username = username;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.username.setText(username.get(position));
        holder.userId.setText(userId.get(position));
    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, userId;
        Button unfollow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_list);
            userId = itemView.findViewById(R.id.user_id_list);
            unfollow = itemView.findViewById(R.id.unfollow);

            final int username_id = 0;
            final int unfollow_id = 1;

            unfollow.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        ArrayList<Object> unfollow_list = new ArrayList<>();
                        unfollow_list.add(userId.getText().toString());
                        listener.onItemSelected(unfollow_id, v, unfollow_list);
                    }
                }
            }));

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        ArrayList<Object> user = new ArrayList<>();
                        user.add(userId.getText().toString());
                        listener.onItemSelected(username_id, v, user);
                    }
                }
            });
        }
    }
}