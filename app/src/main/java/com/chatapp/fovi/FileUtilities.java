package com.chatapp.fovi;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 21438547 on 15/01/2016.
 */
public class FileUtilities {

    static final int MEDIA_TYPE_IMAGE = 4;
    static final int MEDIA_TYPE_VIDEO =5;

    public static Uri getOutputMediaFileUri(int mediaType){

        if(isExternalStorageAvailable()){

            String appname = "FoBi";
            File mediaStorageDir = null;

            switch(mediaType){
                case MEDIA_TYPE_IMAGE:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appname);
                    break;
                case MEDIA_TYPE_VIDEO:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), appname);
                    break;
            }

            if (!mediaStorageDir.exists()) {

                if (!mediaStorageDir.mkdirs()) {
                    Log.e("ERROR", "No se ha podido crear el directorio");
                    return null;
                }
            }

            File mediaFile;
            Date now = new Date ();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType==MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path+ "IMG_" + timestamp + ".jpg");
            } else if(mediaType==MEDIA_TYPE_VIDEO){
                mediaFile = new File(path+ "VID_" + timestamp + ".mp4");
            } else {
                return null;
            }
            return Uri.fromFile(mediaFile);
        }else {
            return null;
        }
    }
    private static boolean isExternalStorageAvailable() {

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;
    }
}





