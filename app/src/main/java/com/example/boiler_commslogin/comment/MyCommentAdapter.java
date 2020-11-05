package com.example.boiler_commslogin.comment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boiler_commslogin.comment.Item;
import com.example.boiler_commslogin.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MyCommentAdapter extends MultiLevelAdapter {

    private Holder mViewHolder;
    private Context mContext;
    private List<com.example.boiler_commslogin.comment.Item> mListItems = new ArrayList<>();
    private com.example.boiler_commslogin.comment.Item mItem;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;

    MyCommentAdapter(Context mContext, List<com.example.boiler_commslogin.comment.Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView) {
        super(mListItems);
        this.mListItems = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        // set the icon based on the current state
        expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mViewHolder = (Holder) holder;
        mItem = mListItems.get(position);
        Log.d("m_item", Integer.toString(mItem.getCommentID()));
        switch (getItemViewType(position)) {
            case 1:
                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
                break;
            default:
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
        mViewHolder.mTitle.setText(mItem.getTitle());
        mViewHolder.mSubtitle.setText(mItem.getBody());
        mViewHolder.mTitle.setTag((Object)(Integer.toString(mItem.getParentID()) + "," + Integer.toString(mItem.getCommentID())));
        mViewHolder.mItem = mItem;
        if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
            setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
            mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mExpandButton.setVisibility(View.GONE);
        }

        Log.e("MuditLog",mItem.getLevel()+" "+mItem.getPosition()+" "+mItem.isExpanded()+"");

        // indent child items
        // Note: the parent item should start at zero to have no indentation
        // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
        float density = mContext.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView mTitle, mSubtitle;
        ImageView mExpandIcon;
        Item mItem;
        LinearLayout mTextBox, mExpandButton;
        Button mReplyButton;
        EditText mReply;

        Holder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mSubtitle = (TextView) itemView.findViewById(R.id.subtitle);
            mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
            mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
            mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);
            mReplyButton = (Button) itemView.findViewById(R.id.replyCommentButton);
            mReply = (EditText) itemView.findViewById(R.id.replyEditText);
            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });*/

            //set click listener on LinearLayout because the click area is bigger than the ImageView
            mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set click event on expand button here
                    Log.d("adapterPosittion", Integer.toString(getAdapterPosition()));
                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                    // rotate the icon based on the current state
                    // but only here because otherwise we'd see the animation on expanded items too while scrolling
                    mExpandIcon.animate().rotation(mListItems.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();
                    Toast.makeText(mContext, "EXPAND!!!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), mItem.isExpanded()), Toast.LENGTH_SHORT).show();
                }
            });

            mReplyButton.setOnClickListener(new View.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v){
                        //mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());

                        String text = mReply.getText().toString();
                        if(!text.equals("")) {
                            hideKeyboard(((Activity) mContext));
                            int newParentID = Integer.parseInt(mTitle.getTag().toString().split(",")[1]);
                            String textBody = mReply.getText().toString();
                            Log.d("madeIT", textBody);
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String commentTime = dtf.format(now);
                            String str_result = null;
                            try {
                                str_result= (String)new sendComment(mContext).execute(Integer.toString(mItem.getPostID()),newParentID, textBody, ((Activity) mContext).getIntent().getStringExtra("USERID"), commentTime).get(2000, TimeUnit.MILLISECONDS);

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                            Log.d("str_result",str_result);
                            mReply.setText("");
                            /*Item newComment = new Item(mItem.getLevel() + 1);
                            newComment.setCommentID(Integer.parseInt(str_result));
                            newComment.setParentID(newParentID);
                            newComment.setParentID(0);
                            newComment.setBody(textBody);
                            newComment.setTitle("you posted:");

                            mMultiLevelRecyclerView.openTill(mItem.getLevel());
                            notifyDataSetChanged();*/
                            Intent myIntent = new Intent(mContext, viewComments.class);
                            myIntent.putExtra("USERID", ((Activity) mContext).getIntent().getStringExtra("USERID"));
                            myIntent.putExtra("USERNAME", ((Activity) mContext).getIntent().getStringExtra("USERNAME"));
                            myIntent.putExtra("PASSWORD", ((Activity) mContext).getIntent().getStringExtra("PASSWORD"));
                            myIntent.putExtra("POSTID", Integer.toString(mItem.getPostID()));
                            Log.d("mItem", "" + mItem.getPostID());
                            //myIntent.putExtra("key", value); //Optional parameters
                            mContext.startActivity(myIntent);
                        }
                    }
            });
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
