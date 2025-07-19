package com.clearance.app.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClearanceController {

    @GetMapping({"/", "/clearance"})
    public String clearance(Model model) {
        model.addAttribute("pageTitle", "Clearance-Page");
        return "clearance";
    }

    @GetMapping("/add-new-clearance")
    public String addNewClearancePage(Model model){

        model.addAttribute("pageTitle", "add-new-clearance-Page");
        return "add-new-clearance";
    }

    @PostMapping("/add-new-clearance")
    public String addNewClearance(){
        return "redirect:/clearance";
    }
}
