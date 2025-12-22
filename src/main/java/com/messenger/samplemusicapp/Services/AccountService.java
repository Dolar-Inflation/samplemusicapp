package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Repository.AccountRepository;
import org.springframework.stereotype.Service;

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


}
