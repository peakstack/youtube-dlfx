package adapter;

import data.Audio;
import data.Video;

public class VideoToAudioAdapter {

    private final Video video;

    public VideoToAudioAdapter(Video video) {
        this.video = video;
    }

    public Audio transformToAudio() {
        Audio audio = new Audio();
        audio.setTitle(video.getTitle());
        audio.setAuthor(video.getAuthor());
        return audio;
    }

}
