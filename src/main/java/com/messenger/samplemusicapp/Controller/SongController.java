package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Services.SongService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/all")
    public Map<String,String> allSongs() {
       return songService.getAllSongs();
    }

    /*@RequestBody Song song, Album album, @RequestParam MultipartFile file*/
    @PostMapping("/add")
    public Song addSong(@RequestParam("songname") String songname,
                        @RequestParam("artist") String artist,
                        Album album,
                        @RequestParam(value = "genre", required = false) String genre,
                        @RequestParam("file") MultipartFile file) throws IOException {


        Song song = new Song();
        song.setSongname(songname);
        song.setArtist(artist);
        song.setGenre(genre);


        songService.PostSong(song, album,file);
        return song;


    }





}
