package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeContainer);



        listView = findViewById(R.id.listView);// here i need to populate the list view the parse users that we get from the server.
        listView.setOnItemClickListener(SocialMediaActivity.this);
        // This will automatically update based on the data from the array adapter.
        // here we need to ged the all users if not, objects from the server.
        arrayList = new ArrayList(); // to take a array list
        arrayAdapter = new ArrayAdapter(SocialMediaActivity.this,android.R.layout.simple_list_item_1,arrayList); // let the list view we have an array adapter.



        try {

            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()); // we do not need the current user from the query as he is already the user.

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) { // users is a list.

                    if(e==null){
                        if(users.size()>0){

                            for(ParseUser user: users){

                                arrayList.add(user.getUsername()); // in order to populate our list view.
                                // We need to have an Array adapter and this will update the list view. otherwise, we can not interact with the list view directly.
                            }

                            listView.setAdapter(arrayAdapter);
                            listView.setVisibility(View.VISIBLE);


                        }// else {} there are no current users...
                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()); // we do not need the current user from the query as he is already the user.

                    parseQuery.whereNotContainedIn("username",arrayList);// we do not need to get users that are already in the array list.

                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) { // users is a list.

                            if(users.size()>0){
                                if(e==null){

                                    for(ParseUser user: users){

                                        arrayList.add(user.getUsername()); // in order to populate our list view.
                                        // We need to have an Array adapter and this will update the list view. otherwise, we can not interact with the list view directly.
                                    }

                                    arrayAdapter.notifyDataSetChanged();// this will notify the list view that data is changed and
                                    // new data is added to the array list. just go ahead and update the list view and show the updated data to the user.

                                    if(swipeRefreshLayout.isRefreshing()){// this means trying to refresh..
                                        swipeRefreshLayout.setRefreshing(false); // we do not want the continue refreshing..
                                    }


                                }
                            }else{
                                if(swipeRefreshLayout.isRefreshing()){// this means trying to refresh..
                                    swipeRefreshLayout.setRefreshing(false); // we do not want the continue refreshing..
                                }
                            }

                        }
                    });

                }  catch (Exception e){
                    e.printStackTrace();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutUserItem) {

            FancyToast.makeText(SocialMediaActivity.this,  ParseUser.getCurrentUser().getUsername()+ " is logged out successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    // Social media activity will be eliminated from the stack.

                    if(e==null){

                        Intent intent = new Intent(SocialMediaActivity.this, MainActivity.class); // this must be put in oder to move in to the next activity window...
                        startActivity(intent);
                        finish();

                    }

                }
            });


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SocialMediaActivity.this,ChatFuction.class);
                intent.putExtra("selectedUser",arrayList.get(position));
                startActivity(intent);




    }
}