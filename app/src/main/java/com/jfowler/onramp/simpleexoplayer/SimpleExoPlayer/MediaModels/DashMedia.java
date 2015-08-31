package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels;

import android.content.Context;
import android.net.Uri;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.DashRenderer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.MediaListener;

/**
 * Created by jfowler on 8/27/15.
 */
public class DashMedia extends AdaptiveMedia{

    private DashRenderer dashRenderer;

    public DashMedia(Context context, MediaListener listener, Uri uri, String userAgent){
        super(context, listener, uri, userAgent);
        dashRenderer = new DashRenderer(this);
    }

    @Override
    protected void prepareRender() {
        dashRenderer.prepareRender(getContext(), this);
    }

}
