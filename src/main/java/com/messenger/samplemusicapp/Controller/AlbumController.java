package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.DTO.AlbumDTO;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Records.AlbumInfo;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import com.messenger.samplemusicapp.Services.AccountService;
import com.messenger.samplemusicapp.Services.AlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AlbumController {
    private final AlbumService albumService;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(AlbumController.class);
    private final AccountService accountService;

    public AlbumController(AlbumService albumService, ObjectMapper mapper, AccountService accountService) {

        this.albumService = albumService;
        this.mapper = mapper;
        this.accountService = accountService;
    }

    @GetMapping("/all/albums")
    public Map<String, AlbumInfo> getAllAlbums() {
        logger.info(albumService.getAllAlbums().toString());
        return albumService.getAllAlbums();

    }//мб map(album,List<Song>)?
    @GetMapping("/albums/{title}/songs")
    public List<Song> getAllAlbumsSongs(@PathVariable String title) {
        logger.info(albumService.getSongsByAlbum(title).toString());
        return albumService.getSongsByAlbum(title);
    }


    @PostMapping
    public AlbumDTO addAlbum(@RequestParam String title,
                             @RequestParam String artist,
                             @RequestParam String year,
                             @RequestParam String genre,
                             Principal principal,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam List<MultipartFile> songFiles

    ) throws IOException {

        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        album.setYear(year);
        album.setGenre(genre);
//        List<Song> songs = new ArrayList<>();
//        if (songsJson != null && !songsJson.isBlank()) {
//
//            songs = Arrays.asList(mapper.readValue(songsJson, Song[].class));
//        }


        Account account = accountService.findByUsername(principal.getName());

        logger.info(album.toString());
        return albumService.createAlbum(album,songFiles,account,file);/*album;*/



    }


}
