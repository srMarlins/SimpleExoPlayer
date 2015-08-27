package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;

/**
 * Created by jfowler on 8/27/15.
 */
public class DashMedia extends AdaptiveMedia {

    @Override
    public MediaCodecVideoTrackRenderer buildVideoTrackRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        return null;
    }

    @Override
    protected MediaCodecAudioTrackRenderer buildAudioRender(Context context, int bufferSegmentSize, int bufferSegmentCount){
        // Build the audio renderer.
        //TODO - Add fixed dash streaming here
       /* DataSource audioDataSource = new DefaultUriDataSource(context, bandwidthMeter, getUri().getPath());
        ChunkSource audioChunkSource = new DashChunkSource(manifestFetcher, audioAdaptationSetIndex,
                null, audioDataSource, new FormatEvaluator.FixedEvaluator(), LIVE_EDGE_LATENCY_MS, elapsedRealtimeOffset, null,
                null);
        ChunkSampleSource audioSampleSource = new ChunkSampleSource(audioChunkSource,
                loadControl, bufferSegmentSize * bufferSegmentCount);
        return new MediaCodecAudioTrackRenderer(audioSampleSource);*/
    }
}
