package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

/**
 * Created by jfowler on 8/27/15.
 */
public class AudioMedia extends Media {

    public AudioMedia(Context context, Uri uri, String userAgent) {
        super(context, uri, userAgent);
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context) {
        return buildRenderer(context, BUFFER_SEGMENT_SIZE, BUFFER_SEGMENT_COUNT);
    }

    @Override
    public TrackRenderer[] buildRenderer(Context context, int bufferSegmentSize, int bufferSegmentCount){
        Allocator allocator = new DefaultAllocator(bufferSegmentSize);
        DataSource dataSource = new DefaultUriDataSource(context, getUserAgent());
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                getUri(), dataSource, allocator,
                bufferSegmentSize*bufferSegmentCount);
        MediaCodecAudioTrackRenderer[] array = new MediaCodecAudioTrackRenderer[1];
        array[0] = new MediaCodecAudioTrackRenderer(sampleSource, null, true);
        return array;
    }
}
