package com.marublosso.worktrack.worktrack_backend.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontController {

    @GetMapping("/worktrack")
    public String showWorkTrackPage(HttpServletRequest sessionRequest, Model model) {

        // 로그인 세션 체크
        LoginUserDto loginUser = (LoginUserDto) sessionRequest.getSession(false).getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/worktrack/login";
        }

        model.addAttribute("genBa", loginUser.getDept());
        model.addAttribute("userName", loginUser.getUsername());
        return "3_mainpage"; // templates/worktrack.html
    }

    @GetMapping("/worktrack/ichiran")
    public String ichiranPage(HttpServletRequest sessionRequest) {
        // 로그인 세션 체크
        LoginUserDto loginUser = (LoginUserDto) sessionRequest.getSession(false).getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/worktrack/login";
        }
        return "ichiran";
    }

    @GetMapping("/worktrack/login")
    public String loginPage() {
        return "2_LogInpage";
    }

}
