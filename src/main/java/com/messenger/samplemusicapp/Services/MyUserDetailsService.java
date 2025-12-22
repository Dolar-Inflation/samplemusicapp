package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Config.MyUserDetails;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Account> accounts = accountRepository.findByUsername(username);

        return accounts.map(MyUserDetails::new).orElseThrow(()->new UsernameNotFoundException(username));
    }

}
