package ru.otus.library.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.library.domain.User;
import ru.otus.library.repository.UserRepository;

@Controller
public class AuthorizationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String main(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam String j_login, @RequestParam String j_password){
        userRepository.save(new User(j_login, j_password));
        return "loginPage";
    }
}
