/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private boolean isEditing = false;
    private SongModel songModel;
    private MediaPlayer mediaPlayer;
    private Song songToEdit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryChoice.getItems().add("Pop");
        try {
            songModel = new SongModel();
        } catch (IOException ex) {
            Logger.getLogger(PopupSongController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void chooseURL(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select song database");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(true);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            urlField.setText(chooser.getSelectedFile().getAbsolutePath());
            mediaPlayer = new MediaPlayer(new Media(new File(chooser.getSelectedFile().getAbsolutePath()).toURI().toString()));
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    timeField.setText("" + mediaPlayer.getMedia().getDuration().toSeconds());
                }
            });
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) urlField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveSong(ActionEvent event) {
        if (!isEditing) {
            isEditing = false;
            Double d = new Double(mediaPlayer.getMedia().getDuration().toSeconds());
            int i = d.intValue();
            songModel.createSong(nameField.getText(), artistField.getText(), categoryChoice.getSelectionModel().getSelectedItem(), i, urlField.getText());
        } else {
            Double d = new Double(mediaPlayer.getMedia().getDuration().toSeconds());
            int i = d.intValue();
            songModel.updateSong(songToEdit, nameField.getText(), artistField.getText(), categoryChoice.getSelectionModel().getSelectedItem(), i, urlField.getText());
        }
    }

    void setInfo(Song selectedItem) {
        isEditing = true;
        songToEdit = selectedItem;
        nameField.setText(selectedItem.getTitle());
        artistField.setText(selectedItem.getArtist());
        urlField.setText(selectedItem.getLocation());
        if (selectedItem.getCategory().equals("")) {
            categoryChoice.setValue(selectedItem.getCategory());
        } else {
            categoryChoice.setValue("");
        }
        mediaPlayer = new MediaPlayer(new Media(new File(selectedItem.getLocation()).toURI().toString()));
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                timeField.setText("" + mediaPlayer.getMedia().getDuration().toSeconds());
            }
        });
    }

}
