package com.marublosso.worktrack.worktrack_backend.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontController {

    @GetMapping("")
    public String showWorkTrackPage(HttpServletRequest sessionRequest, Model model) {

        // 로그인 세션 체크
        HttpSession session = sessionRequest.getSession(false);
        LoginUserDto loginUser = null;

        if (session != null) {
            loginUser = (LoginUserDto) session.getAttribute("loginUser");
        }

        if (loginUser == null) {
            return "redirect:/login";
        }

        // 유저 정보를 모델에 추가(메인화면 이름, 현장표시)
        model.addAttribute("genBa", loginUser.getDept());
        model.addAttribute("userName", loginUser.getUsername());

        return "3_mainpage";
    }

    @GetMapping("/calendar")
    public String calendarPage(HttpServletRequest sessionRequest) {
        // 로그인 세션 체크
        LoginUserDto loginUser = (LoginUserDto) sessionRequest.getSession(false).getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "4_calendarpage";

    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpServletRequest sessionRequest) {
        // 로그인 세션 체크
        LoginUserDto loginUser = (LoginUserDto) sessionRequest.getSession(false).getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "5_dashboard";
    }

    @GetMapping("/settings")
    public String settingsPage(HttpServletRequest sessionRequest, Model model) {
        // 로그인 세션 체크
        LoginUserDto loginUser = (LoginUserDto) sessionRequest.getSession(false).getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 유저 정보를 모델에 추가(메인화면 이름, 현장표시)
        model.addAttribute("email", loginUser.getEmail());
        model.addAttribute("userName", loginUser.getUsername());
        return "6_setting";
    }

    @GetMapping("/createAcount")
    public String signUpPage(HttpServletRequest sessionRequest) {

        return "1_1CreateAcount";
    }

    @GetMapping("/createPassword")
    public String setPasswordPage(HttpServletRequest sessionRequest) {

        return "1_1CreatPassword";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "2_LogInpage";
    }

}
