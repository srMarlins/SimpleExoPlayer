package com.jfowler.onramp.simpleexoplayer.MediaModels;

import android.content.Context;
import android.net.Uri;

import com.jfowler.onramp.simpleexoplayer.Renderers.HlsRenderer;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.MediaListener;


/**
 * Created by jfowler on 9/1/15.
 */
public class HlsMedia extends AdaptiveMedia {

    private HlsRenderer hlsRenderer;

    public HlsMedia(Context context, MediaListener mediaListener, Uri uri, String userAgent) {
        super(context, mediaListener, uri, userAgent);
        hlsRenderer = new HlsRenderer(this);
    }

    @Override
    protected void prepareRender() {
        hlsRenderer.prepareRender(getContext(), this);
    }
}
