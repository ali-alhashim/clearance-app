package com.clearance.app.controller;



import com.clearance.app.dto.ClearanceDto;
import com.clearance.app.model.AppUser;
import com.clearance.app.model.Clearance;
import com.clearance.app.model.Employee;
import com.clearance.app.repository.AppUserRepository;
import com.clearance.app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClearanceController {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @GetMapping({"/", "/clearance"})
    public String clearance(Model model) {
        model.addAttribute("pageTitle", "Clearance-Page");
        return "clearance";
    }

    @GetMapping("/add-new-clearance")
    public String addNewClearancePage(Model model, @ModelAttribute ClearanceDto clearanceDto){

        List<Employee> employees = employeeRepository.findAll();
        List<AppUser> managers = appUserRepository.findByIsManager(true);
        List<AppUser> accounts = appUserRepository.findByDepartment("ACCOUNTS");

        model.addAttribute("accounts", accounts);
        model.addAttribute("employees", employees);
        model.addAttribute("managers", managers);
        model.addAttribute("pageTitle", "add-new-clearance-Page");
        return "add-new-clearance";
    }

    @PostMapping("/add-new-clearance")
    public String addNewClearance(@ModelAttribute ClearanceDto clearanceDto){

        Clearance clearance = new Clearance();

        return "redirect:/clearance";
    }
}
