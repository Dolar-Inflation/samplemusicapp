package com.messenger.samplemusicapp.Controller;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Services.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/all")
    public List<Song> allSongs() {
       return songService.getAllSongs();
    }


    @PostMapping("/add")
    public Song addSong(@RequestBody Song song, Album album) {

        songService.PostSong(song, album);
        return song;


    }





}
