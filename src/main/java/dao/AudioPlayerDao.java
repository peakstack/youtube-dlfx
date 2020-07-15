package dao;

import data.Playable;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.log4j.Logger;
import util.DAOException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class AudioPlayerDao {

    private final Logger logger = Logger.getLogger(AudioPlayerDao.class);

    public Optional<Playable> play(Playable playable) throws DAOException {
        try {
            final FileInputStream stream = new FileInputStream(playable.getFile().getPath());
            final Player player = new Player(stream);
            player.play();
            return Optional.of(playable);
        } catch (JavaLayerException | FileNotFoundException e) {
            logger.error("Can't play audio", e);
            throw new DAOException("Can't play audio");
        }
    }
}
