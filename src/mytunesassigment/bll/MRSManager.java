/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.bll;

import java.io.IOException;
import java.util.List;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;
import mytunesassigment.dal.PlaylistDAO;
import mytunesassigment.dal.SongDAO;

/**
 *
 * @author nedas
 */
public class MRSManager implements MRSLogicFacade {

    private final PlaylistDAO playListDAO;
    private final SongDAO songDAO;

    // Constructor initialises the DAO classes
    public MRSManager() throws IOException {
        playListDAO = new PlaylistDAO();
        songDAO = new SongDAO();
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
    public Playlist createPlaylist(List<Song> songList, String name) {
        return playListDAO.createPlaylist(songList, name);
    }

    @Override
    public Playlist updatePlaylist(List<Song> songList, String name) {
        return playListDAO.updatePlaylist(songList, name);
    }

    @Override
    public List<Song> getAllSongs() {
        return songDAO.getAllSongs();
    }

    @Override
    public Song createSong(String title, String artist, String category, double playtime, String location) {
        return songDAO.createSong(title, artist, category, playtime, location);
    }

    @Override
    public void deleteSong(Song songToDelete) {
        songDAO.deleteSong();
    }

    @Override
    public Song updateSong(String title, String artist, String category, double playtime, String location) {
        return songDAO.updateSong(title, artist, category, playtime, location);
    }

}
