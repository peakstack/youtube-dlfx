package service.impl;

import dao.AudioPlayerDao;
import data.Playable;
import org.apache.log4j.Logger;
import service.AudioPlayerService;
import util.DAOException;
import util.ServiceResponse;

import java.text.MessageFormat;
import java.util.Optional;

public class AudioPlayerServiceImpl implements AudioPlayerService {

    private final Logger logger = Logger.getLogger(AudioPlayerServiceImpl.class);

    private final AudioPlayerDao dao;

    public AudioPlayerServiceImpl(AudioPlayerDao dao) {
        this.dao = dao;
    }

    @Override
    public ServiceResponse<Playable> play(Playable playable) {
        ServiceResponse<Playable> response = new ServiceResponse<>();

        try {
            Optional<Playable> optionalPlayable = dao.play(playable);

            if (optionalPlayable.isPresent()) {
                Playable localPlayable = optionalPlayable.get();
                response.setResponseObject(localPlayable);

                logger.info(MessageFormat.format("Audio played: Title: {0}, Author: {1}", playable.getTitle().getName(), playable.getAuthor().getName()));
            }
        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            response.addErrorMessages("audioPlayer.cannotPlay");
        }
        return response;
    }
}