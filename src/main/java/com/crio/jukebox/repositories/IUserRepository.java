package com.crio.jukebox.repositories;

import java.util.List;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.PlayList;

public interface IUserRepository extends CRUDRepository<User, String> {
    public List<PlayList> findAllPlayList(String name);

    public com.crio.jukebox.entities.User save(com.crio.jukebox.entities.User user);
}
