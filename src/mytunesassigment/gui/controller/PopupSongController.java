/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.toIntExact;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import mytunesassigment.be.Song;
import mytunesassigment.gui.model.SongModel;

/**
 * FXML Controller class
 *
 * @author nedas
 */
public class PopupSongController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField artistField;
    @FXML
    private Label timeField;
    @FXML
    private Label urlField;
    @FXML
    private ChoiceBox<String> categoryChoice;
    @FXML
    private Label specificFunctionLabel;
    @FXML
    private Label errorLabel;

    private boolean isEditing = false;
    private SongModel songModel;
    private MediaPlayer mediaPlayer;
    private Song songToEdit;
    PlaylistController controller1;

    /**
     * Initializes the controller class. Also adds all elementes to the choice
     * box
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryChoice.getItems().add("Pop");
        categoryChoice.getItems().add("Electro");
        categoryChoice.getItems().add("Rock");
        categoryChoice.getItems().add("Techno");
        categoryChoice.getItems().add("Jazz");
        categoryChoice.getItems().add("Metal");

        try { //Initialises the song Modal
            songModel = new SongModel();
        } catch (IOException ex) {
            errorLabel.setText("Error: Cannot load song database");
        }
    }

    /*
    Uses JfileChooser class to choose file for the song.
     */
    @FXML
    private void chooseURL(ActionEvent event) {
        JFileChooser chooser = new JFileChooser(); // Initialises Chooser
        chooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop")); //Sets the directory to the desktop
        chooser.setDialogTitle("Select song "); //Specifies the objective of the user
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // Allows both file and directory navigation
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "MP3 and Wav files", "mp3", ".wav"); // Specifies what files should be only displayed
        chooser.setFileFilter(filter); //sets the filter 
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // If the user chooses the file. Get its path and song time.
            urlField.setText(chooser.getSelectedFile().getAbsolutePath());
            mediaPlayer = new MediaPlayer(new Media(new File(chooser.getSelectedFile().getAbsolutePath()).toURI().toString())); // Sets up the media object in order to get time of the song
            setMediaPlayerTime(); // Gets time of the song
        }
    }

    /*
    Closes the stage
     */
    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) urlField.getScene().getWindow();
        stage.close();
    }

    /*
    Saves the song and its data
     */
    @FXML
    private void saveSong(ActionEvent event) {
        int i = toIntExact(Math.round(mediaPlayer.getMedia().getDuration().toSeconds())); // Rounds up the seconds to an int so it can be inserted into the database
        String name = nameField.getText().trim(); //Eliminates all spaces (front and back. However not in the middle of the string )
        if (name != null && name.length() > 0 && name.length() < 50 && urlField.getText() != null && urlField.getText().length() != 0 && i > 0) { // Checks if the fields are not empty .
            if (!isEditing) { // If not editing . Creates song
                songModel.createSong(name, artistField.getText(), categoryChoice.getSelectionModel().getSelectedItem(), i, urlField.getText());
                errorLabel.setText("Success: Successfully created the song");
            } else { // If editing. Modifies the song in database and all playlists
                songModel.updateSong(songToEdit, name, artistField.getText(), categoryChoice.getSelectionModel().getSelectedItem(), i, urlField.getText());
                errorLabel.setText("Success: Successfully updated the song");
            }
        } else {
            errorLabel.setText("Error: Check if you have inserted a name and selected the correct file");
        }

        controller1.refreshSongList(isEditing); // Refreshes the list in main window to reflect changes
    }

    /*
    Specifies to the class that the song will be edited and inserts previous data (If there is any ) in the fields.
     */
    void setInfo(Song selectedItem) {
        isEditing = true;
        songToEdit = selectedItem;
        nameField.setText(selectedItem.getTitle());
        if (selectedItem.getArtist() != null) {
            artistField.setText(selectedItem.getArtist());
        }
        urlField.setText(selectedItem.getLocation());
        if (selectedItem.getCategory() != null) {
            categoryChoice.setValue(selectedItem.getCategory());
        } else {
            categoryChoice.setValue("");
        }
        mediaPlayer = new MediaPlayer(new Media(new File(selectedItem.getLocation()).toURI().toString()));
        setMediaPlayerTime(); // Sets up time field
    }

    /*
    Sets up time.
     */
    private void setMediaPlayerTime() {
        mediaPlayer.setOnReady(() -> { //Once the media file is loaded do the following things
            String averageSeconds = String.format("%1.0f", mediaPlayer.getMedia().getDuration().toSeconds());
            int minutes = Integer.parseInt(averageSeconds) / 60; //Gets minutes
            int seconds = Integer.parseInt(averageSeconds) % 60; // Gets seconds
            if (10 > seconds) { // If the value is under 10 seconds . Prevent from showing 0:x and turn into 0:0X 
                timeField.setText(minutes + ":0" + seconds);
            } else {
                timeField.setText(minutes + ":" + seconds);
            }
        });
    }

    /*
    Sets up the controller for the main window
     */
    void setController(PlaylistController controller1) {
        this.controller1 = controller1;
        if (isEditing) {
            specificFunctionLabel.setText("Editing song");
        } else {
            specificFunctionLabel.setText("Create song");
        }
    }

}
