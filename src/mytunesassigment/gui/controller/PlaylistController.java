/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    private TableView<Song> songsInPlaylist;
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
    @FXML
    private TableColumn<Song, Integer> songInPlaylistID;
    @FXML
    private TableColumn<Song, String> songsInPlaylistName;
    @FXML
    private TextField searchTextBox;
    @FXML
    private Slider volumeSlider;

    private MediaPlayer mediaPlayer;
    private int currentSongPlaying = 0;

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
        songsInPlaylistName.setCellValueFactory(new PropertyValueFactory<>("title"));
        songInPlaylistID.setCellValueFactory(new PropertyValueFactory<>("IDinsideList"));
    }

    @FXML
    private void SkipSongBackward(ActionEvent event) {
        mediaPlayer.stop();
        if (currentSongPlaying - 1 == -1) {
            currentSongPlaying = 0;
        } else {
            currentSongPlaying--;
        }
        play();
    }

    @FXML
    private void playSong(ActionEvent event) {
        currentSongPlaying = songsInPlaylist.getSelectionModel().getSelectedIndex();
        play();
    }

    @FXML
    private void skipSongForward(ActionEvent event) {
        mediaPlayer.stop();
        if (currentSongPlaying + 1 == songsInPlaylist.getItems().size()) {
            currentSongPlaying = 0;
        } else {
            currentSongPlaying++;
        }
        play();
    }

    @FXML
    private void setSound(MouseEvent event) {
        if(mediaPlayer != null){
        makeSound();
        }
    }

    private void makeSound() {
        mediaPlayer.setVolume(volumeSlider.getValue());
    }

    private void play() {
        mediaPlayer = new MediaPlayer(new Media(new File(songsInPlaylist.getItems().get(currentSongPlaying).getLocation()).toURI().toString()));
        currentSong.setText(songsInPlaylist.getItems().get(currentSongPlaying).getTitle() + " is now playing");
        mediaPlayer.play();
        makeSound();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if (songsInPlaylist.getItems().size() == currentSongPlaying + 1) {
                    currentSongPlaying = 0;
                } else {
                    currentSongPlaying++;
                }
                play();
            }
        });
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
    private void editPlaylist(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupPlaylist.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        fxmlLoader.<PopupPlaylistController>getController().setInfo(playlistTableView.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void deletePlaylist(ActionEvent event) {
        playlistModel.deletePlaylist(playlistTableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void moveSongUp(ActionEvent event) {
        int position = songsInPlaylist.getSelectionModel().getSelectedIndex();
        playlistModel.editSongPosition(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(position), songsInPlaylist.getItems().get(position + 1));
    }

    @FXML
    private void moveSongDown(ActionEvent event) {
        int position = songsInPlaylist.getSelectionModel().getSelectedIndex();
        playlistModel.editSongPosition(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(position), songsInPlaylist.getItems().get(position - 11));
    }

    @FXML
    private void removeSong(ActionEvent event) {
        playlistModel.removeSongFromPlaylist(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void addSong(ActionEvent event) {
        playlistModel.addToPlaylist(playlistTableView.getSelectionModel().getSelectedItem(), tableViewSongs.getSelectionModel().getSelectedItem());
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
    private void editSong(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupSong.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        fxmlLoader.<PopupSongController>getController().setInfo(tableViewSongs.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void deleteSong(ActionEvent event) {
        songModel.deleteSong(tableViewSongs.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void displaySongsInPlaylist(MouseEvent event) {
        songsInPlaylist.getItems().clear();
        List<Song> toBeAddedSongList = playlistTableView.getSelectionModel().getSelectedItem().getSongList();
        for (int x = toBeAddedSongList.size() - 1; x >= 0; x--) {
            toBeAddedSongList.get(x).setIDinsideList(toBeAddedSongList.size() - x);
            songsInPlaylist.getItems().add(toBeAddedSongList.get(x));
        }
    }

    @FXML
    private void search(KeyEvent event) {
        if (searchTextBox.getText() == null || searchTextBox.getText().isEmpty()) {
            tableViewSongs.setItems(songModel.getSongs());
        } else {
            ObservableList<Song> foundMovieList;
            foundMovieList = songModel.search(songModel.getSongs(), searchTextBox.getText());
            if (foundMovieList != null) {
                tableViewSongs.setItems(foundMovieList);
            }
        }
    }

}
