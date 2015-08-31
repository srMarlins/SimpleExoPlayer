package com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers;

import android.content.Context;

import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Renderers.RendererInterfaces.RendererListener;

import java.io.IOException;

/**
 * Created by jfowler on 8/28/15.
 */
public class HlsRenderer extends Renderer implements ManifestFetcher.ManifestCallback<HlsMasterPlaylist>{
    public HlsRenderer(Media media) {
        super(media);
        this.manifestFetcher.singleLoad(handler.getLooper(), this);
    }

    @Override
    public void prepareRender(Context context, RendererListener rendererListener) {

    }

    @Override
    public void onSingleManifest(HlsMasterPlaylist hlsMasterPlaylist) {

    }

    @Override
    public void onSingleManifestError(IOException e) {

    }
}
