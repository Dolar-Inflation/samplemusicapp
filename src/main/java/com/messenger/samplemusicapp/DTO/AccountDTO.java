package com.messenger.samplemusicapp.DTO;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AccountDTO {
    private Long id;

    private String username;

    private String password;

    private List<Album> createdAlbums;

    private List<Song> createdSongs;

    private Set<Song> favoriteSongs;

    private Set<Album> favoriteAlbums;
}
