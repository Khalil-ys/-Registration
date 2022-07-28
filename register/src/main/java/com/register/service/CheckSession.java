package com.register.service;

import com.register.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CheckSession {
    public User sessionChecking(HttpServletRequest httpServletRequest){
        User user=(User) httpServletRequest.getSession().getAttribute("istifadeci");
        if (user==null){
            return null;
        }
        return user;
    }
}
