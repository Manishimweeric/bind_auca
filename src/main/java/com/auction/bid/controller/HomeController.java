package com.auction.bid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "homepage"; // This should correspond to an HTML file in your templates directory
    }
}
