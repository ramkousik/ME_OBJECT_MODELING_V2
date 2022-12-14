package com.crio.jukebox.services;

import java.util.List;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.PlayList;

public interface IuserService {
    public User create(String name); 
    public List<PlayList> getAllPlayList(String userId);
}
