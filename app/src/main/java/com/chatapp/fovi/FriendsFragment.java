package com.chatapp.fovi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Cris on 08/01/2016.
 */
public class FriendsFragment extends Fragment {

    ProgressBar spinner;
    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_friends_fragment, container, false);

        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mGridView = (GridView)rootView.findViewById(R.id.friendGrid);

        TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyText);

        return rootView;

    }


    @Override
    public void onResume(){
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();

        query.orderByAscending(ParseConstants.KEY_USERNAME);

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> users, ParseException e) {
                spinner.setVisibility(View.VISIBLE);
                if (e == null) {
                    mUsers = users;
                    String[] usernames = new String[users.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {

                        usernames[i] = user.getUsername();
                        i++;
                    }
                    if(mGridView.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(), mUsers);
                        mGridView.setAdapter(adapter);
                    }else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
                    }
                    spinner.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("ERROR", e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.editFriendsErrorTitle)
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        });

    }

}

