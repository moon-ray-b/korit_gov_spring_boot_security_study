package com.korit.security2_study.controller;

import com.korit.security2_study.dto.ModifyPasswordReqDto;
import com.korit.security2_study.security.model.Principal;
import com.korit.security2_study.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/principal")
    public ResponseEntity<?>getPrincipal(@AuthenticationPrincipal Principal principal){
        return ResponseEntity.ok(principal);
    }

    @PostMapping("/modify/password")
    public ResponseEntity<?> modifyPassword(@RequestBody ModifyPasswordReqDto modifyPasswordReqDto, @AuthenticationPrincipal Principal principal){
        return ResponseEntity.ok(accountService.modifyPassword(modifyPasswordReqDto,principal));
    }
}
