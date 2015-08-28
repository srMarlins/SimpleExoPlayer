package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers;

import android.content.Context;

/**
 * Created by jfowler on 8/28/15.
 */
public interface Renderer {
    public void prepareRender(Context context, RendererListener rendererListener);
}
