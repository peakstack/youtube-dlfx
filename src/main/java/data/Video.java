package data;

public class Video extends Playable {

    private String videoUrl;
    private long views;

    public void setViews(long views) {
        this.views = views;
    }

    public long getViews() {
        return views;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String toString() {
        return title.getName() + " by " + author.getName();
    }
}
