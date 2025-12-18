package com.messenger.samplemusicapp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "album_id",nullable = true)
    @JsonBackReference
    private Album album;
}


