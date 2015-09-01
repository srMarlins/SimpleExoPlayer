package com.jfowler.onramp.simpleexoplayer.Renderers;

import android.content.Context;
import android.media.MediaCodec;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.RendererListener;

import java.io.IOException;

/**
 * Created by jfowler on 8/28/15.
 */
public class HlsRenderer extends Renderer implements ManifestFetcher.ManifestCallback<HlsMasterPlaylist>{

    private static final int BUFFER_SEGMENT_SIZE = 256 * 1024;
    private static final int BUFFER_SEGMENTS = 64;

    private HlsMasterPlaylist manifest;

    public HlsRenderer(Media media) {
        super(media);
        HlsPlaylistParser parser = new HlsPlaylistParser();
        this.manifestFetcher = new ManifestFetcher(media.getUri().toString(), manifestDataSource, parser);
        this.manifestFetcher.singleLoad(handler.getLooper(), this);
    }

    @Override
    public void prepareRender(Context context, RendererListener rendererListener) {
        this.rendererListener = rendererListener;

        int[] variantIndices = null;

        try {
            variantIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(
                    context, manifest.variants, null, false);
        } catch (MediaCodecUtil.DecoderQueryException e) {
            e.printStackTrace();
            return;
        }
        if (variantIndices.length == 0) {
            throw new IllegalStateException("No variants selected.");
        }


        DataSource dataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
        HlsChunkSource chunkSource = new HlsChunkSource(dataSource, media.getUri().toString(), manifest, bandwidthMeter,
                variantIndices, HlsChunkSource.ADAPTIVE_MODE_SPLICE, null);
        HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl,
                BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE, this.handler, null, TYPE_VIDEO);
        this.videoTrackRenderer = new MediaCodecVideoTrackRenderer(sampleSource,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, this.handler, null, 50);
        this.audioTrackRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
    }


    @Override
    public void onSingleManifest(HlsMasterPlaylist hlsMasterPlaylist) {
        this.manifest = hlsMasterPlaylist;
        prepareRender(media.getContext(), rendererListener);
        TrackRenderer[] array = new TrackRenderer[3];
        array[TYPE_VIDEO] = videoTrackRenderer;
        array[TYPE_AUDIO] = audioTrackRenderer;
        array[TYPE_TEXT] = textTrackRenderer;
        rendererListener.onPrepared(array);
    }

    @Override
    public void onSingleManifestError(IOException e) {

    }
}
