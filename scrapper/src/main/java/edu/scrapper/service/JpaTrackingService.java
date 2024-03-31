package edu.scrapper.service;

import edu.common.dto.tracking.TrackingDataTo;
import edu.scrapper.jpa.HibernateSessionFactoryUtil;
import edu.scrapper.jpa.Tracking;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class JpaTrackingService implements TrackingService {

    private final HibernateSessionFactoryUtil hibernateSessionFactoryUtil;

    @Override
    public void trackUrl(TrackingDataTo trackingDataTo) {
        Tracking tracking = new Tracking();
        tracking.setId(trackingDataTo.getChatId());
        tracking.setUrl(trackingDataTo.getUrl().toString());
        tracking.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(tracking);
        tx1.commit();
        session.close();
    }

    @Override
    public void untrackUrl(TrackingDataTo trackingDataTo) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.createQuery("DELETE FROM Tracking WHERE chat_id = :chat_id AND url = :url")
            .setParameter("chat_id", trackingDataTo.getChatId())
            .setParameter("url", trackingDataTo.getUrl().toString())
            .executeUpdate();
        tx1.commit();
        session.flush();
        session.close();
    }
}
