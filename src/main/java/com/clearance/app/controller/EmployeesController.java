package com.clearance.app.controller;

import com.clearance.app.dto.EmployeeDto;

import com.clearance.app.model.AppUser;
import com.clearance.app.model.Employee;
import com.clearance.app.model.Log;
import com.clearance.app.repository.EmployeeRepository;
import com.clearance.app.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class EmployeesController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    LogRepository logRepository;

    @GetMapping("/employees")
    public String employeesPage(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size)
    {
        Page<Employee> employeesPage ;


        try {

            if (keyword != null && !keyword.isEmpty())
            {
                System.out.println("User want to search for employee with keyword:"+ keyword);
                employeesPage = employeeRepository.findByKeyword(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "badgeNumber")));
            }
            else
            {
                System.out.println("User want to view employees no search");
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "badgeNumber"));
                employeesPage = employeeRepository.findAll(pageable);
            }
        } catch (Exception ex)
        {
            System.out.println("Likely collection [employees] does not exist yet");
            employeesPage = Page.empty(PageRequest.of(page, size));
        }

        model.addAttribute("employees", employeesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", employeesPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "employees";
    }


    @GetMapping("/add-new-employee")
    public String addNewUserPage(Model model, RedirectAttributes redirectAttributes)
    {
        // only current user with admin role can view add new user page

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can view add new employee !");
            return "redirect:/users";
        }


        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto", employeeDto);

        return "add-new-employee";
    }

    @PostMapping("/add-new-employee")
    public String addNewUser(@ModelAttribute EmployeeDto employeeDto, RedirectAttributes redirectAttributes)
    {

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can  add new employee !");
            return "redirect:/users";
        }


        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setArName(employeeDto.getArName());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setBadgeNumber(employeeDto.getBadgeNumber());
        employee.setEmail(employeeDto.getEmail());
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setLocation(employeeDto.getLocation());
        employeeRepository.save(employee);



        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("Add New Employee Name"+employee.getName() +" Badge Number:"+employee.getBadgeNumber());
        logRepository.save(log);


        return "redirect:/employees";
    }


    @PostMapping("/import-employees")
    public String importEmployees(@RequestParam("csv") MultipartFile csvFile, RedirectAttributes redirectAttributes) {

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can  import employees !");
            return "redirect:/users";
        }


        System.out.println("User wnat to import employees list with csv file");
        if (csvFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "CSV file is empty.");
            return "redirect:/employees";
        }

        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8)))
        {
            String headerLine = reader.readLine();

            headerLine = headerLine.replace("\uFEFF", ""); // Remove BOM if present

            if (headerLine == null) {
                redirectAttributes.addFlashAttribute("error", "CSV file is missing header.");
                return "redirect:/employees";
            }


            String[] headers = headerLine.split(",");
            String[] expectedHeaders = {
                    "badgeNumber", "name", "arabicName", "email", "department", "location", "jobTitle", "contractStart", "nationality"
            };

            for (int i = 0; i < expectedHeaders.length; i++) {
                if (headers.length <= i || !headers[i].trim().equalsIgnoreCase(expectedHeaders[i])) {
                    redirectAttributes.addFlashAttribute("error", "CSV file columns do not match expected format: [badgeNumber, name, arabicName, email, department, location, jobTitle, contractStart, nationality]");
                    return "redirect:/employees";
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] fields = line.split(",", -1); // -1 includes trailing empty strings

                if (fields.length < 9) continue; // skip malformed rows

                Employee employee = new Employee();
                employee.setBadgeNumber(fields[0].trim());
                employee.setName(fields[1].trim());
                employee.setArName(fields[2].trim());
                employee.setEmail(fields[3].trim());
                employee.setDepartment(fields[4].trim());
                employee.setLocation(fields[5].trim());
                employee.setJobTitle(fields[6].trim());
                // ------- contractStart date like 5/20/2002 to LocalDate fields[7] or maybe null or something different
                String contractStartRaw = fields[7].trim();
                try{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDate contractStart = LocalDate.parse(contractStartRaw, formatter);
                    employee.setContractStart(contractStart);
                }catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
                employee.setNationality(fields[8].trim());

                employees.add(employee);

            }
            System.out.println("Save All employees to Database");
            employeeRepository.saveAll(employees);


            Log log = new Log();
            log.setName(currentUser.getName());
            log.setEmail(currentUser.getEmail());
            log.setDate(LocalDateTime.now());
            log.setAction("Import Employees list from CSV File");
            logRepository.save(log);


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error importing CSV: " + e.getMessage());
        }

        return "redirect:/employees";
    }



}
