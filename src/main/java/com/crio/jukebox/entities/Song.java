package com.crio.jukebox.entities;

import java.util.List;

public class Song extends BaseEntity {
    
    private final String name;

    private final String genre;

    private String albumName;

    private final List<String> artist;

    public Song(String id, String name, String genre, List<String> artist) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public List<String> getArtist() {
        return artist;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artist == null) ? 0 : artist.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Song other = (Song) obj;
        if (artist == null) {
            if (other.artist != null)
                return false;
        } else if (!artist.equals(other.artist))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString() {
        return "Song [id=" + id +", artis=" + artist + ", name=" + name + "]";
    }

}
