package com.bigbrain.v1.DAOandRepositories;

import com.bigbrain.v1.models.Announcements;

import java.util.List;

public interface AnnouncementDao {

    int save(Announcements announcement);
    int deleteByID(int announcementIDPK);
    int update(Announcements announcement);
    Announcements findLatest();
    Announcements findByPk(int announcementIDPK);
    List<Announcements> findAll();
}
