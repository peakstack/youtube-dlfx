package service;

import data.Playable;
import util.ServiceResponse;

public interface AudioPlayerService {

    ServiceResponse<Playable> play(Playable playable);

}
