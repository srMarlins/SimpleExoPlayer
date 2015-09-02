package com.jfowler.onramp.simpleexoplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jfowler.onramp.simpleexoplayer.Samples;
import com.jfowler.onramp.simpleexoplayer.Utils.MediaFactory;
import com.jfowler.onramp.simpleexoplayer.VideoPlayerActivity;
import com.jfowler.onramp.simpleexoplayerdemo.R;


/**
 * Created by jfowler on 9/1/15.
 */
public class MediaSelectorFragment extends Fragment{

    private static final String TAG = "MediaSelectorFragment";

    private TabHost mTabHost;
    private Context mContext;

    public static MediaSelectorFragment newInstance(String mediaType) {
        MediaSelectorFragment frag = new MediaSelectorFragment();
        Bundle args = new Bundle();
        args.putInt(MediaFactory.MEDIA_TYPE_TAG, MediaFactory.stringToInt(mediaType));
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_selector, container, false);

        Bundle args = getArguments();
        int mediaType = args.getInt(MediaFactory.MEDIA_TYPE_TAG);

        mTabHost = (TabHost)rootView.findViewById(R.id.tabHost);

        mTabHost.setup();

        TabHost.TabSpec specVideo = mTabHost.newTabSpec(getResources().getString(R.string.tab1_name));

        specVideo.setContent(R.id.listview_video);
        specVideo.setIndicator(getResources().getString(R.string.tab1_name));

        mTabHost.addTab(specVideo);
        TabHost.TabSpec specAudio = mTabHost.newTabSpec(getResources().getString(R.string.tab2_name));
        specAudio.setContent(R.id.listview_audio);
        specAudio.setIndicator(getResources().getString(R.string.tab2_name));

        mTabHost.addTab(specAudio);

        final Samples.Sample[] samples = Samples.getAllSampleByType(mediaType);
        MediaAdapter mediaAdapter = new MediaAdapter(mContext, samples);

        ListView videoList = (ListView) rootView.findViewById(R.id.listview_video);
        videoList.setAdapter(mediaAdapter);
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Samples.Sample holder = samples[position];
                Intent videoIntent = new Intent(mContext, VideoPlayerActivity.class);
                videoIntent.putExtra(MediaFactory.MEDIA_URI_TAG, holder.uri);
                videoIntent.putExtra(MediaFactory.MEDIA_TYPE_TAG, MediaFactory.MEDIA_TYPE_VIDEO);
                videoIntent.putExtra(MediaFactory.STREAM_TYPE_TAG, holder.type);
                startActivity(videoIntent);
            }
        });

        ListView audioList = (ListView) rootView.findViewById(R.id.listview_audio);
        audioList.setAdapter(mediaAdapter);
        audioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public class MediaAdapter extends BaseAdapter{

        private Samples.Sample[] mSamples;
        private Context mContext;

        public MediaAdapter(Context context, Samples.Sample[] samples){
            mContext = context;
            mSamples = samples;
        }

        @Override
        public int getCount() {
            return mSamples.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();
            Samples.Sample currentSample = mSamples[position];

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.listview_media_item, parent, false);

                holder.mMediaName = (TextView) convertView.findViewById(R.id.text_media_title);
                holder.mMediaType = (TextView) convertView.findViewById(R.id.text_media_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mMediaName.setText(currentSample.name);
            holder.mMediaType.setText(MediaFactory.intToString(currentSample.type));


            return convertView;
        }

    }

    static class ViewHolder{
        TextView mMediaName;
        TextView mMediaType;
    }
}
