package com.jfowler.onramp.simpleexoplayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.SimpleExoPlayer;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.Media;
import com.jfowler.onramp.simpleexoplayer.SimpleExoPlayer.MediaModels.VideoMedia;

public class MainActivity extends Activity {

    private SimpleExoPlayer simpleExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleExoPlayer = new SimpleExoPlayer();

        final VideoView videoView = (VideoView) findViewById(R.id.videoViewSurface);
        final Button play = (Button) findViewById(R.id.buttonPlay);
        Button pause = (Button) findViewById(R.id.buttonPause);
        Button stop = (Button) findViewById(R.id.buttonStop);
        final EditText input = (EditText) findViewById(R.id.editTextURI);
        final ExoPlayer.Listener listener = new ExoPlayer.Listener() {
            @Override
            public void onPlayerStateChanged(boolean b, int i) {
            }

            @Override
            public void onPlayWhenReadyCommitted() {
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
                simpleExoPlayer.reinstantiateExoPlayer();
                play.setEnabled(true);
                Toast.makeText(MainActivity.this, "Error playing, try again", Toast.LENGTH_SHORT).show();
            }
        };

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!simpleExoPlayer.isPlaying()) {
                    String url = input.getText().toString().trim();
                    if(url != null && !url.equals("")) {
                        Media media = new VideoMedia(Uri.parse(url));
                        simpleExoPlayer.playMedia(MainActivity.this, listener, videoView.getHolder().getSurface(), media);
                        play.setEnabled(false);
                    }else{
                        Toast.makeText(MainActivity.this, "No input", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Already playing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer.isPlaying()) {
                    simpleExoPlayer.pauseMedia();
                    play.setEnabled(true);
                }else{
                    Toast.makeText(MainActivity.this, "Is not playing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.stopMedia();
                play.setEnabled(true);
            }
        });
    }

}
