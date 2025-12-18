package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import com.messenger.samplemusicapp.Repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final UploadFileService uploadFileService;

    public SongService(SongRepository songRepository, AlbumRepository albumRepository, UploadFileService uploadFileService) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.uploadFileService = uploadFileService;
    }

    public void PostSong(Song song, Album album, MultipartFile file) throws IOException {

        if (album != null) {
            if (album.getId() == null) {

                album = albumRepository.save(album);
            } else {

                album = albumRepository.findById(album.getId())
                        .orElseThrow(() -> new IllegalArgumentException("error"));
            }
            song.setAlbum(album);
            if (song.getFileUrl() != null) {
                song.setFileUrl("/" + Paths.get(song.getFileUrl()).getFileName().toString());
            }
        }
        if (file != null) {
          String fileUrl =  uploadFileService.uploadFile(file);
          song.setFileUrl(fileUrl);
        }
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
