package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.DTO.AccountDTO;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Mappers.AccountMapper;
import com.messenger.samplemusicapp.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

public AccountDTO getAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        return accountMapper.accountToAccountDTO(account);
}
public Account findByUsername(String username) {
        Account account = accountRepository.findByUsername(username).get();
        return account;
}


    public void save(Account account) {
        accountRepository.save(account);
    }

}
