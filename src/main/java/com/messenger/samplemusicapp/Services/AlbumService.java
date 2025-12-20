package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UploadFileService uploadFileService;
    public AlbumService(AlbumRepository albumRepository, UploadFileService uploadFileService) {
        this.albumRepository = albumRepository;
        this.uploadFileService = uploadFileService;
    }

    public Map<String,String> getAllAlbums() {

        return albumRepository.findAll()
                .stream()
                .filter(a -> a.getTitle() != null && a.getImageurl() != null)
                .collect(Collectors.toMap(Album::getTitle, Album::getImageurl));
    }

    public Album createAlbum(Album album, List<MultipartFile> songFiles, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {

            String savedFileName = uploadFileService.uploadImage(image);
            album.setImageurl( savedFileName);
        } else {

            album.setImageurl("/resources/default.png");
        }
        if (songFiles != null && !songFiles.isEmpty()) {
            for (MultipartFile file : songFiles) {
                String songUrl = uploadFileService.uploadFile(file);
                Song song = new Song();
                song.setSongname(file.getOriginalFilename());
                song.setArtist(album.getArtist());
                song.setGenre(album.getGenre());
                song.setFileUrl(songUrl);
                song.setAlbum(album);
                album.getSongs().add(song);
            }
        }


        return albumRepository.save(album);
    }
    public Album DeleteAlbumById(Long id) {
        Album album = albumRepository.findById(id).get();
        albumRepository.delete(album);
        return album;
    }
    public List<Song> getSongsByAlbum(String title) {
        return albumRepository.findByTitle(title)
                .map(Album::getSongs)
                .orElse(List.of());
    }

    public void addSongToAlbum(Album album,Song song) {
        album.getSongs().add(song);
        albumRepository.save(album);
    }
    public void removeSongFromAlbum(Album album,Song song) {
        album.getSongs().remove(song);
        albumRepository.save(album);
    }
}
