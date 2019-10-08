package com.lambda.service.impl;

import com.lambda.model.entity.Playlist;
import com.lambda.model.entity.Song;
import com.lambda.repository.PlaylistRepository;
import com.lambda.service.PlaylistService;
import com.lambda.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    SongService songService;

    @Override
    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    @Override
    public Page<Playlist> findAllByUser_Id(Long userId, Pageable pageable) {
        return playlistRepository.findAllByUser_Id(userId, pageable);
    }

    @Override
    public Page<Playlist> findAllByNameContaining(String name, Pageable pageable) {
        return playlistRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public void save(Playlist playlist) {
        playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }
    @Override
    public boolean addSongToPlaylist(Long songId, Long playlistId){
        Optional<Song> song = songService.findById(songId);
        Optional<Playlist> playlist = this.findById(playlistId);
        if(song.isPresent() && playlist.isPresent()){
            Collection<Song> songList = playlist.get().getSongs();
            songList.add(song.get());
            songService.save(song.get());
            return true;
        }
        return false;

    }
    @Override
    public boolean deletePlaylistSong(Long songId, Long playlistId) {
        Optional<Song> song = songService.findById(songId);
        Optional<Playlist> playlist = this.findById(playlistId);
        if(song.isPresent() && playlist.isPresent()) {
            Collection<Song> songList = playlist.get().getSongs();
            songList.remove(song.get());
            songService.save(song.get());
            return true;
        }
        return false;
    }
}
