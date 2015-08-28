package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;

import java.io.IOException;


/**
 * Created by jfowler on 8/27/15.
 */
public abstract class AdaptiveMedia extends Media implements ManifestFetcher.ManifestCallback {

    protected MediaCodecVideoTrackRenderer videoTrackRenderer;
    protected MediaCodecAudioTrackRenderer audioTrackRenderer;

    public AdaptiveMedia(Uri uri) {
        super(uri);
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public final TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        initResources(context);
        TrackRenderer[] array = new TrackRenderer[2];
        array[0] = audioTrackRenderer;
        array[1] = videoTrackRenderer;
        return array;
    }

    public void initResources(Context context){
        handler = new Handler();
        loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
        bandwidthMeter = new DefaultBandwidthMeter();
        MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
        manifestDataSource = new DefaultUriDataSource(context, getUri().getPath());
        manifestFetcher = new ManifestFetcher<>(getUri().getPath(), manifestDataSource, parser);
        manifestFetcher.singleLoad(handler.getLooper(), this);
    }


    @Override
    public void onSingleManifest(Object o) {
        if(o instanceof MediaPresentationDescription) {
            this.manifest = (MediaPresentationDescription) o;
        }else{
            throw new IllegalArgumentException("onSingleManifest is not passing a MediaPresentationDescription");
        }
    }

    @Override
    public void onSingleManifestError(IOException e) {
        e.printStackTrace();
    }
}
