package com.bigbrain.v1.controllers;

import com.bigbrain.v1.DAOandRepositories.AnnouncementRepository;
import com.bigbrain.v1.models.Addresses;
import com.bigbrain.v1.models.Announcements;
import com.bigbrain.v1.models.Users;
import com.bigbrain.v1.DAOandRepositories.AddressRepository;
import com.bigbrain.v1.DAOandRepositories.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//After logged in
@Controller
public class LoginController {

    private AddressRepository addressRepository;
    private UsersRepository usersRepository;

    private AnnouncementRepository announcementRepository;

    @Autowired
    public LoginController(AddressRepository addressRepository, UsersRepository usersRepository, AnnouncementRepository announcementRepository) {
        this.addressRepository = addressRepository;
        this.usersRepository = usersRepository;
        this.announcementRepository = announcementRepository;
    }

    @RequestMapping(value = "/")
    public String indexPage() {
        return "index";
    }


    @RequestMapping(value =  "/welcome")
    public String postLogin(@AuthenticationPrincipal OAuth2User principal, ModelMap model, HttpSession httpSession){
        String email = principal.getAttribute("email");
        Users user = usersRepository.findByEmail(email);

        System.out.println("Found user: " + user);
        // User not found, proceed with registration
        if ( user == null){
            String firstName = principal.getAttribute("given_name");
            String lastName = principal.getAttribute("family_name");
            Users newUser = new Users(email, firstName, lastName);

            model.addAttribute("newUser", newUser);
            //System.out.println("NEW USER: " + newUser.toString());
            model.addAttribute("newAddress", new Addresses());
            return "registration";
        }

        model.addAttribute("user", user);
        httpSession.setAttribute("user", user);

        //feed announcement to welcome
        Announcements latestAnnouncement = announcementRepository.findLatest();
        model.addAttribute("latestAnnouncement", latestAnnouncement);
        return "welcome";
    }

    // Registration form submission
    @PostMapping("/registration")
    public String submitRegistration(@ModelAttribute("newUser") Users newUser, @ModelAttribute("newAddress") Addresses newAddress, HttpSession httpSession, ModelMap model){

        newUser.setAddress(newAddress);
        newUser.setSubscriptionStatus(Users.SubscriptionStatues.Pending.toString());
        newUser.setRole(Users.Roles.Homeowner);
        usersRepository.save(newUser);
        Users user = usersRepository.findByEmail(newUser.getEmail());
        //System.out.println("Form submission newUser: " + user.toString());
        newAddress.setUserIDFK(user.getUserIdPK());
        user.setAddress(newAddress);
       // System.out.println("Form submission: " + user.toString() + newAddress.toString());
        httpSession.setAttribute("user", user);
        model.addAttribute("user", user);
        addressRepository.save(newAddress);
        registerUserRoleWithSpringSecurity(SecurityContextHolder.getContext().getAuthentication(), user);
        Announcements latestAnnouncement = announcementRepository.findLatest();
        model.addAttribute("latestAnnouncement", latestAnnouncement);
        return "redirect:/backend";
    }

    @GetMapping("/backend")
    public String backend(@AuthenticationPrincipal OAuth2User principal, Model model){
        String email = principal.getAttribute("email");
        Users user = usersRepository.findByEmail(email);

        //System.out.println("Found user: " + user);
        // User not found, proceed with registration
        if ( user == null){
            String firstName = principal.getAttribute("given_name");
            String lastName = principal.getAttribute("family_name");
            Users newUser = new Users(email, firstName, lastName);

            model.addAttribute("newUser", newUser);
            //System.out.println("NEW USER: " + newUser.toString());
            model.addAttribute("newAddress", new Addresses()); // adduserIDFK
            return "registration";
        }

        registerUserRoleWithSpringSecurity(SecurityContextHolder.getContext().getAuthentication(), user);

        model.addAttribute("user", user);
        return "main";
    }


    // Add user's role from db to spring security
    public void registerUserRoleWithSpringSecurity(Authentication authentication, Users user){
        // Create a new authentication token with the user's role as a granted authority
        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
                AuthorityUtils.createAuthorityList(user.getRole()));

        // Set the new authentication token for the current security context
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
