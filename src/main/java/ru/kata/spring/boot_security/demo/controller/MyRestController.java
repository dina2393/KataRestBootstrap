package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.RoleDTO;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class MyRestController {
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    public MyRestController(ModelMapper modelMapper,UserService userService){
        this.modelMapper = modelMapper;
        this.userService = userService;
    }
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers().stream().map(this::userConvertToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        User newUser = userDTOConvertToUser(userDTO);
        userService.saveNewUser(newUser);
        return new ResponseEntity<>(userConvertToUserDTO(newUser), HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") int id) {
        User updateUser = userDTOConvertToUser(userDTO);
        userService.updateUser(updateUser, id);
        return new ResponseEntity<>(userConvertToUserDTO(updateUser), HttpStatus.OK);
    }
//    Потом попробовать по-другому
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
//        return ResponseEntity.ok(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userConvertToUserDTO(user), HttpStatus.OK);
        }
    }
    @GetMapping("/roles")
    public ResponseEntity<Set<RoleDTO>> getAllRoles() {
        Set<RoleDTO> roleDTOSet= userService.getAllRoles().stream().map(this::roleConvertToRoleDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(roleDTOSet, HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<User> showUserInfo(Principal principal) {
        User user = userService.getUserByName(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);

    }


//convert to DTO and VS
    public User userDTOConvertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO,User.class);
    }
    public UserDTO userConvertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    public Role roleDTOConvertToRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }
    public RoleDTO roleConvertToRoleDTO(Role role) {
        return modelMapper.map(role,RoleDTO.class);
    }


}
