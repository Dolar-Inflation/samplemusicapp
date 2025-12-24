package com.messenger.samplemusicapp.Repository;

import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    public Song findByAlbum(Album album);
    Optional<Song> findBySongnameAndAlbum(String songname, Album album);





}
