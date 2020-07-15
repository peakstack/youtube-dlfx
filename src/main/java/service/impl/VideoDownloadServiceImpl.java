package service.impl;

import dao.VideoDownloadDao;
import data.Playable;
import data.Video;
import org.apache.log4j.Logger;
import service.VideoDownloadService;
import util.DAOException;
import util.ServiceResponse;

import java.text.MessageFormat;
import java.util.Optional;

public class VideoDownloadServiceImpl implements VideoDownloadService {

    private final VideoDownloadDao dao;

    public VideoDownloadServiceImpl(VideoDownloadDao dao) {
        this.dao = dao;
    }

    private final Logger logger = Logger.getLogger(VideoDownloadServiceImpl.class);

    @Override
    public ServiceResponse<Playable> downloadById(String id) {
        ServiceResponse<Playable> response = new ServiceResponse<>();

        try {
            Optional<Video> optionalVideo = dao.downloadVideoById(id);

            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                response.setResponseObject(video);

                logger.info(MessageFormat.format("Video downloaded: Title: {0}, Author: {1}", video.getTitle(), video.getAuthor()));
            }

        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            response.addErrorMessages("videoDownloadService.cannotDownload");
        }
        return response;
    }
}
