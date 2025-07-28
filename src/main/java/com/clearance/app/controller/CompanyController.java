package com.clearance.app.controller;

import com.clearance.app.model.AppUser;
import com.clearance.app.model.Company;
import com.clearance.app.model.Log;
import com.clearance.app.repository.CompanyRepository;
import com.clearance.app.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class CompanyController {



    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    LogRepository logRepository;

    @GetMapping("/company")
    public String company(Model model)
    {
        Company company = companyRepository.findFirstBy();
        if(company ==null)
        {
            company = new Company();
        }
        model.addAttribute("company", company);
        return "company";
    }


    @PostMapping("/company")
    public String updateCompany(@RequestParam(name = "logo", required = false) MultipartFile logo, @RequestParam String name, @RequestParam String arName, @RequestParam String cr, RedirectAttributes redirectAttributes)
    {
        Company company = companyRepository.findFirstBy();
        if(company ==null)
        {
            company = new Company();
        }

        company.setName(name);
        company.setCr(cr);
        company.setArName(arName);

        if(logo != null && !logo.isEmpty())
        {
            String contentType = logo.getContentType();
            if (contentType != null && contentType.startsWith("image/"))
            {
                try {
                    String uploadDir = "uploads/company/";
                    Files.createDirectories(Paths.get(uploadDir));

                    String originalFilename = logo.getOriginalFilename();
                    String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
                    Path filePath = Paths.get(uploadDir + uniqueFileName);
                    logo.transferTo(filePath);
                    company.setLogo("/files/company/" + uniqueFileName);
                }
                catch (IOException e)
                {
                    redirectAttributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
                    return "redirect:/company";
                }
            }
            else{
                redirectAttributes.addFlashAttribute("error", " Logo File must be image only ! failed: ");
                return "redirect:/company";
            }
        }

        companyRepository.save(company);

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("update  Company");
        logRepository.save(log);

        return "redirect:/company";
    }


    @GetMapping("/files/company/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/company/").resolve(filename);
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
}
