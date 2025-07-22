package com.clearance.app.controller;

import com.clearance.app.dto.EmployeeDto;

import com.clearance.app.model.Employee;
import com.clearance.app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeesController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public String employeesPage(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size)
    {
        Page<Employee> employeesPage ;


        try {

            if (keyword != null && !keyword.isEmpty()) {
                employeesPage = employeeRepository.findByKeyword(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "badgeNumber")));
            } else {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "badgeNumber"));
                employeesPage = employeeRepository.findAll(pageable);
            }
        } catch (Exception ex) {
            // Likely collection does not exist yet
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
    public String addNewUserPage(Model model)
    {
        // only current user with admin role can view add new user page
        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto", employeeDto);

        return "/add-new-employee";
    }

    @PostMapping("/add-new-employee")
    public String addNewUser(@ModelAttribute EmployeeDto employeeDto)
    {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setArName(employeeDto.getArName());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setBadgeNumber(employeeDto.getBadgeNumber());
        employee.setEmail(employeeDto.getEmail());
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setLocation(employeeDto.getLocation());
        employeeRepository.save(employee);

        return "redirect:/employees";
    }


}
