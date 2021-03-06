package com.jfowler.onramp.simpleexoplayer.MediaModels;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.jfowler.onramp.simpleexoplayer.Renderers.Renderer;

/**
 * Created by jfowler on 8/27/15.
 */
public class VideoMedia extends Media {

    public VideoMedia(Context context, Uri uri, String userAgent) {
        super(context, uri, userAgent);
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        TrackRenderer[] array = new TrackRenderer[2];
        Allocator allocator = new DefaultAllocator(bufferSegmentSize);
        DataSource dataSource = new DefaultUriDataSource(context, getUserAgent());
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                getUri(), dataSource, allocator, bufferSegmentSize * bufferSegmentCount);
        array[Renderer.TYPE_VIDEO] = new MediaCodecVideoTrackRenderer(
                sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        array[Renderer.TYPE_AUDIO] = new MediaCodecAudioTrackRenderer(sampleSource);

        return array;
    }
}
