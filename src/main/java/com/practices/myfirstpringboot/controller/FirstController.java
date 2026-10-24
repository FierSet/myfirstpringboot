package com.practices.myfirstpringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController 
{
    @GetMapping("/firstview")
    public String firstview() 
    {
        return "firstview";
    }
    
    @GetMapping("/secondview")
    public String secondview() 
    {
        return "secondview";
    }

}
