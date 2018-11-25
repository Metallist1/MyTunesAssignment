/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.bll;

import java.util.List;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */

/* PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ PLEASE READ 

This class is basically used for connecting the BLL and GUI. 
This class only purpuse is to recieve method calls from GUI and transfer them to MRSManager.
MRSManager acts like a transfer node between BLL and DAO.
In here you should extensivly comment on what the method does. (Atleast this is how it was in peters code)

 */
public interface MRSLogicFacade {

    //gets all playlists
    public List<Playlist> getAllPlaylists();

    //deletes specific playlist
    public void deletePlaylist(Playlist play);
    

    // updates playlist
    public Playlist updatePlaylist(List<Song> songList, String name);

    //gets all songs
    public List<Song> getAllSongs();

    //creates new song
    public Song createSong(String title, String artist, String category, int playtime, String location);

    //deletes specific song
    public void deleteSong(Song songToDelete);

    //updates song
    public Song updateSong(String title, String artist, String category, int playtime, String location);

    public Playlist createPlaylist(String name);

    public Song addToPlaylist(Playlist playlist, Song song);


}
