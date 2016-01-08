package com.chatapp.fovi;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Cris on 16/12/2015.
 */
public class FoviApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

    }
}
