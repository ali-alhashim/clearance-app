package com.clearance.app.controller;


import com.clearance.app.dto.UserDto;
import com.clearance.app.model.AppUser;
import com.clearance.app.model.Log;
import com.clearance.app.repository.AppUserRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class UsersController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    LogRepository logRepository;

    @GetMapping("/users")
    public String usersPage(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size)
    {
        Page<AppUser> usersPage;
        if(keyword !=null && !keyword.isEmpty())
        {

            usersPage = appUserRepository.findByKeyword(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));
        }
        else
        {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name"));
            usersPage = appUserRepository.findAll(pageable);
        }

        model.addAttribute("users", usersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", usersPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "users";
    }

    @GetMapping("/add-new-user")
    public String addNewUserPage(Model model, RedirectAttributes redirectAttributes)
    {
        // only current user with admin role can view add new user page
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can add user !");
            return "redirect:/users";
        }
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);

        return "add-new-user";
    }

    @PostMapping("/add-new-user")
    public String addNewUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes)
    {
        // only current user with admin role can add new user

        AppUser existEmail = appUserRepository.findByEmailIgnoreCase(userDto.getEmail()).orElse(null);
        if(existEmail !=null)
        {
            redirectAttributes.addAttribute("error", "The Email Already Exist !");
            return "redirect:/add-new-user";
        }

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can add user !");
            return "redirect:/users";
        }


        AppUser newUser = new AppUser();
        newUser.setName(userDto.getName());
        newUser.setDepartment(userDto.getDepartment());
        if(userDto.isManager())
        {
            newUser.setManager(true);
        }

        newUser.setEmail(userDto.getEmail());
        newUser.setRole(userDto.getRole());
        appUserRepository.save(newUser);



        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("Add New User Name:"+newUser.getName() +" email:"+newUser.getEmail());
        logRepository.save(log);


        return "redirect:/users";
    }

    //update or delete user
    @GetMapping("/user")
    public String userPage(@RequestParam String email, @ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes)
    {

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can view user !");
            return "redirect:/users";
        }


        if(email.isEmpty())
        {
            redirectAttributes.addAttribute("error", "User Email is Required !");
            return "redirect:/users";
        }

        AppUser user = appUserRepository.findByEmailIgnoreCase(email).orElse(null);

        if(user ==null)
        {
            redirectAttributes.addAttribute("error", "User Email Not Exist !");
            return "redirect:/users";
        }

        userDto.setManager(user.isManager());
        userDto.setEmail(user.getEmail());
        userDto.setDepartment(user.getDepartment());
        userDto.setRole(user.getRole());
        userDto.setName(user.getName());
        return "edit-user";
    }

    @PostMapping("/edit-user")
    public String updateUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes)
    {

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can edit user !");
            return "redirect:/users";
        }

        AppUser user = appUserRepository.findByEmailIgnoreCase(userDto.getEmail()).orElse(null);

        if(user ==null)
        {
            redirectAttributes.addAttribute("error", "User Email Not Exist !");
            return "redirect:/users";
        }

        user.setDepartment(userDto.getDepartment());
        user.setName(userDto.getName());


        user.setManager(userDto.isManager());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        appUserRepository.save(user);

        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("update  User Name:"+user.getName() +" email:"+user.getEmail());
        logRepository.save(log);



        return "redirect:/users";
    }

    @GetMapping("/user-delete")
    public String deleteUser(@RequestParam String email, RedirectAttributes redirectAttributes)
    {

        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!Objects.equals(currentUser.getRole(), "ADMIN"))
        {
            redirectAttributes.addAttribute("error", "only admin can delete user !");
            return "redirect:/users";
        }


        if(email.isEmpty())
        {
            redirectAttributes.addAttribute("error", "User Email is Required !");
            return "redirect:/users";
        }

        AppUser user = appUserRepository.findByEmailIgnoreCase(email).orElse(null);

        if(user ==null)
        {
            redirectAttributes.addAttribute("error", "User Email Not Exist !");
            return "redirect:/users";
        }

        appUserRepository.delete(user);


        Log log = new Log();
        log.setName(currentUser.getName());
        log.setEmail(currentUser.getEmail());
        log.setDate(LocalDateTime.now());
        log.setAction("Delete User Name:"+user.getName() +" email:"+user.getEmail());
        logRepository.save(log);

        return "redirect:/users";
    }
}
