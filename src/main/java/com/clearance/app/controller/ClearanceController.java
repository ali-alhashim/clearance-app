package com.clearance.app.controller;



import com.clearance.app.dto.ClearanceDto;
import com.clearance.app.model.AppUser;
import com.clearance.app.model.Approval;
import com.clearance.app.model.Clearance;
import com.clearance.app.model.Employee;
import com.clearance.app.repository.AppUserRepository;
import com.clearance.app.repository.ClearanceRepository;
import com.clearance.app.repository.EmployeeRepository;
import com.clearance.app.service.ClearanceService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClearanceController {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ClearanceService clearanceService;

    @Autowired
    ClearanceRepository clearanceRepository;

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
        List<AppUser> its = appUserRepository.findByDepartment("IT");
        List<AppUser> purchasing = appUserRepository.findByDepartment("PURCHASING");
        List<AppUser> customerService = appUserRepository.findByDepartment("CUSTOMER-SERVICE");
        List<AppUser> mobileSIM = appUserRepository.findByDepartment("MOBILE");
        List<AppUser> hr = appUserRepository.findByDepartment("HR");
        List<AppUser> companyVehicle = appUserRepository.findByDepartment("VEHICLE");
        List<AppUser> security = appUserRepository.findByDepartment("SECURITY");
        List<AppUser> medicalInsurance = appUserRepository.findByDepartment("INSURANCE");
        List<AppUser> salesAndMarketing = appUserRepository.findByDepartment("SALES");
        List<AppUser> finance = appUserRepository.findByDepartment("FINANCE");

        model.addAttribute("finance", finance);
        model.addAttribute("salesAndMarketing", salesAndMarketing);
        model.addAttribute("medicalInsurance", medicalInsurance);
        model.addAttribute("security", security);
        model.addAttribute("hr", hr);
        model.addAttribute("companyVehicle", companyVehicle);
        model.addAttribute("mobileSIM", mobileSIM);
        model.addAttribute("customerService", customerService);
        model.addAttribute("purchasing", purchasing);
        model.addAttribute("its", its);
        model.addAttribute("accounts", accounts);
        model.addAttribute("employees", employees);
        model.addAttribute("managers", managers);
        model.addAttribute("pageTitle", "add-new-clearance-Page");
        return "add-new-clearance";
    }

    @PostMapping("/add-new-clearance")
    public String addNewClearance(@ModelAttribute ClearanceDto clearanceDto, RedirectAttributes redirectAttributes){

        Clearance clearance = new Clearance();

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = currentUser.getEmail();

        Employee employee = employeeRepository.findByBadgeNumber(clearanceDto.getBadgeNumber()).orElse(null);
        if(employee ==null)
        {
            redirectAttributes.addAttribute("error", "The Employee not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser itUser = appUserRepository.findByEmail(clearanceDto.getItEmail()).orElse(null);
        if(itUser ==null)
        {
            redirectAttributes.addAttribute("error", "The IT User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser accountsUser = appUserRepository.findByEmail(clearanceDto.getAccountsEmail()).orElse(null);
        if(accountsUser == null)
        {
            redirectAttributes.addAttribute("error", "The Accounts User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser hrUser = appUserRepository.findByEmail(clearanceDto.getHrEmail()).orElse(null);
        if(hrUser == null)
        {
            redirectAttributes.addAttribute("error", "The HR User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser managerUser = appUserRepository.findByEmail(clearanceDto.getDirectManagerEmail()).orElse(null);
        if(managerUser == null)
        {
            redirectAttributes.addAttribute("error", "The Direct Manager User not exist !");
            return "redirect:/add-new-clearance";
        }



        List<Approval> approvals = new ArrayList<>();

        Approval itApproval = new Approval();
        itApproval.setApprovalEmail(clearanceDto.getItEmail());
        itApproval.setName(itUser.getName());
        itApproval.setDepartment(itUser.getDepartment());


        Approval accountsApproval = new Approval();
        accountsApproval.setApprovalEmail(clearanceDto.getAccountsEmail());
        accountsApproval.setDepartment(accountsUser.getDepartment());
        accountsApproval.setName(accountsUser.getName());


        Approval hrApproval = new Approval();
        hrApproval.setApprovalEmail(clearanceDto.getHrEmail());
        hrApproval.setDepartment(hrUser.getDepartment());
        hrApproval.setName(hrUser.getName());

        Approval managerApproval = new Approval();
        managerApproval.setApprovalEmail(clearanceDto.getDirectManagerEmail());
        managerApproval.setDepartment(managerUser.getDepartment());
        managerApproval.setName(managerUser.getName());





        approvals.add(itApproval);
        approvals.add(accountsApproval);
        approvals.add(hrApproval);
        approvals.add(managerApproval);


        clearance.setName(employee.getName());
        clearance.setBadgeNumber(clearanceDto.getBadgeNumber());
        clearance.setCreatedAt(LocalDateTime.now());
        clearance.setCode(clearanceService.generateNextClearanceCode());
        clearance.setLastWorkingDate(clearanceDto.getLastWorkingDate());
        clearance.setArName(employee.getArName());
        clearance.setCreatedByUserEmail(currentUserEmail);
        clearance.setDepartment(employee.getDepartment());
        clearance.setJobTitle(employee.getJobTitle());
        clearance.setApprovals(approvals);

        clearanceRepository.save(clearance);

        return "redirect:/clearance";
    }
}
