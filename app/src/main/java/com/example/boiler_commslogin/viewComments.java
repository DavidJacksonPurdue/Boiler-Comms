package com.example.boiler_commslogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.example.boiler_commslogin.data.model.LoadUserCredentials;
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
        List<Item> commentsAsItems = new ArrayList<>();
        for(int x = 0; x <nList.getLength(); x++){
            Element commentElement = (Element)(nList.item(x));
            Item currentComment = new Item(x);
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


        List<Item> itemList = (List<Item>) recursivlyPopulateComments(commentsAsItems, -1, 0);

        MyCommentAdapter myCommentAdapter = new MyCommentAdapter(this, itemList, multiLevelRecyclerView);

        multiLevelRecyclerView.setAdapter(myCommentAdapter);


        //If you want to already opened Multi-RecyclerView just call openTill where is parameter is
        // position to corresponding each level.
        multiLevelRecyclerView.openTill(0);
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

    private List<?> recursivlyPopulateComments(List<Item> commentsList, int goalID, int level) {
        List<RecyclerViewItem> itemList = new ArrayList<>();
        for(int x = 0; x < commentsList.size(); x++){
            Item item = new Item(level);
            item.setTitle(String.format(Locale.ENGLISH, commentsList.get(x).getUserName(), x));
            item.setBody(String.format(Locale.ENGLISH, commentsList.get(x).getBody(), x));

            if(commentsList.get(x).getParentID() == goalID){
                item.addChildren((List<RecyclerViewItem>) recursivlyPopulateComments(commentsList, commentsList.remove(x).getCommentID(), level + 1));
                x--;
                itemList.add(item);
            }

        }
        return itemList;
    }







    private List<?> recursivePopulateFakeData(int levelNumber, int depth) {
        List<RecyclerViewItem> itemList = new ArrayList<>();

        String title;
        switch (levelNumber) {
            case 1:
                title = "PQRST %d";
                break;
            case 2:
                title = "XYZ %d";
                break;
            default:
                title = "ABCDE %d";
                break;
        }

        for (int i = 0; i < depth; i++) {
            Item item = new Item(levelNumber);
            item.setTitle(String.format(Locale.ENGLISH, title, i));
            item.setBody(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if (depth % 2 == 0) {//add child based on recursion
                item.addChildren((List<RecyclerViewItem>) recursivePopulateFakeData(levelNumber + 1, depth / 2));
            }
            itemList.add(item);//add at same level
        }

        return itemList;
    }
}