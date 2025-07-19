package com.clearance.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClearanceController {

    @GetMapping({"/", "/clearance"})
    public String clearance() {

        return "clearance";
    }
}
