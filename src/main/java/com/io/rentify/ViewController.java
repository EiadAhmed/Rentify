package com.io.rentify;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")

    public String login() {
        return "custom_login";
    }

    @GetMapping("/chatpage")
    public String handelChat() {
        return "chatpage";
    }

    @GetMapping("/chatpage2")
    public String handelChat2() {
        return "chatpage2";
    }
}
