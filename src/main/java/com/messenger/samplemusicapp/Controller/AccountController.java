package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/me")
    public Account getCurrentAccount(Principal principal) {
        return accountService.findByUsername(principal.getName()); }
}
