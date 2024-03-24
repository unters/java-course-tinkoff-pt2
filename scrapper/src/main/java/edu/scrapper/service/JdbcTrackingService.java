package edu.scrapper.service;

import edu.common.dto.tracking.TrackingDataTo;
import edu.scrapper.dao.TrackingDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTrackingService implements TrackingService {

    private final TrackingDao trackingDao;

    @Override
    public void trackUrl(TrackingDataTo trackingDataTo) {
        // TODO: add exception handling logic (url is already being tracked, chat is not registered and so on).
        trackingDao.trackUrl(trackingDataTo.getChatId(), trackingDataTo.getUrl().toString());
    }

    @Override
    public void untrackUrl(TrackingDataTo trackingDataTo) {
        trackingDao.untrackUrl(trackingDataTo.getChatId(), trackingDataTo.getUrl().toString());
    }
}
