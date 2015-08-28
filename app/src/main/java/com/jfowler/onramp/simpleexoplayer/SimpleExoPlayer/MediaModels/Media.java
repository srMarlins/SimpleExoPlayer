package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer.TrackRenderer;

/**
 * Created by jfowler on 8/27/15.
 */
public abstract class Media {
    public static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    public static final int BUFFER_SEGMENT_COUNT = 160;

    private Uri uri;
    private Context context;

    public Media(Uri uri){
        this.uri = uri;
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

    public abstract TrackRenderer[] buildRenderer(Context context);
    public abstract TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount);
}
