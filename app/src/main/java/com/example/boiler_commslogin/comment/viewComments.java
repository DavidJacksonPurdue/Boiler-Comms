package com.example.boiler_commslogin.comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.boiler_commslogin.R;
import com.example.boiler_commslogin.comment.Item;
//import com.example.boiler_commslogin.comment.R;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Boolean.FALSE;

public class viewComments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MultiLevelRecyclerView multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String str_result = null;
        try {
            str_result= (String)new loadComments(this).execute("0").get(2000, TimeUnit.MILLISECONDS);;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Document postXML = null;
        try {
            postXML = loadXMLFromString(str_result);
        }catch(Exception e){
            e.printStackTrace();
        }
        postXML.getDocumentElement().normalize();
        NodeList nList = postXML.getElementsByTagName("comment");
        List<com.example.boiler_commslogin.comment.Item> commentsAsItems = new ArrayList<>();
        for(int x = 0; x <nList.getLength(); x++){
            Element commentElement = (Element)(nList.item(x));
            com.example.boiler_commslogin.comment.Item currentComment = new com.example.boiler_commslogin.comment.Item(x);
            currentComment.setCommentID(Integer.parseInt(commentElement.getAttribute("commentID")));
            currentComment.setPostID(Integer.parseInt(commentElement.getAttribute("postID")));
            if(!(commentElement.getAttribute("parentCommentID").equals(""))) {
                currentComment.setParentID(Integer.parseInt(commentElement.getAttribute("parentCommentID")));
            }
            currentComment.setBody(commentElement.getAttribute("commentText"));
            currentComment.setUserName(commentElement.getAttribute("userName"));
            currentComment.setUserID(Integer.parseInt(commentElement.getAttribute("userID")));
            currentComment.setDate(commentElement.getAttribute("dateSubmitted"));
            commentsAsItems.add(currentComment);
        }


        List<com.example.boiler_commslogin.comment.Item> itemList = (List<com.example.boiler_commslogin.comment.Item>) recursivlyPopulateComments(commentsAsItems, -1, 0);

        MyCommentAdapter myCommentAdapter = new MyCommentAdapter(this, itemList, multiLevelRecyclerView);

        multiLevelRecyclerView.setAdapter(myCommentAdapter);


        //If you want to already opened Multi-RecyclerView just call openTill where is parameter is
        // position to corresponding each level.
        multiLevelRecyclerView.setToggleItemOnClick(FALSE);
        multiLevelRecyclerView.openTill(2,1,0,0);
    }


    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    //depth y axis (parent comments)
    //levelNumber xaxis Child comments

    private List<?> recursivlyPopulateComments(List<com.example.boiler_commslogin.comment.Item> commentsList, int goalID, int level) {
        List<RecyclerViewItem> itemList = new ArrayList<>();
        for(int x = 0; x < commentsList.size(); x++){
            com.example.boiler_commslogin.comment.Item item = new Item(level);
            item.setTitle(String.format(Locale.ENGLISH, commentsList.get(x).getUserName(), x));
            item.setBody(String.format(Locale.ENGLISH, commentsList.get(x).getBody(), x));
            item.setCommentID(commentsList.get(x).getCommentID());
            item.setParentID(commentsList.get(x).getParentID());
            if(commentsList.get(x).getParentID() == goalID){
                item.addChildren((List<RecyclerViewItem>) recursivlyPopulateComments(commentsList, commentsList.remove(x).getCommentID(), level + 1));
                x--;
                itemList.add(item);
            }

        }
        return itemList;
    }


}