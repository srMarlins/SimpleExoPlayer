package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers;

import com.google.android.exoplayer.TrackRenderer;

/**
 * Created by jfowler on 8/28/15.
 */
public interface RendererListener {
    void onPrepared(TrackRenderer[] renderers);
}
