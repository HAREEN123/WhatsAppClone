package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatFuction extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList; // all messages will be assigned to this array list..
    private ArrayAdapter adapter;
    private String selectedUser;// as we are going to take the user name from the users list..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_fuction);

        selectedUser = getIntent().getStringExtra("selectedUser");// the person we clicked on the list view..
        FancyToast.makeText(this,  "Chat with " + selectedUser+" Now..!!!", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

        findViewById(R.id.btnSend).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList ();
        adapter =  new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(adapter);

        try {
            ParseQuery<ParseObject>firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject>secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender",ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient",selectedUser);

            secondUserChatQuery.whereEqualTo("waSender",selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient",ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);// or means accepts.....
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if(objects.size()>0 && e==null){
                        for (ParseObject chatObject : objects){
                            String waMessage = chatObject.get("waMessage") + "";

                            if(chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())){
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }

                            if(chatObject.get("waSender").equals(selectedUser)){
                                waMessage = selectedUser + ": " + waMessage;
                            }

                            chatsList.add(waMessage);

                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        final EditText edtMessage = findViewById(R.id.edtSend);

        ParseObject chat = new ParseObject("Chat");// we are going to have a new class in the parse dashboard.
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());// parse file means our image.
        chat.put("waTargetRecipient", selectedUser);// this is a new column to the class naming the Description.
        chat.put("waMessage",edtMessage.getText().toString());

        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    FancyToast.makeText(ChatFuction.this, "Message From " + ParseUser.getCurrentUser().getUsername() + " sent to " + selectedUser, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": "+ edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();// this method is used to update the list view. letting the list view know that there are some new data.
                    edtMessage.setText("");

                }

            }
        });




    } // this is our functionality to send messages
}