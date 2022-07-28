package com.register.controller;

import com.register.entity.User;
import com.register.service.CheckSession;
import com.register.service.EmailChecking;
import com.register.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailChecking emailChecking;

    @Autowired
    private CheckSession checkSession;

    @GetMapping("/registr")
    public String registrationPage(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }
    @PostMapping("/registr")
    public String register(User user){
        Optional<User> user1=emailChecking.checkEmail(user);
        if (user1.isPresent()){
            return "redirect:registr?eyni";
        }
        userService.save(user);
        return "redirect:registr?success";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @PostMapping("/login")
    public String loginToWebsite(@RequestParam String email, @RequestParam String password, Model model,
                                 HttpServletRequest httpServletRequest){
        Optional<User>user=userService.login(email, password);
        if (!user.isPresent()){
            model.addAttribute("error","Bele istifadeci movcud deyil");
            return "login";
        }
        httpServletRequest.getSession().setAttribute("istifadeci",user.get());
        return "welcome";
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest httpServletRequest){
       User user=checkSession.sessionChecking(httpServletRequest);
       if (user==null){
           return "login";
       }
        return "welcome";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest,Model model){
        httpServletRequest.getSession().invalidate();
        model.addAttribute("logout","Siz ugurla sistemden cixis elediniz!");
        return "login";
    }
}
