package com.bigbrain.v1.controllers;

import com.bigbrain.v1.models.Addresses;
import com.bigbrain.v1.models.Users;
import com.bigbrain.v1.DAOandRepositories.AddressRepository;
import com.bigbrain.v1.DAOandRepositories.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private AddressRepository addressRepository;
    private UsersRepository usersRepository;

    @Autowired // constructor dependency injection
    public UserController(UsersRepository usersRepository, AddressRepository addressRepository){
        this.usersRepository = usersRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/profile")
    public String ShowProfile(HttpSession httpSession, Model model){
        Users user = (Users) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        return "profile";
    }


    @GetMapping("/profile/edit")
    public String EditProfile(HttpSession httpSession, Model model){
        Users user = (Users) httpSession.getAttribute("user");
        Addresses userAddress = addressRepository.findByUserID(user.getUserIdPK());
        model.addAttribute("userAddress", userAddress);
        model.addAttribute("user", user);
        //System.out.println("Pre profile: " + user.toString() + "\n" + userAddress.toString());
        return "editprofile";
    }

    @PostMapping("/profile/edit")
    public String SubmitProfileUpdate(@ModelAttribute("user") Users user, @ModelAttribute("userAddress") Addresses userAddress){
       // System.out.println("edit profile: " + user.toString() + "\n" + userAddress.toString());
        usersRepository.update(user, user.getUserIdPK());
        addressRepository.update(userAddress, userAddress.getUserIDFK());
        return "welcome";
    }


}
