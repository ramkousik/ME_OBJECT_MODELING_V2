package com.crio.jukebox.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.crio.jukebox.dtos.UserPlayedSongDto;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.SongPlayingOrder;
import com.crio.jukebox.entities.SongPlayingStatus;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.UserPlayList;
import com.crio.jukebox.exceptions.InvalidOperationException;
import com.crio.jukebox.exceptions.PlayListNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.IUserPlayListRepository;
import com.crio.jukebox.repositories.IUserRepository;

public class UserPlayListService implements IUserPlayListService {

    private IUserRepository iUserRepository;

    private ISongRepository iSongRepository;

    private IUserPlayListRepository userPlayListRepository;

    private List<Song> currentSongPlaylistQueue;

    private Integer currentSongPlayingId;

    public UserPlayListService(IUserRepository iUserRepository, ISongRepository iSongRepository, IUserPlayListRepository userPlayListRepository) {
        this.iUserRepository = iUserRepository;
        this.iSongRepository = iSongRepository;
        this.userPlayListRepository = userPlayListRepository;
        this.currentSongPlaylistQueue = new LinkedList<Song>();
    }

    @Override
    public UserPlayedSongDto playSongById(String userId, String songId)
            throws UserNotFoundException, SongNotFoundException {
        // TODO Auto-generated method stub
        User currUser = iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Song song = iSongRepository.findById(songId).orElseThrow(() -> new SongNotFoundException("Song Not Found"));
        if(currentSongPlaylistQueue.get(currentSongPlayingId).getId().equals(songId)) {
            UserPlayedSongDto userPlayedSongDto=new UserPlayedSongDto(currUser.getName(),song.getName(),song.getAlbumName(),String.join(", ", song.getArtist().toString()));
            return userPlayedSongDto;
        }
        for(int i = 0; i < currentSongPlaylistQueue.size(); i++) {
            if(currentSongPlaylistQueue.get(i).getId().equals(songId)) {
                currentSongPlayingId = i;
                Song newSong=currentSongPlaylistQueue.get(i);
                UserPlayedSongDto userPlayedSongDto=new UserPlayedSongDto(currUser.getName(),newSong.getName(),newSong.getAlbumName(),String.join(", ", newSong.getArtist().toString()));
                return userPlayedSongDto;
            }
        }
        throw new SongNotFoundException("Song is Not Present Current Playing PlayList");
    }

    @Override
    public UserPlayedSongDto playSongByOrder(String userId, SongPlayingOrder playingOrder)
            throws UserNotFoundException {
        // TODO Auto-generated method stub
        User currUser = iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        
        if(playingOrder == SongPlayingOrder.NEXT){
            currentSongPlayingId=(currentSongPlayingId + 1) % currentSongPlaylistQueue.size();
            Song currentPlayingSong = currentSongPlaylistQueue.get(currentSongPlayingId);
            UserPlayedSongDto userPlayedSongDto = new UserPlayedSongDto(currUser.getName(),currentPlayingSong.getName(),currentPlayingSong.getAlbumName(),String.join(", ", currentPlayingSong.getArtist().toString()));
            return userPlayedSongDto;
        }else if(playingOrder == SongPlayingOrder.BACK) {
            currentSongPlayingId = (currentSongPlayingId - 1 + currentSongPlaylistQueue.size()) % currentSongPlaylistQueue.size();
            Song currentPlayingSong=currentSongPlaylistQueue.get(currentSongPlayingId);
            UserPlayedSongDto userPlayedSongDto=new UserPlayedSongDto(currUser.getName(),currentPlayingSong.getName(),currentPlayingSong.getAlbumName(),String.join(", ", currentPlayingSong.getArtist().toString()));
            return userPlayedSongDto;
        }
        return null;
    }

    @Override
    public PlayList createPlayList(String userId, String PlayListName, List<String> songs)
            throws UserNotFoundException, SongNotFoundException {
        // TODO Auto-generated method stub

        User currUser=iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        List<Song> listOfAllSongs=new LinkedList<Song>();

        for(String songId:songs){
            Song song = iSongRepository.findSongById(songId);
            if(song != null)
                listOfAllSongs.add(song);
            else throw new SongNotFoundException("Songs Not Found");
        }
        PlayList playList = new PlayList(null, PlayListName, listOfAllSongs);
        PlayList savedPlayList = userPlayListRepository.createPlayList(playList);
        List<PlayList> listOfPlayList = userPlayListRepository.findAllPlayListByUserId(currUser.getId());
        if(listOfPlayList != null){
            listOfPlayList.add(savedPlayList);
            UserPlayList userPlayList = new UserPlayList(null, currUser, listOfPlayList);
            userPlayListRepository.save(userPlayList);
        }else{
            UserPlayList userPlayList=new UserPlayList(null, currUser, new ArrayList<PlayList>(Collections.singletonList(savedPlayList)));
            userPlayListRepository.save(userPlayList);
        }
        return savedPlayList;
    }

    @Override
    public void deletePlayList(String userId, String PlayListId)
            throws UserNotFoundException, PlayListNotFoundException {
        // TODO Auto-generated method stub
        if(userPlayListRepository.isPlayListExistByPlayListId(userId,PlayListId)){
            userPlayListRepository.delelePlayListByUserIdAndPlayListId(userId,PlayListId);
        }else{
            throw new PlayListNotFoundException("PlayList is Not Found");
        }
    }

    @Override
    public PlayList addSongToPlayList(String userId, String playListId, List<String> songs)
            throws UserNotFoundException, PlayListNotFoundException, SongNotFoundException,
            InvalidOperationException {
        // TODO Auto-generated method stub
        User currentUser = iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<Song> listOfAllSongs = new ArrayList<Song>();
        for(String songId:songs){
            Song song=iSongRepository.findSongById(songId);
            if(song != null)
                listOfAllSongs.add(song);
            else throw new SongNotFoundException("Songs Not Found");
        }
        PlayList playList=userPlayListRepository.addListOfSongsToUserPlayList(userId,playListId,listOfAllSongs);
        if(playList == null) throw new InvalidOperationException("No PlayList Found");
        return playList;
    }


    @Override
    public PlayList deleteSongFromPlayList(String userId, String playListId, List<String> songs)
            throws UserNotFoundException, PlayListNotFoundException, SongNotFoundException,
            InvalidOperationException {
        // TODO Auto-generated method stub
        User currentUser = iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<Song> listOfAllSongs = new ArrayList<Song>();
        for(String songId:songs){
            Song song = iSongRepository.findSongById(songId);
            if(song != null)
                listOfAllSongs.add(song);
            else throw new SongNotFoundException("Songs Not Found");
        }
        PlayList playList=userPlayListRepository.removeListOfSongsFromUserPlayList(userId,playListId,listOfAllSongs);
        if(playList == null) throw new InvalidOperationException("Song ID is Not Found");
        return playList;
    }

    @Override
    public UserPlayedSongDto setCurrentPlayList(String userId, String playListId)
            throws UserNotFoundException, PlayListNotFoundException {
        // TODO Auto-generated method stub
        User currentUser = iUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        if(!userPlayListRepository.isPlayListExistByPlayListId(userId,playListId)) throw new PlayListNotFoundException("PlayList is Not Found");
        List<PlayList> listOfPlayList = userPlayListRepository.findAllPlayListByUserId(currentUser.getId());
        for(PlayList currentPlay:listOfPlayList){
            if(currentPlay.getSongPlayingStatus() == SongPlayingStatus.PLAYING && !currentPlay.getId().equals(playListId)){
                currentPlay.setSongPlayingStatus(SongPlayingStatus.NOT_PLAYING);
            }
        }
        PlayList currentPlayList=listOfPlayList.stream().filter(p->p.getId().equals(playListId)).findFirst().get();
        currentPlayList.setSongPlayingStatus(SongPlayingStatus.PLAYING);
        currentSongPlaylistQueue.clear();
        for(Song song:currentPlayList.getSongs()){
            currentSongPlaylistQueue.add(song);
        }
        //songIterator=currSongPlaylistQueue.listIterator();
        currentSongPlayingId = 0;
        Song currSong=currentSongPlaylistQueue.get(currentSongPlayingId);
        UserPlayedSongDto userPlayedSongDto=new UserPlayedSongDto(currentUser.getName(),currSong.getName(),currSong.getAlbumName(),currSong.getArtist().toString());
        //songIterator.next();
        return userPlayedSongDto;
    }

    
}

