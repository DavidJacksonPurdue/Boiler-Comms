package com.example.boiler_commslogin.directMessage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.boiler_commslogin.directMessage.specificDM.THEIR_DM;
import static com.example.boiler_commslogin.directMessage.specificDM.YOUR_DM;

public class specificDMAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<specificDM> mList;
    Context context;
    public specificDMAdapter(List list, Context context){
        this.mList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case THEIR_DM: {
                Log.d("dm_unique_identifier", "their 1");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_from_row_laoyout, parent, false);
                return new TheirDMHolder(view);
            }
            case YOUR_DM: {
                Log.d("dm_unique_identifier", "yours 1");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_to_row_layout, parent, false);
                return new YourDMHolder(view);
            }
        }
        Log.d("faillul", "you failed 1");
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        specificDM object = mList.get(position);
        return object.getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        specificDM object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case THEIR_DM: {
                    Log.d("dm_unique_identifier", "their 2");
                    ((TheirDMHolder) holder).theirBody.setText(object.getBody());
                    ((TheirDMHolder) holder).theirUserName.setText(object.getName());
                    ((TheirDMHolder) holder).theirTime.setText(object.getTime());
                    break;
                }
                case YOUR_DM: {
                    Log.d("dm_unique_identifier", "your 2");
                    ((YourDMHolder) holder).yourBody.setText(object.getBody());
                    ((YourDMHolder) holder).yourUserName.setText(object.getName());
                    ((YourDMHolder) holder).yourTime.setText(object.getTime());
                    break;
                }
            }
        }
        Log.d("faillul", "you failed 2");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TheirDMHolder extends RecyclerView.ViewHolder{
        TextView theirBody;
        TextView theirUserName;
        TextView theirTime;
        public TheirDMHolder(@NonNull View itemView) {
            super(itemView);
            theirBody = itemView.findViewById(R.id.body_textview_from_row);
            theirUserName = itemView.findViewById(R.id.username_textview_from_row);
            theirTime = itemView.findViewById(R.id.time_textview_from_row);
        }
    }
    public class YourDMHolder extends RecyclerView.ViewHolder{
        TextView yourBody;
        TextView yourUserName;
        TextView yourTime;
        public YourDMHolder(@NonNull View itemView) {
            super(itemView);
            yourBody = itemView.findViewById(R.id.body_textview_to_row);
            yourUserName = itemView.findViewById(R.id.username_textview_to_row);
            yourTime = itemView.findViewById(R.id.time_textview_to_row);
        }
    }
}




