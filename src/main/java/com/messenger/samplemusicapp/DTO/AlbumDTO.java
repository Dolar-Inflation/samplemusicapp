package com.messenger.samplemusicapp.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AlbumDTO {
    private Long id;
    private String title;
    private String artist;
    private String year;
    private String genre;
    private String imageurl;

    private List<SongDTO> songs;
    private Long accountId;
}
