package com.marublosso.worktrack.worktrack_backend.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.marublosso.worktrack.worktrack_backend.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontController {

    @GetMapping("/worktrack")
    public String showWorkTrackPage(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/worktrack/login";
        }
        return "worktrack"; // templates/worktrack.html
    }

    @GetMapping("/worktrack/login")
    public String loginPage() {
        return "login";
    }

}
