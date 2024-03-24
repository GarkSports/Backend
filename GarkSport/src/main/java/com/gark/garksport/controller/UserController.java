package com.gark.garksport.controller;

import com.gark.garksport.modal.Manager;
import com.gark.garksport.modal.Staff;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('management:read')")
    @GetMapping("/hello")
        public String sayHello(){
            return "Hello from secured endpoint";
        }

    @PostMapping("/add-staff")
    public Staff addStaff(@RequestBody Staff staff){
        staff.setRole(Role.STAFF);
        staff.setRoleName(staff.getRoleName());
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        //staff.setPermissions(Collections.singleton(Permission.MANAGER_READ));
        staff.setPermissions(staff.getPermissions());

        return repository.save(staff);
    }
}
