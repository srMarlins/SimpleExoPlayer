package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.util.Util;

/**
 * Created by jfowler on 8/27/15.
 */
public abstract class Media {
    public static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    public static final int BUFFER_SEGMENT_COUNT = 160;

    private String userAgent;
    private Uri uri;
    private Context context;

    public Media(Context context, Uri uri, String userAgent){
        this.context = context;
        this.uri = uri;
        this.userAgent = userAgent;
    }

    public Uri getUri(){
        return this.uri;
    }

    public void setUri(Uri uri){
        if(uri != null && uri.getPath() != null) {
            this.uri = uri;
        }else{
            throw new IllegalArgumentException("You must pass in a Uri containing a valid path");
        }
    }

    public Context getContext(){
        return this.context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public String getUserAgent(){
        return this.userAgent;
    }

    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }

    public abstract TrackRenderer[] buildRenderer(Context context);
    public abstract TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount);
}
