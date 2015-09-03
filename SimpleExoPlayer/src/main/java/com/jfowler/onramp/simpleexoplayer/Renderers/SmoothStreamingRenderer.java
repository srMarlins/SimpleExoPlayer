package com.jfowler.onramp.simpleexoplayer.Renderers;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Handler;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.chunk.MultiTrackChunkSource;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingChunkSource;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingManifest;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingManifestParser;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.text.ttml.TtmlParser;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;
import com.jfowler.onramp.simpleexoplayer.MediaModels.AdaptiveMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.RendererListener;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jfowler on 8/28/15.
 */
public class SmoothStreamingRenderer extends Renderer implements ManifestFetcher.ManifestCallback<SmoothStreamingManifest> {

    private SmoothStreamingManifest manifest;

    public SmoothStreamingRenderer(Media media) {
        super(media);
        String url = Util.toLowerInvariant(this.media.getUri().toString()).endsWith("/manifest") ? this.media.getUri().toString() : this.media.getUri().toString() + "/Manifest";
        this.manifestFetcher = new ManifestFetcher<>(url, manifestDataSource, new SmoothStreamingManifestParser());
        this.manifestFetcher.singleLoad(handler.getLooper(), this);
    }

    @Override
    public void prepareRender(Context context, RendererListener rendererListener) {
        this.rendererListener = rendererListener;
        Handler mainHandler = handler;

        if(manifest.protectionElement != null){
            throw new IllegalStateException("SimpleExoPlayer does not support DRM protected content");
        }

        // Obtain stream elements for playback.
        int audioStreamElementCount = 0;
        int textStreamElementCount = 0;
        int videoStreamElementIndex = -1;
        for (int i = 0; i < manifest.streamElements.length; i++) {
            if (manifest.streamElements[i].type == SmoothStreamingManifest.StreamElement.TYPE_AUDIO) {
                audioStreamElementCount++;
            } else if (manifest.streamElements[i].type == SmoothStreamingManifest.StreamElement.TYPE_TEXT) {
                textStreamElementCount++;
            } else if (videoStreamElementIndex == -1
                    && manifest.streamElements[i].type == SmoothStreamingManifest.StreamElement.TYPE_VIDEO) {
                videoStreamElementIndex = i;
            }
        }

        // Determine which video tracks we should use for playback.
        int[] videoTrackIndices = null;
        if (videoStreamElementIndex != -1) {
            try {
                videoTrackIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(context,
                        Arrays.asList(manifest.streamElements[videoStreamElementIndex].tracks), null, false);
            } catch (MediaCodecUtil.DecoderQueryException e) {
                e.printStackTrace();
                return;
            }
        }

        // Build the video renderer.
        if (videoTrackIndices == null || videoTrackIndices.length == 0) {
            this.videoTrackRenderer = null;
        } else {
            DataSource videoDataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
            ChunkSource videoChunkSource = new SmoothStreamingChunkSource(manifestFetcher,
                    videoStreamElementIndex, videoTrackIndices, videoDataSource,
                    new FormatEvaluator.AdaptiveEvaluator(bandwidthMeter), LIVE_EDGE_LATENCY_MS);
            ChunkSampleSource videoSampleSource = new ChunkSampleSource(videoChunkSource, loadControl,
                    AdaptiveMedia.VIDEO_BUFFER_SEGMENTS * Media.BUFFER_SEGMENT_SIZE, mainHandler, null,
                    TYPE_VIDEO);
            this.videoTrackRenderer = new MediaCodecVideoTrackRenderer(videoSampleSource, null, true,
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, null, mainHandler, null, 50);
        }

        // Build the audio renderer.
        final String[] audioTrackNames;
        final MultiTrackChunkSource audioChunkSource;
        if (audioStreamElementCount == 0) {
            audioTrackNames = null;
            audioChunkSource = null;
            this.audioTrackRenderer = null;
        } else {
            audioTrackNames = new String[audioStreamElementCount];
            ChunkSource[] audioChunkSources = new ChunkSource[audioStreamElementCount];
            DataSource audioDataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
            FormatEvaluator audioFormatEvaluator = new FormatEvaluator.FixedEvaluator();
            audioStreamElementCount = 0;
            for (int i = 0; i < manifest.streamElements.length; i++) {
                if (manifest.streamElements[i].type == TYPE_AUDIO) {
                    audioTrackNames[audioStreamElementCount] = manifest.streamElements[i].name;
                    audioChunkSources[audioStreamElementCount] = new SmoothStreamingChunkSource(
                            manifestFetcher, i, new int[] {0}, audioDataSource, audioFormatEvaluator,
                            LIVE_EDGE_LATENCY_MS);
                    audioStreamElementCount++;
                }
            }
            audioChunkSource = new MultiTrackChunkSource(audioChunkSources);
            ChunkSampleSource audioSampleSource = new ChunkSampleSource(audioChunkSource, loadControl,
                    AdaptiveMedia.AUDIO_BUFFER_SEGMENTS * Media.BUFFER_SEGMENT_SIZE, mainHandler, null,
                    TYPE_AUDIO);
            this.audioTrackRenderer = new MediaCodecAudioTrackRenderer(audioSampleSource, null, true,
                    mainHandler, null);
        }

        // Build the text renderer.
        final String[] textTrackNames;
        final MultiTrackChunkSource textChunkSource;
        if (textStreamElementCount == 0) {
            textTrackNames = null;
            textChunkSource = null;
            this.textTrackRenderer = null;
        } else {
            textTrackNames = new String[textStreamElementCount];
            ChunkSource[] textChunkSources = new ChunkSource[textStreamElementCount];
            DataSource ttmlDataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
            FormatEvaluator ttmlFormatEvaluator = new FormatEvaluator.FixedEvaluator();
            textStreamElementCount = 0;
            for (int i = 0; i < manifest.streamElements.length; i++) {
                if (manifest.streamElements[i].type == SmoothStreamingManifest.StreamElement.TYPE_TEXT) {
                    textTrackNames[textStreamElementCount] = manifest.streamElements[i].language;
                    textChunkSources[textStreamElementCount] = new SmoothStreamingChunkSource(
                            manifestFetcher, i, new int[] {0}, ttmlDataSource, ttmlFormatEvaluator,
                            LIVE_EDGE_LATENCY_MS);
                    textStreamElementCount++;
                }
            }
            textChunkSource = new MultiTrackChunkSource(textChunkSources);
            ChunkSampleSource ttmlSampleSource = new ChunkSampleSource(textChunkSource, loadControl,
                    TEXT_BUFFER_SEGMENTS * Media.BUFFER_SEGMENT_SIZE, mainHandler, null,
                    TYPE_TEXT);
            this.textTrackRenderer = new TextTrackRenderer(ttmlSampleSource, null, mainHandler.getLooper(),
                    new TtmlParser());
        }

    }

    @Override
    public void onSingleManifest(SmoothStreamingManifest smoothStreamingManifest) {
        this.manifest = smoothStreamingManifest;
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

