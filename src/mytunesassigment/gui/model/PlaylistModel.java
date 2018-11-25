/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.model;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;
import mytunesassigment.bll.MRSLogicFacade;
import mytunesassigment.bll.MRSManager;

/**
 *
 * @author nedas
 */
public class PlaylistModel {

    private ObservableList<Playlist> allPlaylists = FXCollections.observableArrayList();

    private MRSLogicFacade logiclayer;

    /*
    The purpuse of this class is being a communication point between the GUI and BL.
    Operations in this class should be limited to only calling methods from the MRSLogicFacade(MRSManager)
     */
    public PlaylistModel() throws IOException {
        allPlaylists = FXCollections.observableArrayList();
        logiclayer = new MRSManager();
        allPlaylists.addAll(logiclayer.getAllPlaylists());
    }

    public ObservableList<Playlist> getPlaylists() {
        return allPlaylists;
    }

    public void createPlaylist(String name) {
        Playlist playList = logiclayer.createPlaylist(name);
        allPlaylists.add(playList);
    }

    public void deletePlaylist(Playlist play) {
        logiclayer.deletePlaylist(play);
        allPlaylists.remove(play);
    }

    public void updatePlaylist(Playlist play, List<Song> songList, String name) {
        allPlaylists.remove(play);
        Playlist playList = logiclayer.updatePlaylist(songList, name);
        allPlaylists.add(playList);
    }

    public void editPlaylist(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Song addToPlaylist(Playlist get, Song get0) {
        Song returnedSong = logiclayer.addToPlaylist(get, get0);
        return returnedSong;
    }
}
