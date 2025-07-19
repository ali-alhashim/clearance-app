package com.clearance.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClearanceController {

    @GetMapping({"/", "/clearance"})
    public String clearance(Model model) {
        model.addAttribute("pageTitle", "Clearance-Page");
        return "clearance";
    }
}
