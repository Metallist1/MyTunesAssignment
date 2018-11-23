/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.model;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunesassigment.be.Song;
import mytunesassigment.bll.MRSLogicFacade;
import mytunesassigment.bll.MRSManager;

/**
 *
 * @author nedas
 */
public class SongModel {

    private ObservableList<Song> allSongs = FXCollections.observableArrayList();

    private MRSLogicFacade logiclayer;

    public SongModel() throws IOException {
        allSongs = FXCollections.observableArrayList();
        logiclayer = (MRSLogicFacade) new MRSManager();
        allSongs.addAll(logiclayer.getAllSongs());
    }

    public ObservableList<Song> getSongs() {
        return allSongs;
    }
    
    public void createSong(String title,String artist, String category,double playtime,String location) {
        Song newSong = logiclayer.createSong(title, artist,category,playtime,location);
        allSongs.add(newSong);
    }

    public void deleteSong(Song songToDelete) {
        logiclayer.deleteSong(songToDelete);
        allSongs.remove(songToDelete);
    }

    public void updateSong(Song songToDelete, String title,String artist, String category,double playtime,String location) {
        allSongs.remove(songToDelete);
        Song newSong = logiclayer.updateSong(title, artist,category,playtime,location);
        allSongs.add(newSong);
    }
}
