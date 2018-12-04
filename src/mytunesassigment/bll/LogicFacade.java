/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.bll;

import java.util.List;
import javafx.collections.ObservableList;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public interface LogicFacade {

    /*
    Gets a list of all playlists
     */
    public List<Playlist> getAllPlaylists();

    /*
    Creates a playlist
     */
    public Playlist createPlaylist(String name);

    /*
    Edits the playlist with new name
     */
    public void editPlaylist(Playlist get, String text);

    /*
    Deletes specified playlist
     */
    public void deletePlaylist(Playlist play);

    /*
    Gets a list of all songs
     */
    public List<Song> getAllSongs();

    /*
    Creates a song with given parameters
     */
    public Song createSong(String title, String artist, String category, int playtime, String location);

    /*
    Updates song with given parameters
     */
    public Song updateSong(Song songToDelete, String title, String artist, String category, int playtime, String location);

    /*
    Deletes specified song
     */
    public void deleteSong(Song songToDelete);

    /*
    Adds specified song to specified playlist
     */
    public Song addToPlaylist(Playlist playlist, Song song);

    /*
    Removes song from specified playlist
     */
    public void removeSongFromPlaylist(Playlist selectedItem, Song selectedSong);

    /*
    Edits song position in playlist list
     */
    public void editSongPosition(Playlist selectedItem, Song selected, Song exhangeWith);

    /*
    Searches for all songs that matches the given query 
     */
    public ObservableList<Song> search(ObservableList<Song> items, String text);

    /*
    Gets all categories
     */
    public List<String> getAllCategories();

    /*
    Creates a new category
     */
    public void createCategory(String name);

    /*
    Deletes specified category
     */
    public void deleteCategory(String name);

}
