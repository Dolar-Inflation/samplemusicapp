package com.messenger.samplemusicapp.Repository;

import com.messenger.samplemusicapp.Entity.Account;
import com.messenger.samplemusicapp.Entity.Album;
import com.messenger.samplemusicapp.Entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

public Account findByCreatedSongs(List<Song> createdSongs);

public Account findByCreatedAlbums(List<Album> createdAlbums);

}
