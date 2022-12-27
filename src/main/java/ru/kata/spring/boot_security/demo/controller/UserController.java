package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class UserController {
    @GetMapping("/user")
    public String userPage() {
        return "user-panel";
    }
    @GetMapping("/")
    public String showIndex(){
        return "index";
    }
}
