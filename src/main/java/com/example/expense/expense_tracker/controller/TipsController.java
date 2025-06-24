package com.example.expense.expense_tracker.controller;

import com.example.expense.expense_tracker.service.MascotTipsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping(path = "api/tips")
public class TipsController {
    private final MascotTipsService mascotTipsService;

    public TipsController(MascotTipsService mascotTipsService) {
        this.mascotTipsService = mascotTipsService;
    }

    @GetMapping
    public List<String> getMascotTips(){
        return mascotTipsService.generateTipsForUser();
    }
}
