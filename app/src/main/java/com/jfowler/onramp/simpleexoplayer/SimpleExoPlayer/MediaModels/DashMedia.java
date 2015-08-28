package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.chunk.MultiTrackChunkSource;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.Representation;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;
import com.google.android.exoplayer.dash.mpd.UtcTimingElementResolver;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;
import com.jfowler.onramp.simpleexoplayer.MainActivity;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.DashRenderer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.MediaListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfowler on 8/27/15.
 */
public class DashMedia extends AdaptiveMedia{

    private DashRenderer dashRenderer;

    public DashMedia(Context context, MediaListener listener, Uri uri){
        super(context, listener, uri);
        dashRenderer = new DashRenderer(this);
    }

    @Override
    protected void prepareRender() {
        dashRenderer.prepareRender(getContext(), this);
    }

}
