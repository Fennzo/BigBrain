package com.bigbrain.v1.models;

import java.util.Date;


public class Announcements {

    public enum Severities {
        Urgent, Social, Informative
    }

    private int announcementIDPK;
    private int userIDFK;
    private String severity;
    private Date announcementDate;
    private String title;
    private String description;

    public Announcements() {

    }

    public Announcements(int userIDFK) {
        this.userIDFK = userIDFK;
    }

    public Announcements(int userIDFK, String category, String title, String description) {
        this.userIDFK = userIDFK;
        this.severity = category;
        this.title = title;
        this.description = description;
    }

    public int getAnnouncementIDPK() {
        return announcementIDPK;
    }

    public int getUserIDFK() {
        return userIDFK;
    }

    public void setUserIDFK(int userIDFK) {
        this.userIDFK = userIDFK;
    }

    public void setAnnouncementIDPK(int announcementIDPK) {
        this.announcementIDPK = announcementIDPK;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String category) {
        this.severity = category;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Announcements [announcementPK=" + announcementIDPK + ", userIDFK=" + userIDFK + ", severity=" + severity + ", announcementDate=" + announcementDate + ", title=" + title + ", description=" + description + "]";
    }

}
