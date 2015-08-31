package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.Renderer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.RendererInterfaces.MediaListener;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.RendererInterfaces.RendererListener;


/**
 * Created by jfowler on 8/27/15.
 */
public abstract class AdaptiveMedia extends Media implements RendererListener{

    public static final int VIDEO_BUFFER_SEGMENTS = 200;
    public static final int AUDIO_BUFFER_SEGMENTS = 60;

    private MediaCodecVideoTrackRenderer videoTrackRenderer;
    private MediaCodecAudioTrackRenderer audioTrackRenderer;
    private TextTrackRenderer textTrackRenderer;
    private MediaListener mediaListener;

    public AdaptiveMedia(Context context, MediaListener mediaListener, Uri uri, String userAgent) {
        super(context, uri, userAgent);
        this.mediaListener = mediaListener;
    }

    protected abstract void prepareRender();

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public final TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        TrackRenderer[] renderers = new TrackRenderer[3];
        renderers[Renderer.TYPE_VIDEO] = videoTrackRenderer;
        renderers[Renderer.TYPE_AUDIO] = audioTrackRenderer;
        renderers[Renderer.TYPE_TEXT] = textTrackRenderer;

        return renderers;
    }

    @Override
    public void onPrepared(TrackRenderer[] renderers) {
        videoTrackRenderer = (MediaCodecVideoTrackRenderer)renderers[Renderer.TYPE_VIDEO];
        audioTrackRenderer = (MediaCodecAudioTrackRenderer)renderers[Renderer.TYPE_AUDIO];
        textTrackRenderer = (TextTrackRenderer)renderers[Renderer.TYPE_TEXT];
        mediaListener.mediaPrepared(this);
    }
}
