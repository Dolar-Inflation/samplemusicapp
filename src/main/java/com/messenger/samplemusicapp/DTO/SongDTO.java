package com.messenger.samplemusicapp.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDTO {
    private Long id;
    private String songname;
    private String artist;
    private String genre;
    private Long durability;
    private String fileUrl;
    private Long albumId;
    private Long accountId;
}
