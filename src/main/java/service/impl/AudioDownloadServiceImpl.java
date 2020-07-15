package service.impl;

import dao.AudioDownloadDao;
import data.Audio;
import data.Playable;
import org.apache.log4j.Logger;
import service.AudioDownloadService;
import util.DAOException;
import util.ServiceResponse;

import java.text.MessageFormat;
import java.util.Optional;

public class AudioDownloadServiceImpl implements AudioDownloadService {

    private final AudioDownloadDao dao;

    public AudioDownloadServiceImpl(AudioDownloadDao dao) {
        this.dao = dao;
    }

    private final Logger logger = Logger.getLogger(AudioDownloadServiceImpl.class);

    @Override
    public ServiceResponse<Playable> downloadById(String id) {
        ServiceResponse<Playable> response = new ServiceResponse<>();

        try {
            Optional<Audio> optionalAudio = dao.downloadAudioById(id);

            if (optionalAudio.isPresent()) {
                Audio audio = optionalAudio.get();
                response.setResponseObject(audio);

                logger.info(MessageFormat.format("Video downloaded: Title: {0}, Author: {1}", audio.getTitle(), audio.getAuthor()));
            }

        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            response.addErrorMessages("videoDownloadService.cannotDownload");
        }
        return response;
    }
}
