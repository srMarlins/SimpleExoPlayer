package com.jfowler.onramp.simpleexoplayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.MediaListener;
import com.jfowler.onramp.simpleexoplayer.Renderers.SmoothStreamingRenderer;


/**
 * Created by jfowler on 8/31/15.
 */
public class SmoothStreamingMedia extends AdaptiveMedia {
    private SmoothStreamingRenderer smoothRenderer;

    public SmoothStreamingMedia(Context context, MediaListener listener, Uri uri, String userAgent){
        super(context, listener, uri, userAgent);
        smoothRenderer = new SmoothStreamingRenderer(this);
    }

    @Override
    protected void prepareRender() {
        smoothRenderer.prepareRender(getContext(), this);
    }
}
