package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import com.messenger.samplemusicapp.Repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public SongService(SongRepository songRepository, AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    public void PostSong(Song song, Album album) {

        if (album != null) {
            if (album.getId() == null) {
                // альбом новый → сначала сохраняем
                album = albumRepository.save(album);
            } else {
                // альбом уже есть → подтягиваем из базы
                album = albumRepository.findById(album.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Album not found"));
            }
            song.setAlbum(album);
        }
        // если album == null → просто сохраняем песню без альбома
        songRepository.save(song);

    }
    public void FindAlbumBySong(Song song, Album album) {
        if (album != null) {
            System.out.println(song.getAlbum());
            System.out.println(song.getAlbum().getTitle());
        }
        else throw new NullPointerException("Album is null"); {

        }
    }

    public List<Song> getAllSongs() {

        return songRepository.findAll();
    }


    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }


    public Song deleteSongById(Long id) {
        Song song = songRepository.findById(id).orElse(null);
        songRepository.delete(song);
        return song;
    }

}
