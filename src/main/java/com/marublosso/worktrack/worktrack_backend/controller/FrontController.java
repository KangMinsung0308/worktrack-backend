package com.marublosso.worktrack.worktrack_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/worktrack")
    public String showWorkTrackPage() {
        // templates 폴더 안에 있는 worktrack.html을 렌더링
        return "worktrack";
    }
}
