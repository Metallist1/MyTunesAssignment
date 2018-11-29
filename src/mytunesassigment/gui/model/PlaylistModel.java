/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.model;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;
import mytunesassigment.bll.Manager;
import mytunesassigment.bll.LogicFacade;

/**
 *
 * @author nedas
 */
public class PlaylistModel {

    private ObservableList<Playlist> allPlaylists;

    private final LogicFacade logiclayer;

    /*
    Initialises the constructor and the logic layer
    */
    public PlaylistModel() throws IOException {
        logiclayer = new Manager();
    }

    /*
    Gets all playlists
    */
    public ObservableList<Playlist> getPlaylists() {
        allPlaylists = FXCollections.observableArrayList();
        allPlaylists.addAll(logiclayer.getAllPlaylists());
        return allPlaylists;
    }

    /*
    Sents name to create the playlist
    */
    public void createPlaylist(String name) {
        logiclayer.createPlaylist(name);
    }

    /*
    Deletes the specified playlist
    */
    public void deletePlaylist(Playlist play) {
        logiclayer.deletePlaylist(play);
    }

    /*
    Edits the playlist with inserted name
    */
    public void editPlaylist(Playlist play, String name) {
        logiclayer.editPlaylist(play, name);
    }

    /*
    Adds song to playlist
    */
    public Song addToPlaylist(Playlist play, Song song) {
        return logiclayer.addToPlaylist(play, song);
    }

    /*
    Removes song from playlist
    */
    public void removeSongFromPlaylist(Playlist selectedItem, Song selectedSong) {
        logiclayer.removeSongFromPlaylist(selectedItem, selectedSong);
    }

    /*
    Modifies the songs position in the playlist
    */
    public void editSongPosition(Playlist selectedItem, Song selected, Song exhangeWith) {
        logiclayer.editSongPosition(selectedItem, selected, exhangeWith);
    }
}
