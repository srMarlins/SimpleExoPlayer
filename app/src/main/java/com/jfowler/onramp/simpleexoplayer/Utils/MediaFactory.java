package com.jfowler.onramp.simpleexoplayer.utils;

import android.content.Context;
import android.net.Uri;

import com.jfowler.onramp.simpleexoplayer.MediaModels.AudioMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.DashMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.HlsMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.MediaModels.SmoothStreamingMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.VideoMedia;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.MediaListener;

/**
 * Created by jfowler on 9/2/15.
 */
public class MediaFactory {

    public static final String MEDIA_NAME_TAG = "mediaName";
    public static final String MEDIA_URI_TAG = "mediaUri";
    public static final String MEDIA_TYPE_TAG = "mediaType";
    public static final String STREAM_TYPE_TAG = "streamType";

    public static final int STREAM_TYPE_DASH = 0;
    public static final int STREAM_TYPE_SS = 1;
    public static final int STREAM_TYPE_HLS = 2;
    public static final int STREAM_TYPE_STANDARD = 3;

    public static final int MEDIA_TYPE_VIDEO = 4;
    public static final int MEDIA_TYPE_AUDIO = 5;


    public static Media getAdaptiveMedia(Context context, MediaListener mediaListener, String uri, String userAgent, int streamType, int mediaType){
        Media media = null;

        switch(streamType){
            case STREAM_TYPE_DASH: media = new DashMedia(context, mediaListener, Uri.parse(uri), userAgent); break;
            case STREAM_TYPE_SS: media = new SmoothStreamingMedia(context, mediaListener, Uri.parse(uri), userAgent); break;
            case STREAM_TYPE_HLS: media = new HlsMedia(context, mediaListener, Uri.parse(uri), userAgent); break;
            case STREAM_TYPE_STANDARD: media = getStandardMedia(context, uri, userAgent, mediaType); break;
        }

        return media;
    }

    public static Media getStandardMedia(Context context, String uri, String userAgent, int mediaType){
        Media media = null;

        switch (mediaType){
            case MEDIA_TYPE_VIDEO: media = new VideoMedia(context, Uri.parse(uri), userAgent); break;
            case MEDIA_TYPE_AUDIO: media = new AudioMedia(context, Uri.parse(uri), userAgent); break;
        }

        return media;
    }

    public static int stringToInt(String type){
        switch (type.toLowerCase()){
            case "dash": return STREAM_TYPE_DASH;
            case "smoothstreaming": return STREAM_TYPE_SS;
            case "hls": return STREAM_TYPE_HLS;
            case "standard media": return STREAM_TYPE_STANDARD;
            case "video": return MEDIA_TYPE_VIDEO;
            case "audio": return MEDIA_TYPE_AUDIO;
        }
        return -1;
    }

    public static String intToString(int type){
        switch (type){
            case STREAM_TYPE_DASH: return "DASH";
            case STREAM_TYPE_SS: return "SmoothStreaming";
            case STREAM_TYPE_HLS: return "HLS";
            case STREAM_TYPE_STANDARD: return "Standard Media";
            case MEDIA_TYPE_VIDEO: return "Video";
            case MEDIA_TYPE_AUDIO: return "Audio";
        }
        return null;
    }

    public static boolean isAdaptiveMedia(int type){
        boolean isAdaptive = false;
        switch (type){
            case STREAM_TYPE_DASH:
            case STREAM_TYPE_SS:
            case STREAM_TYPE_HLS: isAdaptive = true; break;
        }
        return isAdaptive;
    }
}
