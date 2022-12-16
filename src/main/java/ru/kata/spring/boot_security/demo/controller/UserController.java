package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import java.security.Principal;
import java.util.List;

@Controller

public class UserController {
    UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping ("/admin")
    public String showAllUsers(Model model, Principal principal){
        List<User> allUsers = userServiceImpl.getAllUsers();
        model.addAttribute("currentUser",userServiceImpl.getUserByName(principal.getName()));
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        return "admin-panel";
    }


//    @GetMapping("/admin/addNewUser")
//    public String addNewUser(Model model){
//        //User user = new User();
//        model.addAttribute("user",new User());
//        return "user-info";
//    }
    @PostMapping("/admin")
    public String create(@ModelAttribute("newUser") User user,
                         @ModelAttribute("newRole") Role role) {

        userServiceImpl.saveNewUser(user, role.getRole());
        return "redirect:/admin";
    }

//    @PostMapping("admin/addNew")
//    public String saveUser(@ModelAttribute("user") User user){
//        userServiceImpl.saveUser(user);
//        return "redirect:/admin";
//    }


//    @GetMapping("/admin/updateInfo/{id}")
//    public String updateUser(Model model, @PathVariable("id") int id){
//        model.addAttribute("user", userServiceImpl.getUser(id));
//        return "user-update";
//    }


    @GetMapping("admin/get/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") int id) {
        return userServiceImpl.getUser(id);
    }


    @PostMapping ("/admin/updateUser")
    public String update(@ModelAttribute("myUserUpdate") User user, @ModelAttribute("roleUpdate") Role role) {
        userServiceImpl.updateUser(user, role.getRole());
        return "redirect:/admin";
    }

//    @DeleteMapping("/admin/deleteUser/{id}")
//    public String deleteUser(@PathVariable("id") int id) {
//        userServiceImpl.deleteUser(id);
//        return "redirect:/admin";
//    }
    @DeleteMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userServiceImpl.deleteUser(user.getId());
        return "redirect:/admin";
    }


    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("currentUser",userServiceImpl.getUserByName(principal.getName()));
        return "user-panel";
    }
    @GetMapping("/")
    public String showIndex(){
        return "index";
    }

}
