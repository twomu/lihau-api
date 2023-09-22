package com.lhj.controller;

import com.lhj.model.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author lihua
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @PostMapping
    public String getNameByPost(@RequestBody User user){
        return "你请求的名字为："+user.getUserName();
    }

    @GetMapping
    public String getNameByGet(@RequestParam String name){
        return "你请求的名字为："+name;
    }

    @PostMapping("/user")
    public User getUser(@RequestBody User user){
        return user;
    }
}
