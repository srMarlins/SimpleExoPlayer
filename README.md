# SimpleExoPlayer

## About
An easy to use wrapper for Google's ExoPlayer class (A better replacement for MediaPlayer).
To use put this line into your app's build.gradle file

  `compile 'com.srmarlins.simpleexoplayer:simpleexoplayer:X.X.X'`
  
and replace the X.X.X with your desired version number.

This is intended for people unfamiliar with ExoPlayer and MediaPlayer and their limitations/problems.  

Features
  - Stream standard audio and video files from local or remote files.
  - Stream DASH media
  - SmoothStreaming
  - Supports all media codecs used in Android's MediaCodec
  - Requires API 16 or greater
  - HLS streaming
  
In Progress
   - Demo application using the SimpleExoPlayer
   - DRM support

*Please note that the MediaCodec class will often throw errors on the emulator while using this library.  Please test on devices before submitting bug reports.

## Examples
### Standard Media
    SimpleExoPlayer player = new SimpleExoPlayer();
    VideoMedia media = new VideoMedia(mContext, Uri.parse(url), userAgent);
    player.playMedia(this, null, mVideoView.getHolder().getSurface(), media);
            
### Adaptive Playback - Dash/HLS/SmoothStreaming
    SimpleExoPlayer player = new SimpleExoPlayer();
    DashMedia dashMedia = new DashMedia(context, new MediaListener() {
    @Override
    public void mediaPrepared(Media media) {
    mSimpleExoPlayer.playMedia(VideoPlayerActivity.this, null, 		mVideoView.getHolder().getSurface(), media);
    }
    }, Uri.parse(uri), userAgent);
