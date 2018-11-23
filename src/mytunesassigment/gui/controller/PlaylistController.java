/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;
import mytunesassigment.gui.model.PlaylistModel;
import mytunesassigment.gui.model.SongModel;

/**
 * FXML Controller class
 *
 * @author nedas
 */
public class PlaylistController implements Initializable {

    @FXML
    private ListView<Song> SongView;
    @FXML
    private ListView<Playlist> PlaylistView;

    private PlaylistModel playlistModel;
    private SongModel songModel;

    /*
    The purpuse of this class is ONLY to display GUI elements and also play songs.
    In regards to this fact i am also initialising SongModel and PlaylistModel.
    For general playlist options (like displaying all playlists and such ) You should reference Playlistmodel
    While for general individual song options (like adding , deleting or other functions) YOu should reference songModel
     */
    public PlaylistController() throws IOException {
        playlistModel = new PlaylistModel();
        songModel = new SongModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PlaylistView.setItems(playlistModel.getPlaylists());
        SongView.setItems(songModel.getSongs());
    }

    /*
    This file is just a list of commands that could be usefull for playing music. Please reffer to 
    https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
    for detaled info of commands and
    https://stackoverflow.com/questions/22490064/how-to-use-javafx-mediaplayer-correctly
    to set up media player normally
     */
    public void playMusic() {
        Media pick = new Media("Despacito.mp3"); // replace this with your own audio url
        MediaPlayer player = new MediaPlayer(pick);

        // Play the media file
        player.play();
        //Sets volume of music to your specified value . 
        //The volume HAS to be a double and only can be between 1 and 0 .
        //1 Being full soung . 0 Being mute . So 0.5 Would be half soung
        player.setVolume(0.5);

        
        // This next method is initialised is when the media player entity is ready . 
        // This means on start of song play it will do what ever you type in ready.
        // Could be used for counter , showing song name or something else.
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                //Your code that will run at the start of the song
            }
        });

        //This method runs when the audio ends .
        //Could be used to switch to next song and change the look .
        // Please note that all the coding should be in the run method that is created
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                //Your code that will run at end of audio
            }
        });
    }
}
