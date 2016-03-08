package com.chatapp.fovi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cris on 08/01/2016.
 */
public class InboxFragment extends ListFragment {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar spinner;
    List<ParseObject> mMessages;
    ParseObject message;
    String messageType;
    ParseFile file;
    Uri fileUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_inbox_fragment, container, false);

        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveMessages();
    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENTS_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                spinner.setVisibility(View.INVISIBLE);

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (e == null) {
                    mMessages = parseObjects;
                    String[] usernames = new String[parseObjects.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }
                    MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                            setListAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        message = mMessages.get(position);
        messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        file = message.getParseFile(ParseConstants.KEY_FILE);
        fileUri = Uri.parse(file.getUrl());

        if(messageType.equals(ParseConstants.TYPE_IMAGE)){
            Intent i = new Intent(getActivity(), ViewImageActivity.class);
            i.setData(fileUri);
            startActivity(i);
        }else{
            Intent i = new Intent(Intent.ACTION_VIEW, fileUri);
            i.setDataAndType(fileUri, "video/*");
            startActivity(i);
        }

        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENTS_IDS);
        ArrayList<String> idsToRemove = new ArrayList<>();

        if(ids.size() > 1){
            ids.remove(ParseUser.getCurrentUser().getObjectId());
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(ParseConstants.KEY_RECIPIENTS_IDS, idsToRemove);
            message.saveInBackground();
        }else{
            message.deleteInBackground();
        }

    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveMessages();
        }
    };
}


