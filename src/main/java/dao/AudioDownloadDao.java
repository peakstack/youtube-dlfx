package dao;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import data.Audio;
import data.Author;
import data.Title;
import org.apache.log4j.Logger;
import util.DAOException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AudioDownloadDao {

    private final Logger logger = Logger.getLogger(AudioDownloadDao.class);

    public Optional<Audio> downloadAudioById(String id) throws DAOException {
        Audio audio = new Audio();

        YoutubeDownloader downloader = new YoutubeDownloader();
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

        try {
            YoutubeVideo ytVideo = downloader.getVideo(id);
            VideoDetails details = ytVideo.details();

            Author author = new Author();
            author.setName(details.author());
            author.setChannelUrl(details.author());

            List<AudioFormat> audioVideoFormats = ytVideo.audioFormats();

            if (audioVideoFormats.isEmpty()) {
                throw new DAOException("Can't find audio with quality");
            }

            File outputDir = new File("audios");
            ytVideo.downloadAsync(audioVideoFormats.get(0), outputDir, new OnYoutubeDownloadListener() {
                @Override
                public void onDownloading(int progress) {
                    System.out.println("Downloaded " + progress + "%\n");
                }

                @Override
                public void onFinished(File file) {
                    audio.setFile(file);
                    logger.info("Download finished");
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.error("Error: " + throwable.getMessage(), throwable);
                }
            });

            Title title = new Title();
            title.setName(details.title());

            audio.setTitle(title);
            audio.setAuthor(author);

            return Optional.of(audio);
        } catch (YoutubeException | IOException e) {
            throw new DAOException("Can't download audio");
        }
    }
}
