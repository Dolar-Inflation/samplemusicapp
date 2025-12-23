package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Records.AlbumInfo;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UploadFileService uploadFileService;
    private final AccountService accountService;
    public AlbumService(AlbumRepository albumRepository, UploadFileService uploadFileService, AccountService accountService) {
        this.albumRepository = albumRepository;
        this.uploadFileService = uploadFileService;
        this.accountService = accountService;
    }

    public Map<String, AlbumInfo> getAllAlbums() {

        return albumRepository.findAll()
                .stream()
                .filter(a -> a.getTitle() != null && a.getImageurl() != null )
                .collect(Collectors.toMap(
                        Album::getTitle,
                       a->new AlbumInfo(a.getImageurl(),
                               a.getAccount() != null ? a.getAccount().getUsername() : "неизвестно",a.getId())));
    }

    public Album createAlbum(Album album, List<MultipartFile> songFiles, Account account, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            album.setAccount(account);
            String savedFileName = uploadFileService.uploadImage(image);
            album.setImageurl( savedFileName);
        } else {
            album.setAccount(account);
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
    public Album findAlbumById(Long id) {
        return albumRepository.findById(id).get();
    }

    public void addSongToAlbum(Album album,Song song) {
        album.getSongs().add(song);
        albumRepository.save(album);
    }
    public void removeSongFromAlbum(Album album,Song song) {
        album.getSongs().remove(song);
        albumRepository.save(album);
    }
    public Set<Album> getAllFavoriteAlbums(Principal principal) {

        Account account = accountService.findByUsername(principal.getName());
//        List<Album> albums = (List<Album>) account.getFavoriteAlbums();
//        return albums;
        return account.getFavoriteAlbums();

    }
}
