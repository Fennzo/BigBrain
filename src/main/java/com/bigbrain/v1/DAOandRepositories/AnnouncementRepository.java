package com.bigbrain.v1.DAOandRepositories;

import com.bigbrain.v1.models.Announcements;
import com.bigbrain.v1.models.Incidents;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementRepository implements AnnouncementDao{

    private final JdbcTemplate jdbc;

    public AnnouncementRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int save(Announcements announcement) {
        return jdbc.update(("INSERT INTO Announcements(userIDFK, Severity, title, Description) VALUES (?, ?, ?, ?)"),
                announcement.getUserIDFK(), announcement.getSeverity(), announcement.getTitle(), announcement.getDescription());
    }

    @Override
    public int deleteByID(int announcementIDPK) {
        try{
            return jdbc.update("DELETE FROM Announcements WHERE announcementIDPK=?", announcementIDPK);
        }
        catch(EmptyResultDataAccessException e){
            return 0;
        }
        catch(DataAccessException e){
            System.err.println(e);
            return 0;
        }
    }

    @Override
    public int update(Announcements announcement, int announcementIDPK) {
        return jdbc.update("UPDATE Announcements SET Severity=?, Title=?, Description=? WHERE AnnouncementIdPK=? ",
                announcement.getSeverity(), announcement.getTitle(), announcement.getDescription(),
                announcementIDPK);
    }

    @Override
    public Announcements findLatest() {

        return (Announcements) jdbc.queryForObject("SELECT TOP 1 * FROM Announcements ORDER BY AnnouncementIdPK desc ", new Object[]{}, new BeanPropertyRowMapper(Announcements.class));
    }

    @Override
    public Announcements findByPk(int announcementIDPK) {
        return (Announcements) jdbc.queryForObject("SELECT * FROM Announcements WHERE announcementIDPK = ?", new Object[]{announcementIDPK}, new BeanPropertyRowMapper(Announcements.class));
    }

    @Override
    public List<Announcements> findAll() {

        return jdbc.query("SELECT announcementIDPK, Severity, Title, UserIDFK, Description, AnnouncementDate IncidentDate FROM Announcements ",
                new BeanPropertyRowMapper<>(Announcements.class));
    }
}
