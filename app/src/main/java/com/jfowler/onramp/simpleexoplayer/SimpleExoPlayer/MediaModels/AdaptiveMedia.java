package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.ManifestFetcher;

import java.util.jar.Manifest;

/**
 * Created by jfowler on 8/27/15.
 */
public abstract class AdaptiveMedia extends Media {

    protected ManifestFetcher manifestFetcher;
    protected BandwidthMeter bandwidthMeter;

    public AdaptiveMedia(Uri uri, ManifestFetcher manifestFetcher, BandwidthMeter bandwidthMeter, FormatEvaluator formatEvaluator) {
        super(uri);
        this.manifestFetcher = manifestFetcher;
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public final TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        TrackRenderer[] array = new TrackRenderer[2];
        array[0] = buildAudioTrackRenderer(context, bufferSegmentSize, bufferSegmentCount);
        array[1] = buildVideoTrackRenderer(context, bufferSegmentSize, bufferSegmentCount);
        return array;
    }

    protected abstract MediaCodecAudioTrackRenderer buildAudioTrackRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount);
    protected abstract MediaCodecVideoTrackRenderer buildVideoTrackRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount);
}
