package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.crio.jukebox.entities.Song;

public class SongRepository implements ISongRepository{

    private final Map<String, Song> songsList = new HashMap<String, Song>();

    private Integer autoIncrement = 0;    

    @Override
    public Song save(Song entity) {
        // TODO Auto-generated method stub
        if(entity.getId() == null) {
            autoIncrement++;
            Song s = new Song(Integer.toString(autoIncrement), entity.getName(), entity.getGenre(), entity.getArtist());
            songsList.put(s.getId(), s);
            return s;   
        }
        songsList.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Song> findAll() {
        // TODO Auto-generated method stub
        return songsList.values()
                        .stream()
                        .sorted((s1,s2)->Integer.valueOf(s1.getId())-Integer.valueOf(s2.getId()))
                        .collect(Collectors.toList());
    }                   
    

    @Override
    public Optional<Song> findById(String id) {
        // TODO Auto-generated method stub
        return Optional.ofNullable(songsList.get(id));
    }

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delete(Song entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return songsList.values().stream().count();
    }

    @Override
    public Song findSongById(String songId) {
        // TODO Auto-generated method stub
        return songsList.values().stream().filter(s->s.getId().equals(songId)).findAny().get();
    }
    
}
