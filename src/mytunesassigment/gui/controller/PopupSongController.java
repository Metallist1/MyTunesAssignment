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
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import mytunesassigment.be.Song;
import mytunesassigment.gui.model.CategoryModel;
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
    private CategoryModel categoryModel;
    private MediaPlayer mediaPlayer;
    private Song songToEdit;
    PlaylistController controller1;

    /**
     * Initializes the controller class. Also adds all elementes to the choice
     * box
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { //Initialises the song Modal and category modal
            songModel = new SongModel();
            categoryModel = new CategoryModel();
            List<String> allcategories = categoryModel.getAllCategories(); //Gets all categories
            for (String allcategory : allcategories) { //Adds all categories to choice box
                categoryChoice.getItems().add(allcategory);
            }
        } catch (IOException ex) {
            errorLabel.setText("Error: Cannot load song database");
        }
    }

    /*
    Uses JfileChooser class to choose file for the song.
     */
    @FXML
    private void chooseURL(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop")); //Sets the directory to the desktop
        fileChooser.setTitle("Select song");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            urlField.setText(selectedFile.getAbsolutePath());
            mediaPlayer = new MediaPlayer(new Media(new File(selectedFile.getAbsolutePath()).toURI().toString())); // Sets up the media object in order to get time of the song
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

    @FXML
    private void createCategory(ActionEvent event) throws IOException {
        Parent root1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunesassigment/gui/view/popupCategory.fxml"));
        root1 = (Parent) fxmlLoader.load();
        fxmlLoader.<PopupCategoryController>getController().setController(this); //Sets controler by default for both creating and editing playlists
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 800, 800));
        stage.centerOnScreen();
        stage.show();
    }

    void updateCategory(String name, boolean isAdding) {
        if (isAdding) {
            categoryChoice.getItems().add(name); //adds the name to current category box
        } else {
            categoryChoice.getItems().remove(name); //removes the name from current category box
        }
    }

}
