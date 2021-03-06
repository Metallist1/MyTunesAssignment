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
import mytunesassigment.dal.CategoriesDAO;
import mytunesassigment.dal.PlaylistDAO;
import mytunesassigment.dal.PlaylistSongDAO;
import mytunesassigment.dal.SongDAO;

/**
 *
 * @author nedas
 */
public class Manager implements LogicFacade {

    private final PlaylistDAO playListDAO;
    private final SongDAO songDAO;
    private final SongFilter songSearcher;
    private final PlaylistSongDAO PlaylistSongInfo;
    private final CategoriesDAO categoriesDAO;

    /*
    Initialises all classes in DAL
     */
    public Manager() throws IOException {
        playListDAO = new PlaylistDAO();
        songDAO = new SongDAO();
        songSearcher = new SongFilter();
        PlaylistSongInfo = new PlaylistSongDAO();
        categoriesDAO = new CategoriesDAO();
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playListDAO.getAllPlaylists();
    }

    @Override
    public void deletePlaylist(Playlist play) {
        PlaylistSongInfo.deleteFromPlaylistSongsEverything(play); // Deletes all playlist references in playlistSong table
        playListDAO.deletePlaylist(play); //Deletes the playlist from playlist table
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
        PlaylistSongInfo.deleteFromPlaylistSongsEverything(songToDelete);// Deletes all song references in playlistSong table
        songDAO.deleteSong(songToDelete); // Deletes song from song table
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
        return PlaylistSongInfo.addToPlaylist(playlist, song);
    }

    @Override
    public void removeSongFromPlaylist(Playlist selectedItem, Song selectedSong) {
        PlaylistSongInfo.removeSongFromPlaylist(selectedItem, selectedSong);
    }

    @Override
    public void editPlaylist(Playlist get, String text) {
        playListDAO.updatePlaylist(get, text);
    }

    @Override
    public void editSongPosition(Playlist selectedItem, Song selected, Song exhangeWith) {
        PlaylistSongInfo.editSongPosition(selectedItem, selected, exhangeWith);
    }

    @Override
    public ObservableList<Song> search(ObservableList<Song> items, String text) {
        return songSearcher.search(items, text);
    }

    @Override
    public List<String> getAllCategories() {
        return categoriesDAO.getAllCategories();
    }

    @Override
    public void createCategory(String name) {
        categoriesDAO.createCategory(name);
    }

    @Override
    public void deleteCategory(String name) {
        categoriesDAO.deleteCategory(name);
    }

}
