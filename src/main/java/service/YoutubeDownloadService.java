package service;

import data.Playable;
import util.ServiceResponse;

public interface YoutubeDownloadService {
    ServiceResponse<Playable> downloadById(String id);
}
