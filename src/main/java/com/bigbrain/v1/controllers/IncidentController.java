package com.bigbrain.v1.controllers;

import com.bigbrain.v1.models.Addresses;
import com.bigbrain.v1.models.Incidents;
import com.bigbrain.v1.models.Users;
import com.bigbrain.v1.DAOandRepositories.IncidentRepository;
import com.bigbrain.v1.DAOandRepositories.UsersRepository;
import com.bigbrain.v1.services.ParseErrorMessageService;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Controller
public class IncidentController {

    private final String apiKey = "AIzaSyA_yaxjGDKLtw8qlGU1FsVTafhZwyYT2PI";
    private IncidentRepository incidentRepository;

    private UsersRepository usersRepository;
    private ParseErrorMessageService parseErrorMessageService;

    @Autowired
    public IncidentController(IncidentRepository incidentRepository, UsersRepository usersRepository, ParseErrorMessageService parseErrorMessageService) {
        this.incidentRepository = incidentRepository;
        this.parseErrorMessageService = parseErrorMessageService;
        this.usersRepository = usersRepository;
    }

    //TODO delete
    @GetMapping("/incidentform")
    public String showIncidentForm( HttpSession httpSession, Model model){
       // System.out.println("Incident form: " + email);
        Users user = (Users ) httpSession.getAttribute("user");
        Incidents incident = new Incidents();
        incident.setUserIDFK(user.getUserIdPK());
        incident.setReportedByPhoneNumber(user.getPhoneNumber());
        model.addAttribute("newIncident", incident);
       // model.addAttribute("user", user);
        model.addAttribute("incidentAddress", new Addresses());
        return "incidentform";
    }

    // HttpServletRequest request used to recevie the image file
    @PostMapping("/incidentform")
    public String submitIncidentForm(HttpServletRequest request, @ModelAttribute("newIncident") Incidents newIncident,
                                     @ModelAttribute("incidentAddress") Addresses incidentAddress, Model model) throws IOException {
        // Store image into incidents obj
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        if (file != null && file.getContentType().startsWith("image/")) {
            byte[] imageData = file.getBytes();
            newIncident.setImage(imageData);
        }

        if(!incidentAddress.getZipCode().matches("\\d{5}")){
            model.addAttribute("errorMessage", "Zipcode must be 5 digits only");
            return "incidentform";
        }
        String address = incidentAddress.getAddressLine1() + " " + incidentAddress.getCity() + ", " + incidentAddress.getCity() + " " + incidentAddress.getZipCode();

        // geocode address
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context,
                    address).await();
            if (results != null && results.length > 0){
                newIncident.setLatitude(results[0].geometry.location.lat);
                newIncident.setLongitude(results[0].geometry.location.lng);
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDate dateNow = LocalDate.now();
        newIncident.setIncidentDate(Date.valueOf(dateNow));

        try {
            //System.out.println("New incident:" + newIncident.toString());
            incidentRepository.save(newIncident);
            return "redirect:/incidentmap";
        }catch (Exception e) {
            String parsedMessage = parseErrorMessageService.parseErrorMessage(e.getMessage());
            model.addAttribute("errorMessage", parsedMessage);
            return "incidentform";
        }
    }

    @GetMapping("/incidentmap")
    public String showIncidentMap(HttpSession httpSession, Model model){

        List<Incidents> allIncidents = incidentRepository.findAll();
        if (!CollectionUtils.isEmpty(allIncidents)){
            List<String> imgDataList = new ArrayList<>();
            for (Incidents allIncident : allIncidents) {
                if (allIncident.getImage() != null){
                    String base64String = Base64.getEncoder().encodeToString(allIncident.getImage());
                    imgDataList.add(base64String);
                }else {
                    imgDataList.add(null);
                }
            }
            model.addAttribute("imageList", imgDataList);
        }
        model.addAttribute("allIncidents", allIncidents);
        return "incidentmap";
    }

    @GetMapping("/incidentmap/stats")
    @ResponseBody
    public Map<String, Integer> getIncidentStats() {
        int [] stats = incidentStats();
        Map<String, Integer> response = new HashMap<>();
        response.put("resolvedIncidents", stats[0]);
        response.put("newIncidents", stats[1]);
        return response;
    }

    @GetMapping("/user/incidents")
    public String showUserIncidents(HttpSession httpSession, Model model){

        Users user = (Users) httpSession.getAttribute("user");
        List<Incidents> userIncidents = incidentRepository.findAllByID(user.getUserIdPK());

        if (!CollectionUtils.isEmpty(userIncidents)){
            List<String> imgDataList = new ArrayList<>();
            for (Incidents allIncident : userIncidents) {
                if (allIncident.getImage() != null){
                    String base64String = Base64.getEncoder().encodeToString(allIncident.getImage());
                    imgDataList.add(base64String);
                }else {
                    imgDataList.add(null);
                }
            }
            model.addAttribute("imageList", imgDataList);
        }

        //System.out.println("USERINCIDENTS: " + userIncidents);
        model.addAttribute("userIncidents", userIncidents);
        return "userincidents";
    }

    @GetMapping("/user/deleteincidents/{incidentIDPK}")
    public String deleteIncidents(@PathVariable int incidentIDPK,HttpSession httpSession, Model model){
        try{
            incidentRepository.deleteById(incidentIDPK);
            return "redirect:/incidentmap";
        }
        catch (Exception e){
            String parsedMessage = parseErrorMessageService.parseErrorMessage(e.getMessage());
            model.addAttribute("errorMessage", parsedMessage);
            return "userIncidents";
        }
    }

    @GetMapping("/user/updateincident/{incidentIDPK}")
    public String updateIncident(@PathVariable int incidentIDPK, Model model, HttpSession httpSession){
        Incidents incidentToUpdate = incidentRepository.findIncidentByPK(incidentIDPK);
        model.addAttribute("user", (Users) httpSession.getAttribute("user"));

        if (incidentToUpdate.getImage() != null){
            String base64String = Base64.getEncoder().encodeToString(incidentToUpdate.getImage());
            model.addAttribute("imgData", base64String);
        }

        model.addAttribute("incidentToUpdate", incidentToUpdate);
        model.addAttribute("incidentAddress", new Addresses());
        return "incidentupdateform";
    }

    @PostMapping("/user/updateincident")
    public String submitUpdateIncident(@ModelAttribute("incidentToUpdate") Incidents incidentToUpdate, HttpSession httpSession, @ModelAttribute("incidentAddress") Addresses incidentAddress, Model model){
        String address = incidentAddress.getAddressLine1() + " " + incidentAddress.getCity() + ", " + incidentAddress.getCity() + " " + incidentAddress.getZipCode();

        // geocode address
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context,
                    address).await();
            if (results != null && results.length > 0){
                incidentToUpdate.setLatitude(results[0].geometry.location.lat);
                incidentToUpdate.setLongitude(results[0].geometry.location.lng);
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


       try {
           // System.out.println("New incident:" + incidentToUpdate.toString());
           incidentRepository.updateById(incidentToUpdate, incidentToUpdate.getIncidentIDPK());
           return "redirect:/incidentmap";
       }catch (Exception e){
           String parsedMessage = parseErrorMessageService.parseErrorMessage(e.getMessage());
           model.addAttribute("errorMessage", parsedMessage);
           return "incidentupdateform";
       }


    }

    /*
    *Display stats for last month
    *Display number of total resolved and new incidents last month
     */
    public int [] incidentStats(){
        int [] arr = new int [2];
        int resolved_incidents = 0;
        int new_incidents = 0;

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfLastMonth = LocalDate.of(currentDate.getYear(), currentDate.minusMonths(1).getMonth(), 1);
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
        List<Incidents> lastMonthIncidents = incidentRepository.findByDateBetween(Date.valueOf(firstDayOfLastMonth), Date.valueOf(lastDayOfLastMonth));
        for ( Incidents incident : lastMonthIncidents){
            if ( incident.getStatus() == "New")
                new_incidents++;
            else
                resolved_incidents++;
        }
        arr[0] = resolved_incidents;
        arr[1] = new_incidents;
        return arr;
    }

}
