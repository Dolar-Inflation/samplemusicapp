package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Services.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongService songService;
    private final Logger logger = LoggerFactory.getLogger(SongController.class);

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/all")
    public Map<String,String> allSongs() {
        logger.info(songService.getAllSongs().toString());
        return songService.getAllSongs();
    }

    /*@RequestBody Song song, Album album, @RequestParam MultipartFile file*/
//    @PostMapping("/add")
//    public Song addSong(@RequestParam("songname") String songname,
//                        @RequestParam("artist") String artist,
//                        @RequestParam Album album,
//                        @RequestParam(value = "genre", required = false) String genre,
//                        @RequestParam("file") MultipartFile file) throws IOException {
//
//
//        Song song = new Song();
//        song.setSongname(songname);
//        song.setArtist(artist);
//        song.setGenre(genre);
//        if(album != null) {
//            song.setAlbum(album);
//        }
//
//
//        songService.PostSong(song, album,file);
//        return song;
    @PostMapping("/add")
    public Song addSong(@RequestParam("songname") String songname,
                        @RequestParam("artist") String artist,
                        @RequestParam(value = "albumTitle", required = false) String albumTitle,
                        @RequestParam(value = "genre", required = false) String genre,
                        @RequestParam("file") MultipartFile file) throws IOException {

        logger.info(songService.addSong(songname,artist,albumTitle,genre,file).toString());
        return songService.addSong(songname, artist, albumTitle, genre, file);
    }








}
