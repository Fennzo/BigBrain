package com.bigbrain.v1.controllers;

import com.bigbrain.v1.models.Bills;
import com.bigbrain.v1.models.Users;
import com.bigbrain.v1.DAOandRepositories.BillsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BillController {


    private BillsRepository billsRepository;

    @Autowired
    public BillController(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    @GetMapping("/user/userbills")
    public String viewUserBills(HttpSession httpSession, Model model){
        Users user = (Users) httpSession.getAttribute("user");
        List<Bills> userBills = billsRepository.findByUserID(  user.getUserIdPK());
        //model.addAttribute("user", user);
        model.addAttribute("userBills", userBills);
        System.out.println("all user bills" + userBills);
        return "userallbills";
    }

    @GetMapping("/admin/allbills")
    public String viewAllBills(Model model){
        List<Bills> allBills = billsRepository.findAll();
        model.addAttribute("allBills", allBills);
        System.out.println(allBills);
        return "adminallbills";
    }
}
