package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;
import java.util.List;

@Controller

public class AdminController {
    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping ("/admin")
    public String showAllUsers(Model model, Principal principal){
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("currentUser",userService.getUserByName(principal.getName()));
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        return "admin-panel";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("newUser") User user,
                         @ModelAttribute("newRole") Role role) {

        userService.saveNewUser(user, role.getRole());
        return "redirect:/admin";
    }


    @GetMapping("admin/get/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }


    @PutMapping ("/admin/updateUser")
    public String update(@ModelAttribute("myUserUpdate") User user, @ModelAttribute("roleUpdate") Role role) {
        userService.updateUser(user, role.getRole());
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

}
