package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Handler;

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
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.Representation;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;
import com.google.android.exoplayer.dash.mpd.UtcTimingElementResolver;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.AdaptiveMedia;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfowler on 8/28/15.
 */
public class DashRenderer implements Renderer, ManifestFetcher.ManifestCallback, UtcTimingElementResolver.UtcTimingCallback{

    private static final int LIVE_EDGE_LATENCY_MS = 30000;

    private Media media;
    private MediaPresentationDescription manifest;
    private BandwidthMeter bandwidthMeter;
    private ManifestFetcher manifestFetcher;
    private LoadControl loadControl;
    private DefaultUriDataSource manifestDataSource;
    private Handler handler;
    private MediaCodecVideoTrackRenderer videoTrackRenderer;
    private MediaCodecAudioTrackRenderer audioTrackRenderer;
    private Long elapsedRealtimeOffset;
    private RendererListener rendererListener;

    public DashRenderer(Media media){
        this.media = media;
        this.handler = new Handler();
        this.loadControl = new DefaultLoadControl(new DefaultAllocator(Media.BUFFER_SEGMENT_SIZE));
        this.bandwidthMeter = new DefaultBandwidthMeter();
        MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
        this.manifestDataSource = new DefaultUriDataSource(this.media.getContext(), this.media.getUserAgent());
        this.manifestFetcher = new ManifestFetcher<>(this.media.getUri().toString(), manifestDataSource, parser);
        this.manifestFetcher.singleLoad(handler.getLooper(), this);
        this.elapsedRealtimeOffset = 0l;
        this.rendererListener = (RendererListener) media;
    }

    public MediaCodecAudioTrackRenderer getAudioTrackRenderer(){
        return audioTrackRenderer;
    }

    public MediaCodecVideoTrackRenderer getVideoTrackRenderer(){
        return videoTrackRenderer;
    }

    @Override
    public void prepareRender(Context context, RendererListener rendererListener) {
        this.rendererListener = rendererListener;
        Period period = manifest.periods.get(0);
        Handler mainHandler = handler;

        boolean hasContentProtection = false;
        int videoAdaptationSetIndex = period.getAdaptationSetIndex(AdaptationSet.TYPE_VIDEO);
        int audioAdaptationSetIndex = period.getAdaptationSetIndex(AdaptationSet.TYPE_AUDIO);
        AdaptationSet videoAdaptationSet = null;
        AdaptationSet audioAdaptationSet = null;
        if (videoAdaptationSetIndex != -1) {
            videoAdaptationSet = period.adaptationSets.get(videoAdaptationSetIndex);
            hasContentProtection |= videoAdaptationSet.hasContentProtection();
        }
        if (audioAdaptationSetIndex != -1) {
            audioAdaptationSet = period.adaptationSets.get(audioAdaptationSetIndex);
            hasContentProtection |= audioAdaptationSet.hasContentProtection();
        }

        // Fail if we have neither video or audio.
        if (videoAdaptationSet == null && audioAdaptationSet == null) {
            throw new IllegalStateException("No video or audio adaptation sets");
        }


        // Determine which video representations we should use for playback.
        int[] videoRepresentationIndices = null;
        if (videoAdaptationSet != null) {
            try {
                videoRepresentationIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(
                        context, videoAdaptationSet.representations, null, true);
            } catch (MediaCodecUtil.DecoderQueryException e) {
                e.printStackTrace();
            }
        }

        // Build the video renderer.
        if (videoRepresentationIndices == null || videoRepresentationIndices.length == 0) {
            videoTrackRenderer = null;
        } else {
            DataSource videoDataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
            ChunkSource videoChunkSource = new DashChunkSource(manifestFetcher,
                    videoAdaptationSetIndex, videoRepresentationIndices, videoDataSource,
                    new FormatEvaluator.AdaptiveEvaluator(bandwidthMeter), LIVE_EDGE_LATENCY_MS, elapsedRealtimeOffset,
                    mainHandler, null);
            ChunkSampleSource videoSampleSource = new ChunkSampleSource(videoChunkSource, loadControl,
                    AdaptiveMedia.VIDEO_BUFFER_SEGMENTS * Media.BUFFER_SEGMENT_SIZE, mainHandler, null,
                    0);
            videoTrackRenderer = new MediaCodecVideoTrackRenderer(videoSampleSource, null, true,
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, null, mainHandler, null, 50);
        }

        // Build the audio chunk sources.
        List<ChunkSource> audioChunkSourceList = new ArrayList<>();
        List<String> audioTrackNameList = new ArrayList<>();
        if (audioAdaptationSet != null) {
            DataSource audioDataSource = new DefaultUriDataSource(context, bandwidthMeter, media.getUserAgent());
            FormatEvaluator audioEvaluator = new FormatEvaluator.FixedEvaluator();
            List<Representation> audioRepresentations = audioAdaptationSet.representations;
            List<String> codecs = new ArrayList<>();
            for (int i = 0; i < audioRepresentations.size(); i++) {
                Format format = audioRepresentations.get(i).format;
                audioTrackNameList.add(format.id + " (" + format.numChannels + "ch, " +
                        format.audioSamplingRate + "Hz)");
                audioChunkSourceList.add(new DashChunkSource(manifestFetcher, audioAdaptationSetIndex,
                        new int[]{i}, audioDataSource, audioEvaluator, LIVE_EDGE_LATENCY_MS,
                        elapsedRealtimeOffset, mainHandler, null));
                codecs.add(format.codecs);
            }
        }

        // Build the audio renderer.
        final String[] audioTrackNames;
        final MultiTrackChunkSource audioChunkSource;
        if (audioChunkSourceList.isEmpty()) {
            audioTrackNames = null;
            audioChunkSource = null;
            audioTrackRenderer = null;
        } else {
            audioTrackNames = new String[audioTrackNameList.size()];
            audioTrackNameList.toArray(audioTrackNames);
            audioChunkSource = new MultiTrackChunkSource(audioChunkSourceList);
            SampleSource audioSampleSource = new ChunkSampleSource(audioChunkSource, loadControl,
                    AdaptiveMedia.AUDIO_BUFFER_SEGMENTS * Media.BUFFER_SEGMENT_SIZE, mainHandler, null,
                    1);
            audioTrackRenderer = new MediaCodecAudioTrackRenderer(audioSampleSource, null, true,
                    mainHandler, null);
        }

    }

    @Override
    public void onTimestampResolved(UtcTimingElement utcTimingElement, long l) {
        this.elapsedRealtimeOffset = l;
        prepareRender(media.getContext(), rendererListener);
    }

    @Override
    public void onTimestampError(UtcTimingElement utcTimingElement, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onSingleManifest(Object o) {
        if(o instanceof MediaPresentationDescription) {
            this.manifest = (MediaPresentationDescription) o;
            if (manifest.dynamic && manifest.utcTiming != null) {
                UtcTimingElementResolver.resolveTimingElement(manifestDataSource, manifest.utcTiming,
                        manifestFetcher.getManifestLoadCompleteTimestamp(), this);
            } else {
                prepareRender(media.getContext(), rendererListener);
                TrackRenderer[] array = new TrackRenderer[2];
                array[0] = audioTrackRenderer;
                array[1] = videoTrackRenderer;
                rendererListener.onPrepared(array);
            }
        }else{
            throw new IllegalArgumentException("onSingleManifest is not passing a MediaPresentationDescription");
        }
    }

    @Override
    public void onSingleManifestError(IOException e) {
        e.printStackTrace();
    }
}
