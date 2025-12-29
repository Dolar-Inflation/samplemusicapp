package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.DTO.AccountDTO;
import com.messenger.samplemusicapp.DTO.SongDTO;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Mappers.AccountMapper;
import com.messenger.samplemusicapp.Services.AccountService;
import com.messenger.samplemusicapp.Services.AlbumService;
import com.messenger.samplemusicapp.Services.SongService;
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
    private final SongService songService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AlbumService albumService, SongService songService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.albumService = albumService;
        this.songService = songService;
        this.accountMapper = accountMapper;
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
    @PostMapping("/favorites/songs")
    public AccountDTO addFavoriteSongToAccount(Principal principal, @RequestParam Long songId){
        Account account = accountService.findByUsername(principal.getName());
        Song song = songService.getSongById(songId);
        if (!account.getFavoriteSongs().contains(song)) {
            account.getFavoriteSongs().add(song);
            accountService.save(account);
        }
        return accountMapper.accountToAccountDTO(account);
    }
    @GetMapping("/my/favorites/songs")
    public Set<SongDTO> getFavoriteSongs(Principal principal){
        return songService.getAllFavoriteSongs(principal);
    }
    @GetMapping("/my/favorites")
    public Set<Album> getFavoriteAlbums(Principal principal) {



        return albumService.getAllFavoriteAlbums(principal);
    }


}
