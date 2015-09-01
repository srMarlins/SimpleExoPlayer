package com.jfowler.onramp.simpleexoplayer.Renderers;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.RendererListener;

/**
 * Created by jfowler on 8/31/15.
 */
public abstract class Renderer{

    protected static final int LIVE_EDGE_LATENCY_MS = 30000;
    protected static final int TEXT_BUFFER_SEGMENTS = 2;
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_METADATA = 3;

    protected Media media;
    protected BandwidthMeter bandwidthMeter;
    protected ManifestFetcher manifestFetcher;
    protected LoadControl loadControl;
    protected DefaultUriDataSource manifestDataSource;
    protected Handler handler;
    protected MediaCodecVideoTrackRenderer videoTrackRenderer;
    protected MediaCodecAudioTrackRenderer audioTrackRenderer;
    protected TextTrackRenderer textTrackRenderer;
    protected RendererListener rendererListener;

    public Renderer(Media media){
        this.media = media;
        this.handler = new Handler();
        this.loadControl = new DefaultLoadControl(new DefaultAllocator(Media.BUFFER_SEGMENT_SIZE));
        this.bandwidthMeter = new DefaultBandwidthMeter();
        this.manifestDataSource = new DefaultUriDataSource(this.media.getContext(), this.media.getUserAgent());
        this.rendererListener = (RendererListener) media;
    }

    public MediaCodecAudioTrackRenderer getAudioTrackRenderer(){
        return audioTrackRenderer;
    }

    public MediaCodecVideoTrackRenderer getVideoTrackRenderer(){
        return videoTrackRenderer;
    }

    public abstract void prepareRender(Context context, RendererListener rendererListener);
}
