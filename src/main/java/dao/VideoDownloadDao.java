package dao;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import data.Author;
import data.Title;
import data.Video;
import org.apache.log4j.Logger;
import util.DAOException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VideoDownloadDao {

    private final Logger logger = Logger.getLogger(VideoDownloadDao.class);

    public Optional<Video> downloadVideoById(String id) throws DAOException {
        Video video = new Video();

        YoutubeDownloader downloader = new YoutubeDownloader();
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

        try {
            YoutubeVideo ytVideo = downloader.getVideo(id);
            VideoDetails details = ytVideo.details();

            Author author = new Author();
            author.setName(details.author());
            author.setChannelUrl(details.author());

            video.setViews(details.viewCount());
            video.setVideoUrl(details.liveUrl());

            List<AudioVideoFormat> videoFormats = ytVideo.videoWithAudioFormats();

            if (videoFormats.isEmpty()) {
                throw new DAOException("Can't find video with quality");
            }

            File outputDir = new File("videos");

            ytVideo.downloadAsync(videoFormats.get(0), outputDir, new OnYoutubeDownloadListener() {
                @Override
                public void onDownloading(int progress) {
                    System.out.println("Downloaded " + progress + "%\n");
                }

                @Override
                public void onFinished(File file) {
                    video.setFile(file);
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.error("Error: " + throwable.getMessage(), throwable);
                }
            });

            Title title = new Title();
            title.setName(details.title());

            video.setTitle(title);
            video.setAuthor(author);

            return Optional.of(video);
        } catch (YoutubeException | IOException e) {
            throw new DAOException("Can't download video");
        }
    }
}
