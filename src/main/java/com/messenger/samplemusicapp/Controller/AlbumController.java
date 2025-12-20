package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import com.messenger.samplemusicapp.Services.AlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
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

    public AlbumController(AlbumService albumService, ObjectMapper mapper) {

        this.albumService = albumService;
        this.mapper = mapper;
    }

    @GetMapping("/all/albums")
    public Map<String,String> getAllAlbums() {
        logger.info(albumService.getAllAlbums().toString());
        return albumService.getAllAlbums();

    }
    @GetMapping("/albums/{title}/songs")
    public List<Song> getAllAlbumsSongs(@PathVariable String title) {
        logger.info(albumService.getSongsByAlbum(title).toString());
        return albumService.getSongsByAlbum(title);
    }


    @PostMapping
    public Album addAlbum(@RequestParam String title,
                          @RequestParam String artist,
                          @RequestParam String year,
                          @RequestParam String genre,
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
        albumService.createAlbum(album,songFiles,file);
        logger.info(album.toString());
        return album;



    }

}
