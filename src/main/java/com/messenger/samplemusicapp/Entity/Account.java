package com.messenger.samplemusicapp.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "account")
    private List<Album> createdAlbums = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Song> createdSongs = new ArrayList<>();

    @ManyToMany
    @JsonManagedReference(value = "account-songs")
    @JoinTable(name = "account_favorite_songs",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Set<Song> favoriteSongs = new HashSet<>();


    @ManyToMany
    @JsonManagedReference(value = "account-albums")
    @JoinTable(name = "account_favorite_albums",
    joinColumns = @JoinColumn(name = "account_id"),
    inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> favoriteAlbums = new HashSet<>();



}
