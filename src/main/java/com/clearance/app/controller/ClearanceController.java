package com.clearance.app.controller;



import com.clearance.app.config.AppConfig;
import com.clearance.app.dto.ClearanceDto;
import com.clearance.app.model.*;
import com.clearance.app.repository.*;
import com.clearance.app.service.ClearanceService;
import com.clearance.app.service.NotificationEmailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    NotificationEmailService notificationEmailService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    LogRepository logRepository;

    @GetMapping({"/", "/clearance"})
    public String clearance(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size)
    {
        Page<Clearance> clearancesPage;
        if(keyword !=null && !keyword.isEmpty())
        {

            clearancesPage = clearanceRepository.findByKeyword(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "code")));
        }
        else
        {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "code"));
            clearancesPage = clearanceRepository.findAll(pageable);
        }

        model.addAttribute("clearances", clearancesPage);
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

        AppUser itUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getItEmail()).orElse(null);
        if(itUser ==null)
        {
            redirectAttributes.addAttribute("error", "The IT User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser accountsUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getAccountsEmail()).orElse(null);
        if(accountsUser == null)
        {
            redirectAttributes.addAttribute("error", "The Accounts User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser hrUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getHrEmail()).orElse(null);
        if(hrUser == null)
        {
            redirectAttributes.addAttribute("error", "The HR User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser managerUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getDirectManagerEmail()).orElse(null);
        if(managerUser == null)
        {
            redirectAttributes.addAttribute("error", "The Direct Manager User not exist !");
            return "redirect:/add-new-clearance";
        }

        boolean isPurchasingApprovalReqiured = true;
        AppUser purchasingUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getPurchasingEmail()).orElse(null);
        if(purchasingUser == null)
        {
            // no mandatory approval so if no email we will skip this required approval
            isPurchasingApprovalReqiured = false;
        }

        boolean isCustomerServiceApprovalReqiurd = true;
        AppUser customerServiceUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getCustomerEmail()).orElse(null);
        if(customerServiceUser == null)
        {
            // no mandatory approval so if no email we will skip this required approval
            isCustomerServiceApprovalReqiurd = false;
        }

        boolean isSalesApprovalReqiurd = true;
        AppUser salesUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getSalesEmail()).orElse(null);
        if(salesUser ==null)
        {
            isSalesApprovalReqiurd = false;
        }

        AppUser mobileSIMUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getMobileEmail()).orElse(null);
        if(mobileSIMUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Mobile User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser companyVehicleUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getVehicleEmail()).orElse(null);
        if(companyVehicleUser == null)
        {
            redirectAttributes.addAttribute("error", "The Company Vehicle User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser securityUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getSecurityEmail()).orElse(null);
        if(securityUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Security User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser insuranceUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getInsuranceEmail()).orElse(null);
        if(insuranceUser ==null)
        {
            redirectAttributes.addAttribute("error", "The Insurance User not exist !");
            return "redirect:/add-new-clearance";
        }

        AppUser financeUser = appUserRepository.findByEmailIgnoreCase(clearanceDto.getFinanceEmail()).orElse(null);
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
        clearance.setLocation(employee.getLocation());
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

        String clearanceLink = appConfig.getBaseUrl() + "/clearance-view?code=" + clearance.getCode();



            notificationEmailService.sendApprovalEmail(approvals, clearance.getCode(), clearanceLink);

        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("Added New Clearance code:"+clearance.getCode());
        logRepository.save(log);


        return "redirect:/clearance";
    }


    @GetMapping("/clearance-view")
    public String clearanceView(@RequestParam String code, RedirectAttributes redirectAttributes, Model model)
    {
        Clearance clearance = clearanceRepository.findByCode(code).orElse(null);
        if(clearance ==null)
        {
            redirectAttributes.addAttribute("error", "The Clearance Code not Exist !");
            return "redirect:/clearance";
        }

        model.addAttribute("clearance", clearance);
        return "clearance-view";
    }

    // Add Comment to clearance
    @PostMapping("/comments-clearance")
    public String commentsClearance(@RequestParam String clearanceCode,
                                    @RequestParam String text,
                                    @RequestParam(name = "file", required = false)  MultipartFile file,
                                    RedirectAttributes redirectAttributes)
    {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Clearance clearance = clearanceRepository.findByCode(clearanceCode).orElse(null);
        if (clearance == null) {
            redirectAttributes.addFlashAttribute("error", "The Clearance Code does not exist!");
            return "redirect:/clearance";
        }

        // 🔐 Make sure comments list is not null
        List<Comment> comments = clearance.getComments();
        if (comments == null) {
            comments = new ArrayList<>();

        }

        Comment newComment = new Comment();

        // if there is file upload and set the filePath in newComment
        if(file != null && !file.isEmpty())
        {
            // Handle file upload
            // make sure the file is pdf or excel or word or image other not accepted
            String contentType = file.getContentType();
            if (contentType != null && (contentType.equals("application/pdf") || contentType.equals("application/msword") || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || contentType.equals("application/vnd.ms-excel") || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.startsWith("image/")))
            {
                try {
                    String uploadDir = "uploads/comments/";
                    Files.createDirectories(Paths.get(uploadDir));

                    String originalFilename = file.getOriginalFilename();
                    String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
                    Path filePath = Paths.get(uploadDir + uniqueFileName);
                    file.transferTo(filePath);
                    newComment.setFilePath("/files/comments/" + uniqueFileName);
                }
                catch (IOException e)
                {
                    redirectAttributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
                    return "redirect:/clearance-view?code=" + clearanceCode;
                }
            }
            else
            {
                redirectAttributes.addFlashAttribute("error", "Invalid file type. Only PDF, Word, Excel, and images are allowed.");
                return "redirect:/clearance-view?code=" + clearanceCode;
            }





        }
        //-----------------------------------------------------------


        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setEmail(currentUser.getEmail());
        newComment.setName(currentUser.getName());
        newComment.setText(text);

        comments.add(newComment);
        clearance.setComments(comments);
        clearanceRepository.save(clearance);



        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("add comment on  Clearance code:"+clearance.getCode());
        logRepository.save(log);

        return "redirect:/clearance-view?code=" + clearanceCode;
    }


    @PostMapping("/approve-clearance")
    public String approveClearance(@RequestParam String clearanceCode,
                                   @RequestParam String note,
                                   RedirectAttributes redirectAttributes) {

        Clearance clearance = clearanceRepository.findByCode(clearanceCode).orElse(null);
        if (clearance == null) {
            redirectAttributes.addFlashAttribute("error", "The Clearance Code does not exist!");
            return "redirect:/clearance";
        }

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = currentUser.getEmail();

        List<Approval> updatedApprovals = clearance.getApprovals();
        boolean updated = false;

        for (Approval approval : updatedApprovals) {
            if (approval.getApprovalEmail().equalsIgnoreCase(currentUserEmail)) {
                if (approval.isApproved()) {
                    redirectAttributes.addFlashAttribute("error", "You already approved this clearance.");
                    return "redirect:/clearance-view?code=" + clearanceCode;
                }

                approval.setNote(note);
                approval.setApproved(true);
                approval.setApprovedAt(LocalDateTime.now());
                updated = true;
                break;
            }
        }

        if (!updated) {
            redirectAttributes.addFlashAttribute("error", "You are not an assigned approver for this clearance.");
            return "redirect:/clearance-view?code=" + clearanceCode;
        }

        // 🔐 This line ensures MongoDB will persist the nested list change
        clearance.setApprovals(updatedApprovals);

        clearanceRepository.save(clearance);
        redirectAttributes.addFlashAttribute("success", "Your approval has been recorded.");


        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("approved for  Clearance code:"+clearance.getCode());
        logRepository.save(log);


        return "redirect:/clearance-view?code=" + clearanceCode;
    }


    @GetMapping("/files/comments/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/comments/").resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) throw new FileNotFoundException("File not found");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/print-clearance")
    public String printClearance(Model model, @RequestParam String clearanceCode, RedirectAttributes redirectAttributes)
    {
        Clearance clearance = clearanceRepository.findByCode(clearanceCode).orElse(null);
        if (clearance == null) {
            redirectAttributes.addFlashAttribute("error", "The Clearance Code does not exist!");
            return "redirect:/clearance";
        }

        Company company = companyRepository.findFirstBy();
        if(company ==null)
        {
            company = new Company();
        }


        model.addAttribute("clearance", clearance);
        model.addAttribute("company", company);
        return "print-clearance";
    }

}
