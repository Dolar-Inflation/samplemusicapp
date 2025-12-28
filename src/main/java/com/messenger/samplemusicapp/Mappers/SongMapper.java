package com.messenger.samplemusicapp.Mappers;

import com.messenger.samplemusicapp.DTO.SongDTO;
import com.messenger.samplemusicapp.Entity.Song;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {

    public SongDTO SongToDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        songDTO.setId(song.getId());

        songDTO.setArtist(song.getArtist());
        songDTO.setGenre(song.getGenre());
        songDTO.setDurability(song.getDurability());
        songDTO.setFileUrl(song.getFileUrl());
        songDTO.setSongname(song.getSongname());

        return songDTO;
    }

    public Song DTOToSong(SongDTO songDTO) {
        Song song = new Song();
        song.setId(songDTO.getId());
        song.setSongname(songDTO.getSongname());
        song.setArtist(songDTO.getArtist());
        song.setGenre(songDTO.getGenre());
        song.setDurability(songDTO.getDurability());
        song.setFileUrl(songDTO.getFileUrl());
        return song;

    }

}
