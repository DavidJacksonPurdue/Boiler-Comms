package com.example.boiler_commslogin.directMessage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.viewComments;

import java.util.ArrayList;

public class allDMAdapter extends RecyclerView.Adapter{
    String userID;
    String userName;
    String password;
    ArrayList<String> dmIDs;
    ArrayList<String> usernames;
    ArrayList<String> bodys;
    ArrayList<String> times;
    ArrayList<String> otherUIDs;
    Context context;

    public allDMAdapter(Context context, ArrayList<String> dmIDs, ArrayList<String> usernames, ArrayList<String> bodys, ArrayList<String> times, ArrayList<String> ohterUIDs, String userID, String userName, String password){
        this.context = context;
        this.userID = userID;
        this.dmIDs = dmIDs;
        this.usernames = usernames;
        this.bodys = bodys;
        this.times = times;
        this.otherUIDs = ohterUIDs;
        this.userName = userName;
        this.password = password;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.new_dm_row_layout, parent, false);
        return new allDMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((allDMViewHolder)holder).theirUsername.setText(usernames.get(position));
        ((allDMViewHolder)holder).dmBody.setText(bodys.get(position));
        ((allDMViewHolder)holder).time.setText(times.get(position));
        ((allDMViewHolder)holder).dmID = dmIDs.get(position);
        ((allDMViewHolder)holder).uid2 = otherUIDs.get(position);
        ((allDMViewHolder)holder).uid1 = userID;
        ((allDMViewHolder)holder).username = userName;
        ((allDMViewHolder)holder).password = password;
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }


    public class allDMViewHolder extends RecyclerView.ViewHolder{
        TextView theirUsername;
        TextView dmBody;
        TextView time;
        String dmID;
        String uid1;
        String uid2;
        String username;
        String password;
        public allDMViewHolder(@NonNull View itemView) {
            super(itemView);
            theirUsername = itemView.findViewById(R.id.username_textview_new_message);
            dmBody = itemView.findViewById(R.id.body_textview_new_message);
            time = itemView.findViewById(R.id.time_textview_new_message);
            dmBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(context, viewDM.class);
                    myIntent.putExtra("USERID1", uid1);
                    myIntent.putExtra("DM_ID", dmID);
                    myIntent.putExtra("USERID2",uid2);
                    myIntent.putExtra("USERNAME", username);
                    myIntent.putExtra("PASSWORD", password);
                    //myIntent.putExtra("key", value); //Optional parameters
                    context.startActivity(myIntent);
                }
            });
        }
    }
}



