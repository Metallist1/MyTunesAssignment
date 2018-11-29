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
import mytunesassigment.bll.Manager;
import mytunesassigment.bll.LogicFacade;

/**
 *
 * @author nedas
 */
public class SongModel {

    private ObservableList<Song> allSongs = FXCollections.observableArrayList();

    private final LogicFacade logiclayer;

    /*
    Initialises the constructor and the logic layer
    */
    public SongModel() throws IOException {
        logiclayer = (LogicFacade) new Manager();
    }

    /*
    Gets all existing songs
    */
    public ObservableList<Song> getSongs() {
        allSongs = FXCollections.observableArrayList();
        allSongs.addAll(logiclayer.getAllSongs());
        return allSongs;
    }

    /*
    Creates new song with specifics given
    */
    public void createSong(String title, String artist, String category, int playtime, String location) {
        logiclayer.createSong(title, artist, category, playtime, location);
    }

    /*
    Deletes specified song
    */
    public void deleteSong(Song songToDelete) {
        logiclayer.deleteSong(songToDelete);
    }

    /*
    Updates specified song with given values
    */
    public void updateSong(Song songToDelete, String title, String artist, String category, int playtime, String location) {
        logiclayer.updateSong(songToDelete, title, artist, category, playtime, location);
    }

    /*
    Searches for all songs with specific query
    */
    public ObservableList<Song> search(ObservableList<Song> items, String query) {
        return logiclayer.search(items, query);
    }
}
