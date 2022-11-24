package com.crio.jukebox.services;

import java.util.List;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.repositories.IUserRepository;
import com.crio.jukebox.entities.PlayList;

public class UserService implements IuserService{

    IUserRepository iUserRepository;

    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public User create(String name) {
        // TODO Auto-generated method stub
        return iUserRepository.save(new User(null, name));
    }

    @Override
    public List<PlayList> getAllPlayList(String userId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
