package com.paul.artifacts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("serverStatus", "ONLINE");
    model.addAttribute("playersOnline", 1337);
    model.addAttribute("monstersSlain", 42069);
    return "index";
  }
}
