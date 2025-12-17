package com.messenger.samplemusicapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @OneToMany(mappedBy = "artist")
    private List<Album> createdAlbums = new ArrayList<>();

    @OneToMany(mappedBy = "artist")
    private List<Song> createdSongs = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_favorite_songs",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))

    private Set<Song> favoriteSongs = new HashSet<>();
}
