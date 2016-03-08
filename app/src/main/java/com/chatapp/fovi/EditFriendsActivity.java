package com.chatapp.fovi;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
//import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class EditFriendsActivity extends ListActivity {

    private ProgressBar progressBar;
    ImageView imageAddFriends;
    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        imageAddFriends = (ImageView) findViewById(R.id.imageViewAddFriends);

        progressBar = (ProgressBar) findViewById(R.id.pbEdit);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    public void onClickAddFriends(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> users, ParseException e) {
                progressBar.setVisibility(View.VISIBLE);
                if (e == null) {

                    mUsers = users;
                    String[] usernames = new String[users.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                    addFriendsCheckmarks();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("ERROR", e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setTitle(R.string.editFriendsErrorTitle)
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        });

    }

    private void addFriendsCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null){
                    //success
                    for(int i=0; i<mUsers.size(); i++){
                        ParseUser user = mUsers.get(i);
                        for (ParseUser friend : friends){
                            if (friend.getObjectId().equals(user.getObjectId())){
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        //Toast.makeText(this, "FUNCIONAAAA!!!!",Toast.LENGTH_SHORT).show();
        if(getListView().isItemChecked(position) ){
            mFriendsRelation.add(mUsers.get(position));
        }else{
            mFriendsRelation.remove(mUsers.get(position));

        }
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("ERROR", e.getMessage());
                }

            }
        });
    }

}
