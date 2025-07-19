package com.clearance.app.controller;

import com.clearance.app.model.AppUser;
import com.clearance.app.repository.AppUserRepository;
import com.clearance.app.service.EmailService;
import com.clearance.app.service.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class LoginController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;


    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }


  //i use custom-login because post to login works in SecurityConfig file
    @PostMapping("/custom-login")
    public String handleLogin(@RequestParam("email") String email, Model model)
    {
        System.out.println("post login for email: "+email);

        boolean isFirstUser = appUserRepository.count() == 0;
        AppUser user = appUserRepository.findByEmail(email).orElse(null);

        if (!isFirstUser && user == null) {
            model.addAttribute("error", "Email not registered");
            System.out.println("Email not registered");
            return "login";
        }

        if(isFirstUser)
        {
            System.out.println("this is First User .....");
        }



        String otp = otpService.generateOtpForUser(email);

        int result = emailService.emailService(email, otp);

        if(result ==200)
        {

            return "redirect:/verify-otp?email=" + email;
        }
        else
        {
            System.out.println("the otp not sent ! for email:" + result);
            model.addAttribute("error", "otp not sent try again");
            return "login";
        }

    }

    @GetMapping("/logout")
    public String logoutPage(Model model, HttpSession session)
    {
        //clear the session
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        return "logout";
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam("email") String email, Model model)
    {
        model.addAttribute("email", email);
        return "verify-otp";
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("email") String email,@RequestParam("otp") String otp,Model model, HttpSession session)
    {
        boolean isFirstUser = appUserRepository.count() == 0;
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        if (user == null && !isFirstUser) {
            model.addAttribute("error", "User not found");
            return "login";
        }
        //if verified return to clearance or the original request
        boolean isValid = otpService.verifyOtp(email, otp);
        if (!isValid) {
            model.addAttribute("error", "Invalid OTP");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        if(isFirstUser)
        {
            user = new AppUser();
            user.setName("Administrator");
            user.setEmail(email);
            user.setRole("ADMIN");
            appUserRepository.save(user);
        }

        // ✅ Authenticate the user manually
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        // ✅ store authentication in session explicitly
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());



        session.setAttribute("user", user);

        model.addAttribute("pageTitle", "Clearance Page");

        return "redirect:/clearance";
    }


}
