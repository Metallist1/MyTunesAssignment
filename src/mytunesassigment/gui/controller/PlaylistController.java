/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.File;
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
import javafx.scene.control.Button;
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

    private final PlaylistModel playlistModel;
    private final SongModel songModel;
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
    @FXML
    private Button playButton;

    private MediaPlayer mediaPlayer;
    private int currentSongPlaying = 0;

    /*
    Initialises constructor with PlaylistModel and SongModel clases
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
        observableListPlay = playlistModel.getPlaylists();
        observableListSong = songModel.getSongs();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("playtimeString"));

        tableViewSongs.setItems(observableListSong);
        playlistSongNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        playlistSongTotalCount.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        playlistSongTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTimeString"));

        playlistTableView.setItems(observableListPlay);
        songsInPlaylistName.setCellValueFactory(new PropertyValueFactory<>("title"));
        songInPlaylistID.setCellValueFactory(new PropertyValueFactory<>("IDinsideList"));
    }

    /*
    Skips to the previous song. 
     */
    @FXML
    private void SkipSongBackward(ActionEvent event) {
        if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
            stopMediaPlayer(); //Stops music so the sound doesnt dublicate
            if (currentSongPlaying - 1 == -1) {
                currentSongPlaying = 0; //If the song list goes to the first elemenet. It will not go out of bounds 
            } else {
                currentSongPlaying--;
            }
            play(); //Plays the song
        }
    }

    /*
    Plays the songs
     */
    @FXML
    private void playSong(ActionEvent event) {
        if (mediaPlayer == null && songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
            currentSongPlaying = songsInPlaylist.getSelectionModel().getSelectedIndex(); //Selects the song to play using the current selected song
            play(); //Plays the song
        } else { //otherwise stops the media player and sets the mediaPlayer object to null
            currentSong.setText("(none) is now playing");
            playButton.setText("Play");
            stopMediaPlayer();
            mediaPlayer = null;
        }
    }

    /*
    Skips to the next song
     */
    @FXML
    private void skipSongForward(ActionEvent event) {
        if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
            stopMediaPlayer();
            if (currentSongPlaying + 1 == songsInPlaylist.getItems().size()) {
                currentSongPlaying = 0;
            } else {
                currentSongPlaying++;
            }
            play();
        }
    }

    /*
    If the media player object is not null. Calls make sound method
     */
    @FXML
    private void setSound(MouseEvent event) {
        if (mediaPlayer != null) {
            makeSound();
        }
    }

    /*
    Sets sound according to the volume slider value
     */
    private void makeSound() {
        mediaPlayer.setVolume(volumeSlider.getValue());
    }

    /*
    Plays the media file by getting its location from the selected element list
     */
    private void play() {
        playButton.setText("Pause");
        mediaPlayer = new MediaPlayer(new Media(new File(songsInPlaylist.getItems().get(currentSongPlaying).getLocation()).toURI().toString()));
        songsInPlaylist.getSelectionModel().clearAndSelect(currentSongPlaying);
        currentSong.setText(songsInPlaylist.getItems().get(currentSongPlaying).getTitle() + " is now playing");
        mediaPlayer.play();
        makeSound();
        mediaPlayer.setOnEndOfMedia(() -> { // On end of media checks if the next song is valid to be played.
            if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
                if (songsInPlaylist.getItems().size() == currentSongPlaying + 1) {
                    currentSongPlaying = 0; // If the last element of the list is reached. Restarts the counter back to 0 
                } else {
                    currentSongPlaying++;
                }
                play(); //Calls itself to continue playing
            } else {
                stopMediaPlayer(); //If no song is selected. Then stop the playing music.
            }
        });
    }

    /*
    Sets up scenes for creating and editing playlists and songs
     */
    private void setUpScenes(int whichScene, boolean isEditing) throws IOException {
        Parent root1;
        if (whichScene == 1) { //If the scene needed is playlist view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupPlaylist.fxml"));
            root1 = (Parent) fxmlLoader.load();
            if (isEditing) {
                stopMediaPlayer(); //stop the media. As the playlist that is being edited is the one thats playing
                fxmlLoader.<PopupPlaylistController>getController().setInfo(playlistTableView.getSelectionModel().getSelectedItem()); // Tells the playlist controller class that the method will be editing its name
            }
            fxmlLoader.<PopupPlaylistController>getController().setController(this); //Sets controler by default for both creating and editing playlists
        } else { // If the scene needed is song view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupSong.fxml"));
            root1 = (Parent) fxmlLoader.load();
            if (isEditing) {
                stopMediaPlayer();
                fxmlLoader.<PopupSongController>getController().setInfo(tableViewSongs.getSelectionModel().getSelectedItem());// Tells the song controller class that the method will be editing song info
            }
            fxmlLoader.<PopupSongController>getController().setController(this); //Sets controler by default for both creating and editing songs
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    /*
    Creates playlist by calling setUpScene class and specifying the class needed and if it will be editing
     */
    @FXML
    private void createPlaylist(ActionEvent event) throws IOException {
        setUpScenes(1, false);
    }

    /*
    Edits the playlist if a playlist is selected by specifying in the setUpScene that the playlist will be edited
     */
    @FXML
    private void editPlaylist(ActionEvent event) throws IOException {
        if (playlistTableView.getSelectionModel().getSelectedIndex() != -1) {
            setUpScenes(1, true);
        }
    }

    /*
    Deletes the playlist if a playlist is selected
     */
    @FXML
    private void deletePlaylist(ActionEvent event) {
        if (playlistTableView.getSelectionModel().getSelectedIndex() != -1) {
            stopMediaPlayer();
            playlistModel.deletePlaylist(playlistTableView.getSelectionModel().getSelectedItem()); // calls delete playlist from playlistModel
            songsInPlaylist.getItems().clear(); //clears items in playlist song view
            refreshList(); //refreshes the list for the changes to take place
        }
    }

    /*
    If media player exists. Stops it and declares it a null
     */
    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            currentSong.setText("(none) is playing");
            playButton.setText("Play");
            mediaPlayer = null;
        }
    }

    /*
    Moves the song up in the playlist song table and in database
     */
    @FXML
    private void moveSongUp(ActionEvent event) {
        int position = songsInPlaylist.getSelectionModel().getSelectedIndex();
        if (position != -1 && position + 1 < songsInPlaylist.getItems().size()) { //checks if the position the user wants to move is valid
            stopMediaPlayer(); //stops media player
            playlistModel.editSongPosition(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(position), songsInPlaylist.getItems().get(position + 1)); //Calls a method to edit the song position in database
            refreshPlaylistSongList(position, false); //Refreshes the gui to reflect changes
        }
    }

    /*
    Moves the song down
     */
    @FXML
    private void moveSongDown(ActionEvent event) {
        int position = songsInPlaylist.getSelectionModel().getSelectedIndex();
        if (position != -1 && position - 1 >= 0) {
            stopMediaPlayer();
            playlistModel.editSongPosition(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(position), songsInPlaylist.getItems().get(position - 1));
            refreshPlaylistSongList(position, true);
        }
    }

    /*
    Removes the song from the playlist song list
     */
    @FXML
    private void removeSong(ActionEvent event) {
        if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1 && playlistTableView.getSelectionModel().getSelectedIndex() != -1) {
            stopMediaPlayer();
            playlistModel.removeSongFromPlaylist(playlistTableView.getSelectionModel().getSelectedItem(), songsInPlaylist.getSelectionModel().getSelectedItem());
            refreshPlaylistSongs(songsInPlaylist.getSelectionModel().getSelectedIndex(), tableViewSongs.getSelectionModel().getSelectedItem(), false);
        }
    }

    /*
    Adds a song to the playlist song list
     */
    @FXML
    private void addSong(ActionEvent event) {
        if (playlistTableView.getSelectionModel().getSelectedIndex() != -1 && tableViewSongs.getSelectionModel().getSelectedIndex() != -1) {
            stopMediaPlayer();
            playlistModel.addToPlaylist(playlistTableView.getSelectionModel().getSelectedItem(), tableViewSongs.getSelectionModel().getSelectedItem());
            refreshPlaylistSongs(tableViewSongs.getItems().size(), tableViewSongs.getSelectionModel().getSelectedItem(), true);
        }
    }

    /*
    Calls the song list. Specifies that the lis will not be edited
     */
    @FXML
    private void createSong(ActionEvent event) throws IOException {
        setUpScenes(2, false);
    }

    /*
    If a song is selected. Specifies that the song will need editing
     */
    @FXML
    private void editSong(ActionEvent event) throws IOException {
        if (tableViewSongs.getSelectionModel().getSelectedIndex() != -1) {
            setUpScenes(2, true);
        }
    }

    /*
    Deletes the selected song from the database and user view.
     */
    @FXML
    private void deleteSong(ActionEvent event) {
        if (tableViewSongs.getSelectionModel().getSelectedIndex() != -1) { // If a song is selected
            stopMediaPlayer(); //Stops music
            songModel.deleteSong(tableViewSongs.getSelectionModel().getSelectedItem()); //Calls song delete method in songDAO
            refreshSongList(false); //refreshes the list. Specifying that playlist has not been edited
            songsInPlaylist.getItems().clear(); //clears the playlist song list
            refreshList(); // Refreshes the list
        }
    }

    /*
    Displays the playlist songs in a playlit song table
     */
    @FXML
    private void displaySongsInPlaylist(MouseEvent event) {
        stopMediaPlayer();
        songsInPlaylist.getItems().clear();
        List<Song> toBeAddedSongList = playlistTableView.getSelectionModel().getSelectedItem().getSongList(); //Gets specific playlist song list
        for (int x = toBeAddedSongList.size() - 1; x >= 0; x--) { //counts down from the bottom to top so the last song to be added would play last.
            toBeAddedSongList.get(x).setIDinsideList(toBeAddedSongList.size() - x); //Sets Id in the list for the user to see it.
            songsInPlaylist.getItems().add(toBeAddedSongList.get(x)); //adds the song to the table
        }
    }

    /*
    Searches for a specific song by name 
     */
    @FXML
    private void search(KeyEvent event) {
        if (searchTextBox.getText() == null || searchTextBox.getText().length() <= 0) { //If there is no value inserted. Set up normal songs
            tableViewSongs.setItems(songModel.getSongs());
        } else { //Else call method from song filter by specifying both the song list and the query
            ObservableList<Song> foundMovieList = songModel.search(songModel.getSongs(), searchTextBox.getText());
            if (foundMovieList != null) { //If anything is returned. Display it.
                tableViewSongs.setItems(foundMovieList);
            }
        }
    }

    /*
    Reloads the playlist list
     */
    public void refreshList() {
        playlistTableView.getItems().clear();
        playlistTableView.setItems(playlistModel.getPlaylists());
    }

    /*
    Reloads the song list. If the song is being edited. Also reloads the playlists to display changes.
     */
    void refreshSongList(boolean isEditing) {
        tableViewSongs.getItems().clear();
        tableViewSongs.setItems(songModel.getSongs());
        if (isEditing) {
            songsInPlaylist.getItems().clear();
            refreshList();
        }
    }

    /*
    Moves song in the playlist song view. So the user could see the changes faster.
     */
    void refreshPlaylistSongList(int position, boolean MovingUp) {
        if (MovingUp) { //If the song should be mobed up the list
            songsInPlaylist.getItems().add(position - 1, songsInPlaylist.getItems().get(position)); //Creates a new song
            songsInPlaylist.getItems().remove(position + 1); //Removes the previous song
            songsInPlaylist.getItems().get(position - 1).setIDinsideList(position); //Changes the ID
            songsInPlaylist.getItems().get(position).setIDinsideList(position + 1); // Changes the moved song ID
            songsInPlaylist.getSelectionModel().select(position - 1); //Changes the selected song to reflect changes
        } else {
            if (position + 2 <= songsInPlaylist.getItems().size()) { //Checks if move is valid to avoid out of bounds exception
                songsInPlaylist.getItems().add(position + 2, songsInPlaylist.getItems().get(position));
                songsInPlaylist.getItems().remove(position);
                songsInPlaylist.getItems().get(position + 1).setIDinsideList(position + 2);
                songsInPlaylist.getItems().get(position).setIDinsideList(position + 1);
                songsInPlaylist.getSelectionModel().select(position + 1);
            }
        }
    }

    /*
    Refreshes the playlists song list so the user can see the changes
     */
    private void refreshPlaylistSongs(int songToModifyIndex, Song songToModify, boolean isAdding) {
        if (isAdding) { // If the user is adding a song
            songToModify.setIDinsideList(songsInPlaylist.getItems().size() + 1); //Modifies visual ID . Sets it to the highest possible in the list
            songsInPlaylist.getItems().add(songToModify);
        } else { //Else removes the song from the list
            songsInPlaylist.getItems().remove(songToModifyIndex);
        }
    }
}
