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
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.MediaListener;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.RendererListener;

import java.io.IOException;


/**
 * Created by jfowler on 8/27/15.
 */
public abstract class AdaptiveMedia extends Media implements RendererListener{

    protected MediaCodecVideoTrackRenderer videoTrackRenderer;
    protected MediaCodecAudioTrackRenderer audioTrackRenderer;
    protected MediaListener mediaListener;

    public AdaptiveMedia(Context context, MediaListener mediaListener, Uri uri) {
        super(context, uri);
        this.mediaListener = mediaListener;
    }

    protected abstract void prepareRender();

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public final TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        TrackRenderer[] renderers = new TrackRenderer[2];
        renderers[0] = audioTrackRenderer;
        renderers[1] = videoTrackRenderer;
        return renderers;
    }

    @Override
    public void onPrepared(TrackRenderer[] renderers) {
        audioTrackRenderer = (MediaCodecAudioTrackRenderer)renderers[0];
        videoTrackRenderer = (MediaCodecVideoTrackRenderer)renderers[1];
        mediaListener.mediaPrepared(this);
    }
}
