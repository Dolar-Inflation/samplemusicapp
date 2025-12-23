package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        return account;
}
public Account findByUsername(String username) {
        Account account = accountRepository.findByUsername(username).get();
        return account;
}


    public void save(Account account) {
        accountRepository.save(account);
    }

}
