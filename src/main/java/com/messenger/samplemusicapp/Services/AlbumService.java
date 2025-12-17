package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Album CreateAlbum(Album album,List<Song> songs) {

        Album newAlbum = new Album();
        newAlbum.getSongs().addAll(songs);
        newAlbum.setArtist(album.getArtist());
        newAlbum.setTitle(album.getTitle());
        return albumRepository.save(newAlbum);


    }
    public Album DeleteAlbumById(Long id) {
        Album album = albumRepository.findById(id).get();
        albumRepository.delete(album);
        return album;
    }
}
