package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Config.MyUserDetails;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/") public String home() {
        return "redirect:/html/auth.html";
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account, HttpServletRequest request) {

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);


        Authentication auth = new UsernamePasswordAuthenticationToken(
                new MyUserDetails(account), null, new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());


        Account response = new Account();
        response.setUsername(account.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account, HttpServletRequest request) {
        Account dbAccount = accountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!passwordEncoder.matches(account.getPassword(), dbAccount.getPassword())) {
            Account errorResponse = new Account();
            errorResponse.setUsername("ERROR");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        MyUserDetails userDetails = new MyUserDetails(dbAccount);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());


        Account response = new Account();
        response.setUsername(dbAccount.getUsername());
        return ResponseEntity.ok(response);
    }
}
