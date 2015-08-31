package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.RendererInterfaces;

import android.content.Context;

/**
 * Created by jfowler on 8/28/15.
 */
public interface Renderer {
    void prepareRender(Context context, RendererListener rendererListener);
}
