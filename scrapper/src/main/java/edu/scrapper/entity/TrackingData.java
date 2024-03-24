package edu.scrapper.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TrackingData {

    @Id
    @GeneratedValue
    private Long id;
    private String url;
    private String updatedAt;
    private Long chatId;
}
