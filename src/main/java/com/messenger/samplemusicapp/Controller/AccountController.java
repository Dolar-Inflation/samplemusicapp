package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Services.AccountService;
import com.messenger.samplemusicapp.Services.AlbumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AlbumService albumService;

    public AccountController(AccountService accountService, AlbumService albumService) {
        this.accountService = accountService;
        this.albumService = albumService;
    }


    @GetMapping("/me")
    public Account getCurrentAccount(Principal principal) {
        return accountService.findByUsername(principal.getName());
    }

    @PostMapping("/favorites")
    public Account addFavoriteAlbumToAccount(Principal principal ,
                                             @RequestParam Long albumId)
    {
        Account account = accountService.findByUsername(principal.getName());
        Album album = albumService.findAlbumById(albumId);


        if (!account.getFavoriteAlbums().contains(album)) {
            account.getFavoriteAlbums().add(album);

            accountService.save(account);
        }
        return account;
    }
    @GetMapping("/my/favorites")
    public Set<Album> getFavoriteAlbums(Principal principal) {



        return albumService.getAllFavoriteAlbums(principal);
    }


}
