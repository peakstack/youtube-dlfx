package service.impl;

import dao.VideoPlayerDao;
import service.VideoPlayerService;

public class VideoPlayerServiceImpl implements VideoPlayerService {

    private final VideoPlayerDao dao;

    public VideoPlayerServiceImpl(VideoPlayerDao dao) {
        this.dao = dao;
    }



}
