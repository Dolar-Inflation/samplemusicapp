package com.messenger.samplemusicapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "songs")
@Getter @Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String songname;
    private String artist;
    private String genre;
    private Long durability;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "album_id")
    private Album album;
}


