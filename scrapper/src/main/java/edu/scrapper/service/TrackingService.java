package edu.scrapper.service;

import edu.common.dto.tracking.TrackingDataTo;

public interface TrackingService {

    void trackUrl(TrackingDataTo trackingDataTo);

    void untrackUrl(TrackingDataTo trackingDataTo);
}
