package com.bigbrain.v1.controllers;

import com.bigbrain.v1.DAOandRepositories.AnnouncementRepository;
import com.bigbrain.v1.models.Announcements;
import com.bigbrain.v1.models.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class AnnouncementController {

    private AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementController(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @GetMapping("/admin/allannouncements")
    public String displayAllAnnouncements(Model model){
        List<Announcements> allAnnouncements = announcementRepository.findAll();
        model.addAttribute("allannouncements", allAnnouncements);
        return "adminallannouncements";
    }
    @GetMapping("/admin/createannouncement")
    public String createAnnouncement(Model model, HttpSession httpSession){
        Users user = (Users) httpSession.getAttribute("user");
        Announcements newAnnouncement = new Announcements(user.getUserIdPK());
        model.addAttribute("newannouncement", newAnnouncement);
        return "adminannouncementform";
    }

    @PostMapping("/admin/createannouncement")
    public String submitAnnouncement(@ModelAttribute("newAnnouncement") Announcements newAnnouncement){
        announcementRepository.save(newAnnouncement);
        return "redirect:/admin/allannouncements";
    }


    @GetMapping("/admin/updateannouncment/{announcementIDPK}")
    public String updateAnnouncement(@PathVariable int announcementIDPK, Model model){
        Announcements announcementUpdatable = announcementRepository.findByPk(announcementIDPK);
        model.addAttribute("announcementUpdatable", announcementUpdatable);
        return "adminannouncementupdateform";
    }

    @PostMapping("/admin/updateannouncment")
    public String submitUpdateAnnouncement(Announcements updateAnnouncement){
        announcementRepository.update(updateAnnouncement, updateAnnouncement.getAnnouncementIDPK());
        return "redirect:/admin/allannouncements";
    }

    @GetMapping("/admin/deleteannouncenment/{announcementIDPK}")
    public String deleteAnnouncement(@PathVariable int announcementIDPK){
        announcementRepository.deleteByID(announcementIDPK);
        return "redirect:/admin/allannouncements";
    }
}