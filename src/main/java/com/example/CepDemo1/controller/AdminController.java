package com.example.CepDemo1.controller;

import com.example.CepDemo1.model.AdminModel;
import com.example.CepDemo1.model.UserModel;
import com.example.CepDemo1.repo.AdminRepo;
import com.example.CepDemo1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @GetMapping("/{adminId}")
    public AdminModel getById(@PathVariable Long adminId){
        return adminService.getAdminById(adminId);
    }

    @GetMapping("/members")
    public Set<UserModel> getMembers(){
        return adminService.getMembers();
    }
}
