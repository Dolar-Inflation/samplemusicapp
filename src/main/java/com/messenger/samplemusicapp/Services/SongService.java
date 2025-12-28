package com.messenger.samplemusicapp.Services;

import com.messenger.samplemusicapp.DTO.SongDTO;
import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import com.messenger.samplemusicapp.Mappers.SongMapper;
import com.messenger.samplemusicapp.Records.SongInfo;
import com.messenger.samplemusicapp.Repository.AlbumRepository;
import com.messenger.samplemusicapp.Repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SongService {
    private final SongMapper songMapper;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final UploadFileService uploadFileService;
    private final AccountService accountService;

    public SongService(SongMapper songMapper, SongRepository songRepository, AlbumRepository albumRepository, UploadFileService uploadFileService, AccountService accountService) {
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.uploadFileService = uploadFileService;
        this.accountService = accountService;
    }


public SongDTO addSong(String songname, String artist, String albumTitle,
                       String genre, Account account, MultipartFile file) throws IOException {
    Song song = new Song();
    Album album = null;
    if (albumTitle != null && !albumTitle.isBlank()) {
        album = albumRepository.findByTitle(albumTitle).orElse(null);
    }

    if (album != null) {
        Optional<Song> existing = songRepository.findBySongnameAndAlbum(songname, album);
        if (existing.isPresent()) {
            return songMapper.SongToDTO(existing.get());
        }
    }

    song.setSongname(songname);
    song.setArtist(artist);
    song.setGenre(genre);
    song.setAccount(account);


    if (albumTitle != null && !albumTitle.isBlank()) {
        albumRepository.findByTitle(albumTitle).ifPresent(song::setAlbum);

    }


    if (file != null && !file.isEmpty()) {
        String fileUrl = uploadFileService.uploadFile(file);
        song.setFileUrl(fileUrl);
        Song savedSong = songRepository.save(song);
        return songMapper.SongToDTO(savedSong);
    }
    else {
        SongDTO songDTO = songMapper.SongToDTO(song);
        return songDTO;
    }


}


//    public void FindAlbumBySong(Song song, Album album) {
//        if (album != null) {
//            System.out.println(song.getAlbum());
//            System.out.println(song.getAlbum().getTitle());
//        }
//        else throw new NullPointerException("Album is null"); {
//
//        }
//    }

    public Map<String, SongInfo> getAllSongs() {

        return songRepository.findAll()
                .stream()
                .filter(s ->s.getFileUrl() !=null && s.getId() !=null )
                .collect(Collectors.toMap(
                        Song::getSongname,
                        song -> new SongInfo(song.getFileUrl(),
                                song.getId()),(s1, s2) -> s1));



    }
    public Set<SongDTO> getAllFavoriteSongs(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());

        return account.getFavoriteSongs()
                .stream()
                .map(songMapper::SongToDTO)
                .collect(Collectors.toSet());
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
