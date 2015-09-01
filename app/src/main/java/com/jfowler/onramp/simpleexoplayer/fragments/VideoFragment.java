package com.jfowler.onramp.simpleexoplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfowler.onramp.simpleexoplayerdemo.R;

/**
 * Created by jfowler on 9/1/15.
 */
public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        return rootView;
    }

}
