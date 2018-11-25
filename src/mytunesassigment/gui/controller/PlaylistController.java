/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
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

    private PlaylistModel playlistModel;
    private SongModel songModel;
    private ObservableList<Playlist> observableListPlay;
    private ObservableList<Song> observableListSong;
    @FXML
    private Label currentSong;
    @FXML
    private TextField search;
    @FXML
    private ListView<Song> songsInPlaylist;
    @FXML
    private TableView<Song> tableViewSongs;
    @FXML
    private TableColumn<Song, String> nameColumn;
    @FXML
    private TableColumn<Song, String> artistColumn;
    @FXML
    private TableColumn<Song, String> categoryColumn;
    @FXML
    private TableColumn<Song, Integer> timeColumn;
    @FXML
    private TableView<Playlist> playlistTableView;
    @FXML
    private TableColumn<Playlist, String> playlistSongNames;
    @FXML
    private TableColumn<Playlist, Integer> playlistSongTotalCount;
    @FXML
    private TableColumn<Playlist, Integer> playlistSongTotalTime;

    public PlaylistController() throws IOException {
        playlistModel = new PlaylistModel();
        songModel = new SongModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        observableListPlay = playlistModel.getPlaylists();
        observableListSong = songModel.getSongs();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("playtime"));
        tableViewSongs.setItems(observableListSong);
        playlistSongNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        playlistSongTotalCount.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        playlistSongTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        playlistTableView.setItems(observableListPlay);
    }

    /*
    This file is just a list of commands that could be usefull for playing music. Please reffer to 
    https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
    for detaled info of commands and
    https://stackoverflow.com/questions/22490064/how-to-use-javafx-mediaplayer-correctly
    to set up media player normally
     */
    @FXML
    private void SkipSongBackward(ActionEvent event) {
    }

    @FXML
    private void playSong(ActionEvent event) {
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

    @FXML
    private void skipSongForward(ActionEvent event) {
    }

    @FXML
    private void setSound(MouseEvent event) {

    }

    @FXML
    private void searchForSong(ActionEvent event) {
    }

    @FXML
    private void createPlaylist(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupPlaylist.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void editPlaylist(ActionEvent event) {
    }

    @FXML
    private void deletePlaylist(ActionEvent event) {
        int playlistLocation = playlistTableView.getSelectionModel().getSelectedIndex();
        playlistModel.deletePlaylist(observableListPlay.get(playlistLocation));
    }

    @FXML
    private void moveSongUp(ActionEvent event) {
    }

    @FXML
    private void moveSongDown(ActionEvent event) {
    }

    @FXML
    private void removeSong(ActionEvent event) {
    }

    @FXML
    private void addSong(ActionEvent event) {
        int playlistLocation = playlistTableView.getSelectionModel().getSelectedIndex();
        int songLocation = tableViewSongs.getSelectionModel().getSelectedIndex();
        playlistModel.addToPlaylist(observableListPlay.get(playlistLocation), observableListSong.get(songLocation));
    }

    @FXML
    private void createSong(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupSong.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void editSong(ActionEvent event) {
    }

    @FXML
    private void deleteSong(ActionEvent event) {
    }

    @FXML
    private void close(ActionEvent event) {
    }

    @FXML
    private void displaySongsInPlaylist(MouseEvent event) {
        songsInPlaylist.getItems().clear();
        int location = playlistTableView.getSelectionModel().getSelectedIndex();
        List<Song> toBeAddedSongList = observableListPlay.get(location).getSongList();
        for (int x = toBeAddedSongList.size() - 1; x >= 0; x--) {
            songsInPlaylist.getItems().add(toBeAddedSongList.get(x));
        }
    }

}
