package com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces;

import com.google.android.exoplayer.TrackRenderer;

/**
 * Created by jfowler on 8/28/15.
 */
public interface RendererListener {
    void onPrepared(TrackRenderer[] renderers);
}
