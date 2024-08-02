package com.wanted.needswork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WebUpController {
    @GetMapping("/employer/reg")
    public String getEmployerReg() {
        return "reg_employer";

    }
    @GetMapping("/employer/lk")
    public String getEmployerLk() {
        return "lk_employer";
    }
}
