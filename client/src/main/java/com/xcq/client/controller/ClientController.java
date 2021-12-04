package com.xcq.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {

    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            list.add(String.valueOf(i) + "-test");
        }
        mv.addObject("list", list);
        mv.setViewName("/client.html");
        return mv;
    }

    @GetMapping("/test")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/client.html");
        return mv;
    }
}
