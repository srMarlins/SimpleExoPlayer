package com.jfowler.onramp.simpleexoplayer;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.exoplayer.util.Util;
import com.jfowler.onramp.simpleexoplayer.MediaModels.AdaptiveMedia;
import com.jfowler.onramp.simpleexoplayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.Renderers.RendererInterfaces.MediaListener;
import com.jfowler.onramp.simpleexoplayer.Utils.MediaFactory;
import com.jfowler.onramp.simpleexoplayerdemo.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private static String POS_TAG = "seekPos";

    /**
     * The SimpleExoPlayer object used to handle media
     */
    private SimpleExoPlayer mSimpleExoPlayer;

    private VideoView mVideoView;

    private MediaController.MediaPlayerControl mMediaPlayerControl;

    private MediaController mMediaController;

    private int mSeekPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_video);

        if(savedInstanceState != null){
            mSeekPos = savedInstanceState.getInt(POS_TAG, 0);
        }

        if(mSimpleExoPlayer == null) {
            mSimpleExoPlayer = new SimpleExoPlayer();
        }

        mVideoView = (VideoView) findViewById(R.id.videoViewSurface);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mMediaController != null){
                    mMediaController.show();
                }
                return false;
            }
        });

        mMediaController = new MediaController(VideoPlayerActivity.this);
        mMediaPlayerControl = mSimpleExoPlayer.getMediaPlayerControl();



        //Here we retrieve the ExoPlayer's MediaPlayerControl object to control playback
        mMediaController.setMediaPlayer(mMediaPlayerControl);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);
    }

    private Media prepareMedia() throws IllegalStateException{
        Bundle extras = getIntent().getExtras();
        Media media = null;
        if(extras != null){
            String uri = extras.getString(MediaFactory.MEDIA_URI_TAG);
            final int mediaType = extras.getInt(MediaFactory.MEDIA_TYPE_TAG);
            int streamType = extras.getInt(MediaFactory.STREAM_TYPE_TAG);
            if(MediaFactory.isAdaptiveMedia(streamType)){
                media = MediaFactory.getAdaptiveMedia(this, new MediaListener() {
                    @Override
                    public void mediaPrepared(Media media) {
                        if(mediaType == MediaFactory.MEDIA_TYPE_VIDEO) {
                            mSimpleExoPlayer.playMedia(VideoPlayerActivity.this, null, mVideoView.getHolder().getSurface(), media);
                        }else{
                            mSimpleExoPlayer.playMedia(VideoPlayerActivity.this, null, null, media);
                        }

                        mMediaPlayerControl.seekTo(mSeekPos);

                        mMediaController.show();
                    }
                }, uri, Util.getUserAgent(this, getString(R.string.app_name)), streamType, mediaType);
            }
        }

        if(media == null){
            throw new IllegalStateException("Invalid Media parameters were given");
        }

        return media;
    }


    @Override
    protected void onResume() {
        super.onResume();
        prepareMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSeekPos = mMediaPlayerControl.getCurrentPosition();
        mMediaPlayerControl.pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POS_TAG, mSeekPos);
        super.onSaveInstanceState(outState);
    }
}
