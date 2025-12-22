package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Services.AccountService;
import com.messenger.samplemusicapp.Services.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongService songService;
    private final Logger logger = LoggerFactory.getLogger(SongController.class);
    private final AccountService accountService;

    public SongController(SongService songService, AccountService accountService) {
        this.songService = songService;
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public Map<String,String> allSongs() {
        logger.info(songService.getAllSongs().toString());
        return songService.getAllSongs();
    }

    @PostMapping("/add")
    public Song addSong(@RequestParam("songname") String songname,
                        @RequestParam("artist") String artist,
                        Principal principal,
                        @RequestParam(value = "albumTitle", required = false) String albumTitle,
                        @RequestParam(value = "genre", required = false) String genre,
                        @RequestParam("file") MultipartFile file) throws IOException {

       Account account=accountService.findByUsername(principal.getName());
        logger.info(songService.addSong(songname,artist,albumTitle,genre,account,file).toString());
        return songService.addSong(songname, artist, albumTitle, genre,account ,file);
    }
}
