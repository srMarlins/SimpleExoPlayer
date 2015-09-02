package com.jfowler.onramp.simpleexoplayer;

import android.content.Context;
import android.view.Surface;
import android.widget.MediaController;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.util.PlayerControl;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.Renderers.Renderer;

/**
 * Created by jfowler on 8/27/15.
 */
public class SimpleExoPlayer {

    private static int NUM_RENDERERS = 3;

    private ExoPlayer exoPlayer;
    private MediaController.MediaPlayerControl mediaPlayerControl;

    public SimpleExoPlayer(){
        setupMediaPlayer();
    }

    public SimpleExoPlayer(ExoPlayer exoPlayer){
        this.exoPlayer = exoPlayer;
        setupMediaPlayer();
    }

    public void playMedia(Context context, ExoPlayer.Listener listener, Surface surface, Media... media){
        setPlayerListener(listener);
        for(Media m : media) {
            TrackRenderer[] renders = m.buildRenderer(context);
            if(surface != null){
                setVideoSurface(surface, (MediaCodecVideoTrackRenderer) renders[Renderer.TYPE_VIDEO]);
                exoPlayer.prepare(renders[0], renders[1]);
            }else {
                if(renders.length > 1){
                    exoPlayer.prepare(renders[1]);
                }else {
                    exoPlayer.prepare(renders);
                }
            }
        }
        mediaPlayerControl.start();
    }

    public void setVideoSurface(Surface surface, MediaCodecVideoTrackRenderer videoRenderer){
        pushSurface(false, surface, videoRenderer);
    }

    private void pushSurface(boolean blockForSurfacePush, Surface surface, MediaCodecVideoTrackRenderer videoRenderer) {
        if (videoRenderer == null) {
            return;
        }

        if (blockForSurfacePush) {
            exoPlayer.blockingSendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        } else {
            exoPlayer.sendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        }
    }

    private void setPlayerListener(ExoPlayer.Listener listener){
        if(listener == null){
            listener = new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean b, int i) {

                }

                @Override
                public void onPlayWhenReadyCommitted() {

                }

                @Override
                public void onPlayerError(ExoPlaybackException e) {
                    reinstantiateExoPlayer();
                }
            };
        }
        exoPlayer.removeListener(listener);
        exoPlayer.addListener(listener);
    }

    public void pauseMedia(){
        if(mediaPlayerControl.canPause()) {
            mediaPlayerControl.pause();
        }
    }

    public void stopMedia(){
        if(mediaPlayerControl.canPause()){
            mediaPlayerControl.pause();
            mediaPlayerControl.seekTo(0);
        }
        exoPlayer.stop();
    }

    public boolean isPlaying() {
        boolean isPlaying = false;
        if(exoPlayer != null) {
            if (mediaPlayerControl == null) {
                mediaPlayerControl = new PlayerControl(exoPlayer);
            }
            isPlaying = mediaPlayerControl.isPlaying();
        }
        return isPlaying;
    }

    public MediaController.MediaPlayerControl getMediaPlayerControl(){
        return mediaPlayerControl;
    }

    public void reinstantiateExoPlayer(){
        destroyMediaPlayer();
        setupMediaPlayer();
    }

    private void setupMediaPlayer() {
        if(exoPlayer == null){
            exoPlayer = ExoPlayer.Factory.newInstance(NUM_RENDERERS);
        }
        exoPlayer.stop();
        mediaPlayerControl = new PlayerControl(exoPlayer);
    }

    private void destroyMediaPlayer() {
        if (exoPlayer != null) {
            mediaPlayerControl.pause();
            mediaPlayerControl = null;
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
