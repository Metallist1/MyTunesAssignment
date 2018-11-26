/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.bll;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;
import mytunesassigment.bll.util.SongFilter;
import mytunesassigment.dal.PlaylistDAO;
import mytunesassigment.dal.SongDAO;

/**
 *
 * @author nedas
 */
public class MRSManager implements MRSLogicFacade {

    private final PlaylistDAO playListDAO;
    private final SongDAO songDAO;
    private final SongFilter songSearcher;

    // Constructor initialises the DAO classes
    public MRSManager() throws IOException {
        playListDAO = new PlaylistDAO();
        songDAO = new SongDAO();
        songSearcher = new SongFilter();
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playListDAO.getAllPlaylists();
    }

    @Override
    public void deletePlaylist(Playlist play) {
        playListDAO.deletePlaylist(play);
    }

    @Override
    public List<Song> getAllSongs() {
        return songDAO.getAllSongs();
    }

    @Override
    public Song createSong(String title, String artist, String category, int playtime, String location) {
        return songDAO.createSong(title, artist, category, playtime, location);
    }

    @Override
    public void deleteSong(Song songToDelete) {
        songDAO.deleteSong(songToDelete);
    }

    @Override
    public Song updateSong(Song song, String title, String artist, String category, int playtime, String location) {
        return songDAO.updateSong(song, title, artist, category, playtime, location);
    }

    @Override
    public Playlist createPlaylist(String name) {
        return playListDAO.createPlaylist(name);
    }

    @Override
    public Song addToPlaylist(Playlist playlist, Song song) {
        return playListDAO.addToPlaylist(playlist, song);
    }

    @Override
    public void removeSongFromPlaylist(Playlist selectedItem, Song selectedSong) {
        playListDAO.removeSongFromPlaylist(selectedItem, selectedSong);
    }

    @Override
    public void editPlaylist(Playlist get, String text) {
        playListDAO.updatePlaylist(get, text);
    }

    @Override
    public void editSongPosition(Playlist selectedItem, Song selected, Song exhangeWith) {
        playListDAO.editSongPosition(selectedItem, selected, exhangeWith);
    }

    @Override
    public ObservableList<Song> search(ObservableList<Song> items, String text) {
        return songSearcher.search(items, text);
    }

}
