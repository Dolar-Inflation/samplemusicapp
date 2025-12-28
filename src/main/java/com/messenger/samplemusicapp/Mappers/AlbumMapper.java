package com.messenger.samplemusicapp.Mappers;

import com.messenger.samplemusicapp.DTO.AlbumDTO;
import com.messenger.samplemusicapp.Entity.Album;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AlbumMapper {

    private final SongMapper songMapper;

    public AlbumMapper(SongMapper songMapper) {
        this.songMapper = songMapper;
    }

    public AlbumDTO toAlbumDTO(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setId(album.getId());
        albumDTO.setAccountId(album.getAccount().getId());

        albumDTO.setTitle(album.getTitle());
        albumDTO.setArtist(album.getArtist());
        albumDTO.setGenre(album.getGenre());
        albumDTO.setYear(album.getYear());
        albumDTO.setImageurl(album.getImageurl());
        albumDTO.setSongs(album.getSongs()
                .stream()
                .map(songMapper::SongToDTO)
                .collect(Collectors.toList()));


        return albumDTO;

    }


    public Album toAlbum(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setId(albumDTO.getId());
        album.setTitle(albumDTO.getTitle());
        album.setArtist(albumDTO.getArtist());
        album.setGenre(albumDTO.getGenre());
        album.setYear(albumDTO.getYear());
        album.setImageurl(albumDTO.getImageurl());
        album.setSongs(albumDTO.getSongs()
                .stream()
                .map(songMapper::DTOToSong)
        .collect(Collectors.toList()));
        return album;

    }


}
