package com.marublosso.worktrack.worktrack_backend.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontController {

    @GetMapping("")
    public String showWorkTrackPage(HttpServletRequest sessionRequest, Model model) {

        // 로그인 세션 체크
        HttpSession session = sessionRequest.getSession(false);
        Long id = null;

        if (session != null) {
            id = (Long) session.getAttribute("Id");
        }

        if (id == null) {
            return "redirect:/login";
        }

        // 유저 정보를 모델에 추가(메인화면 이름, 현장표시)
        model.addAttribute("genBa", (String) session.getAttribute("Dept"));
        model.addAttribute("userName", (String) session.getAttribute("Name"));

        return "3_mainpage";
    }

    @GetMapping("/calendar")
    public String calendarPage(HttpServletRequest sessionRequest) {
        // 로그인 세션 체크
        Long id = (Long) sessionRequest.getSession(false).getAttribute("Id");
        if (id == null) {
            return "redirect:/login";
        }
        return "4_calendarpage";

    }

    @GetMapping("/ichiran")
    public String ichiranPage(HttpServletRequest sessionRequest) {
        // 로그인 세션 체크
        Long id = (Long) sessionRequest.getSession(false).getAttribute("Id");
        if (id == null) {
            return "redirect:/login";
        }
        return "ichiran";
    }

    @GetMapping("/createAcount")
    public String signUpPage(HttpServletRequest sessionRequest) {

        return "1_1CreateAcount";
    }

    @GetMapping("/createPassword")
    public String setPasswordPage(HttpServletRequest sessionRequest) {

        return "1_1CreatPassword";
    }

    @GetMapping("/updateProfiles")
    public String setDeptPage(HttpServletRequest sessionRequest) {

        return "1_2UpdateProfiles";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "2_LogInpage";
    }

}
