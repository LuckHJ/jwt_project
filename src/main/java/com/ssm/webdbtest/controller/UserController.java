package com.ssm.webdbtest.controller;
import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/CreatUser")//http://localhost:8080/api/users/CreatUser
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping("/FindAllUser")//http://localhost:8080/api/users/FindAllUser
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/FindUser/{id}")//http://localhost:8080/api/users/FindUser/
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/UpdateUser/{id}")//http://localhost:8080/api/users/UpdateUser/
    public void updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        userDetails.setId(id);
        userService.updateUser(userDetails);
    }

    @DeleteMapping("/DeleteUser/{id}")//http://localhost:8080/api/users/DeleteUser/
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
