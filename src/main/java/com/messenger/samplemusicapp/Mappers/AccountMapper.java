package com.messenger.samplemusicapp.Mappers;

import com.messenger.samplemusicapp.DTO.AccountDTO;
import com.messenger.samplemusicapp.Entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO accountToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setCreatedAlbums(account.getCreatedAlbums());
        accountDTO.setCreatedSongs(account.getCreatedSongs());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setFavoriteAlbums(account.getFavoriteAlbums());
        accountDTO.setFavoriteSongs(account.getFavoriteSongs());
        accountDTO.setUsername(account.getUsername());
        return accountDTO;
    }



    public Account accountDTOToAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setCreatedAlbums(accountDTO.getCreatedAlbums());
        account.setCreatedSongs(accountDTO.getCreatedSongs());
        account.setPassword(accountDTO.getPassword());
        account.setFavoriteAlbums(accountDTO.getFavoriteAlbums());
        account.setFavoriteSongs(accountDTO.getFavoriteSongs());
        account.setUsername(accountDTO.getUsername());
        return account;
    }

}
