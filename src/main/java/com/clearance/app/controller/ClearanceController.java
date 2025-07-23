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

        boolean isPurchasingApprovalReqiured = true;
        AppUser purchasingUser = appUserRepository.findByEmail(clearanceDto.getPurchasingEmail()).orElse(null);
        if(purchasingUser == null)
        {
            // no mandatory approval so if no email we will skip this required approval
            isPurchasingApprovalReqiured = false;
        }

        boolean isCustomerServiceApprovalReqiurd = true;
        AppUser customerServiceUser = appUserRepository.findByEmail(clearanceDto.getCustomerEmail()).orElse(null);
        if(customerServiceUser == null)
        {
            // no mandatory approval so if no email we will skip this required approval
            isCustomerServiceApprovalReqiurd = false;
        }

        boolean isSalesApprovalReqiurd = true;
        AppUser salesUser = appUserRepository.findByEmail(clearanceDto.getSalesEmail()).orElse(null);
        if(salesUser ==null)
        {
            isSalesApprovalReqiurd = false;
        }

        AppUser mobileSIMUser = appUserRepository.findByEmail(clearanceDto.getMobileEmail()).orElse(null);
        if(mobileSIMUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Mobile User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser companyVehicleUser = appUserRepository.findByEmail(clearanceDto.getVehicleEmail()).orElse(null);
        if(companyVehicleUser == null)
        {
            redirectAttributes.addAttribute("error", "The Company Vehicle User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser securityUser = appUserRepository.findByEmail(clearanceDto.getSecurityEmail()).orElse(null);
        if(securityUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Security User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser insuranceUser = appUserRepository.findByEmail(clearanceDto.getInsuranceEmail()).orElse(null);
        if(insuranceUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Insurance User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser financeUser = appUserRepository.findByEmail(clearanceDto.getFinanceEmail()).orElse(null);
        if(financeUser == null)
        {
            redirectAttributes.addAttribute("error", "The Finance User not exist !");
            return "redirect:/add-new-clearance";
        }





        List<Approval> approvals = new ArrayList<>();

        if(isCustomerServiceApprovalReqiurd)
        {
            Approval customerApproval = new Approval();
            customerApproval.setDepartment(customerServiceUser.getDepartment());
            customerApproval.setName(customerServiceUser.getName());
            customerApproval.setApprovalEmail(clearanceDto.getCustomerEmail());
            approvals.add(customerApproval);
        }

        if(isPurchasingApprovalReqiured)
        {
            Approval purchasingApproval = new Approval();
            purchasingApproval.setApprovalEmail(clearanceDto.getPurchasingEmail());
            purchasingApproval.setName(purchasingUser.getName());
            purchasingApproval.setDepartment(purchasingUser.getDepartment());
            approvals.add(purchasingApproval);
        }

        if(isSalesApprovalReqiurd)
        {
            Approval salesApproval = new Approval();
            salesApproval.setApprovalEmail(clearanceDto.getSalesEmail());
            salesApproval.setName(salesUser.getName());
            salesApproval.setDepartment(salesUser.getDepartment());
            approvals.add(salesApproval);
        }

        Approval financeApproval = new Approval();
        financeApproval.setApprovalEmail(clearanceDto.getFinanceEmail());
        financeApproval.setName(financeUser.getName());
        financeApproval.setDepartment(financeUser.getDepartment());


        Approval insuranceApproval = new Approval();
        insuranceApproval.setApprovalEmail(clearanceDto.getInsuranceEmail());
        insuranceApproval.setName(insuranceUser.getName());
        insuranceApproval.setDepartment(insuranceUser.getDepartment());

        Approval securityApproval = new Approval();
        securityApproval.setApprovalEmail(clearanceDto.getSecurityEmail());
        securityApproval.setName(securityUser.getName());
        securityApproval.setDepartment(securityUser.getDepartment());

        Approval companyVehicleApproval = new Approval();
        companyVehicleApproval.setApprovalEmail(clearanceDto.getVehicleEmail());
        companyVehicleApproval.setName(companyVehicleUser.getName());
        companyVehicleApproval.setDepartment(companyVehicleUser.getDepartment());

        Approval mobileSIMApproval = new Approval();
        mobileSIMApproval.setApprovalEmail(clearanceDto.getMobileEmail());
        mobileSIMApproval.setName(mobileSIMUser.getName());
        mobileSIMApproval.setDepartment(mobileSIMUser.getDepartment());



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
        approvals.add(mobileSIMApproval);
        approvals.add(companyVehicleApproval);
        approvals.add(securityApproval);
        approvals.add(insuranceApproval);
        approvals.add(financeApproval);



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
