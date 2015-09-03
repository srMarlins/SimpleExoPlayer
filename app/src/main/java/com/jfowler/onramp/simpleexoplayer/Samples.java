/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfowler.onramp.simpleexoplayer;

import com.jfowler.onramp.simpleexoplayer.Utils.MediaFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Holds statically defined sample definitions.
 */
public class Samples {

    public static class Sample {
        public final String name;
        public final String contentId;
        public final String uri;
        public final int type;

        public Sample(String name, String uri, int type) {
            this(name, name.toLowerCase(Locale.US).replaceAll("\\s", ""), uri, type);
        }

        public Sample(String name, String contentId, String uri, int type) {
            this.name = name;
            this.contentId = contentId;
            this.uri = uri;
            this.type = type;
        }

    }

    public static final Sample[] YOUTUBE_DASH_MP4 = new Sample[]{
            new Sample("Google Glass",
                    "http://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?"
                            + "as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&"
                            + "ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7."
                            + "8506521BFC350652163895D4C26DEE124209AA9E&key=ik0", MediaFactory.STREAM_TYPE_DASH),
            new Sample("Google Play",
                    "http://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?"
                            + "as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&"
                            + "ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29."
                            + "84308FF04844498CE6FBCE4731507882B8307798&key=ik0", MediaFactory.STREAM_TYPE_DASH),
    };


    public static final Sample[] SMOOTHSTREAMING = new Sample[]{
            new Sample("Super speed",
                    "http://playready.directtaps.net/smoothstreaming/SSWSS720H264/SuperSpeedway_720.ism/manifest",
                    MediaFactory.STREAM_TYPE_SS),
            new Sample("Big Buck Bunny Multi Res",
                    "http://mediadl.microsoft.com/mediadl/iisnet/smoothmedia/Experience/BigBuckBunny_720p.ism/Manifest",
                    MediaFactory.STREAM_TYPE_SS),
            new Sample("To The Limit",
                    "http://playready.directtaps.net/smoothstreaming/TTLSS720VC1/To_The_Limit_720.ism/Manifest",
                    MediaFactory.STREAM_TYPE_SS),

    };

    public static final Sample[] SMOOTHSTREAM_AUDIO = new Sample[]{
        new Sample("Taxi3",
                "http://playready.directtaps.net/smoothstreaming/ISMAAACLC/Taxi3_AACLC.ism/Manifest",
                MediaFactory.STREAM_TYPE_SS)
    };


    public static final Sample[] HLS_VIDEO = new Sample[]{
            new Sample("Apple master playlist",
                    "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/"
                            + "bipbop_4x3_variant.m3u8", MediaFactory.STREAM_TYPE_HLS),
            new Sample("Apple master playlist advanced",
                    "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/"
                            + "bipbop_16x9_variant.m3u8", MediaFactory.STREAM_TYPE_HLS),
            new Sample("Apple TS media playlist",
                    "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/"
                            + "prog_index.m3u8", MediaFactory.STREAM_TYPE_HLS),
    };

    public static final Sample[] HLS_AUDIO = new Sample[]{
            new Sample("Apple AAC media playlist",
                    "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear0/"
                            + "prog_index.m3u8", MediaFactory.STREAM_TYPE_HLS),
    };

    public static final Sample[] STANDARD_VIDEO = new Sample[]{
            new Sample("Dizzy", "http://html5demos.com/assets/dizzy.mp4", MediaFactory.STREAM_TYPE_STANDARD),
            new Sample("Android screens (Matroska)", "http://storage.googleapis.com/exoplayer-test-media-1/"
                    + "mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv",
                    MediaFactory.STREAM_TYPE_STANDARD),
            new Sample("Big Buck Bunny (MP4 Video)",
                    "http://redirector.c.youtube.com/videoplayback?id=604ed5ce52eda7ee&itag=22&source=youtube&"
                            + "sparams=ip,ipbits,expire,source,id&ip=0.0.0.0&ipbits=0&expire=19000000000&signature="
                            + "513F28C7FDCBEC60A66C86C9A393556C99DC47FB.04C88036EEE12565A1ED864A875A58F15D8B5300"
                            + "&key=ik0", MediaFactory.STREAM_TYPE_STANDARD),
    };

    public static final Sample[] STANDARD_AUDIO = new Sample[]{
            new Sample("Apple AAC 10s", "https://devimages.apple.com.edgekey.net/"
                    + "streaming/examples/bipbop_4x3/gear0/fileSequence0.aac", MediaFactory.STREAM_TYPE_STANDARD),
            new Sample("Apple TS 10s", "https://devimages.apple.com.edgekey.net/streaming/examples/"
                    + "bipbop_4x3/gear1/fileSequence0.ts", MediaFactory.STREAM_TYPE_STANDARD),
            new Sample("Google Play (MP3 Audio)",
                    "http://storage.googleapis.com/exoplayer-test-media-0/play.mp3", MediaFactory.STREAM_TYPE_STANDARD),
            new Sample("WUFT Live Stream",
                    "http://ice02.jou.ufl.edu:8000/wufthd164", MediaFactory.STREAM_TYPE_STANDARD),
    };

    public static Sample[] getAllVideoSamples(){
        ArrayList<Sample> samples = new ArrayList<>();
        samples.addAll(Arrays.asList(Samples.HLS_VIDEO));
        samples.addAll(Arrays.asList(Samples.SMOOTHSTREAMING));
        samples.addAll(Arrays.asList(Samples.YOUTUBE_DASH_MP4));
        samples.addAll(Arrays.asList(Samples.STANDARD_VIDEO));

        return samples.toArray(new Sample[samples.size()]);
    }

    public static Sample[] getAllVideoSamplesByType(int type){
        ArrayList<Sample> samples = new ArrayList<>();
        for (Sample sample: getAllVideoSamples()) {
            if(type == sample.type){
                samples.add(sample);
            }
        }

        return samples.toArray(new Sample[samples.size()]);
    }

    public static Sample[] getAllAudioSamples(){
        ArrayList<Sample> samples = new ArrayList<>();
        samples.addAll(Arrays.asList(getAllVideoSamples()));
        samples.addAll(Arrays.asList(Samples.SMOOTHSTREAM_AUDIO));
        samples.addAll(Arrays.asList(Samples.STANDARD_AUDIO));
        samples.addAll(Arrays.asList(Samples.HLS_AUDIO));

        return samples.toArray(new Sample[samples.size()]);
    }

    public static Sample[] getAllAudioSamplesByType(int type){
        ArrayList<Sample> samples = new ArrayList<>();
        for (Sample sample: getAllAudioSamples()) {
            if(type == sample.type){
                samples.add(sample);
            }
        }
        return samples.toArray(new Sample[samples.size()]);
    }

    private Samples() {
    }

}
