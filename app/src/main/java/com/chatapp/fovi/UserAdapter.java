package com.chatapp.fovi;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by 21438547 on 27/01/2016.
 */
public class UserAdapter extends ArrayAdapter<ParseUser>{

    private static final String TAG = UserAdapter.class.getSimpleName();
    Context mContext;
    List<ParseUser> mUser;

    public UserAdapter(Context context, List<ParseUser> users){
        super(context,R.layout.message_item, users);
        mContext = context;
        mUser = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseUser user = mUser.get(position);
        String email = user.getEmail().toLowerCase();

        if(email.equals("")){
            holder.userImageView.setImageResource(R.drawable.avatar_empty);
        }else{
            String hash = MD5Util.md5Hex(email);
            String gravatarUrl = "http://www.gravatar.com/avatar/"+ hash + "?s=204&d=404";
            //Log.d(TAG, gravatarUrl);
            Picasso.with(mContext)
                    .load(gravatarUrl)
                    .placeholder(R.drawable.avatar_empty)
                    .into(holder.userImageView);
        }

        /*if(user.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)){
            holder.iconImageView.setImageResource(R.drawable.ic_picture);
        }else{
            holder.iconImageView.setImageResource(R.drawable.ic_video);
        }*/
        holder.nameLabel.setText(user.getUsername());

        return convertView;
    }

    public static class ViewHolder{
        ImageView userImageView;
        TextView nameLabel;
    }

    public void refill(List<ParseUser> users){
        mUser.clear();
        mUser.addAll(users);
        notifyDataSetChanged();
    }


}
