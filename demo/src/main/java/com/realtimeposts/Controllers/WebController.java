package com.realtimeposts.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/posts")
    public String posts() {
        return "index";
    }
}