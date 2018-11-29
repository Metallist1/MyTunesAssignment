/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunesassigment.be.Playlist;
import mytunesassigment.gui.model.PlaylistModel;

/**
 * FXML Controller class
 *
 * @author nedas
 */
public class PopupPlaylistController implements Initializable {

    @FXML
    private TextField playlistNameField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label specificFunctionLabel;

    private PlaylistModel playlistModel;
    private boolean isEditing = false;
    private Playlist editingList;
    PlaylistController controller1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            playlistModel = new PlaylistModel();
        } catch (IOException ex) {
            errorLabel.setText("Error : Cannot access playlist database");
        }
    }

    /*
    Takes all information submited and saves it to the database
     */
    @FXML
    private void savePlaylistname(ActionEvent event) {
        String name = playlistNameField.getText().trim(); //Eliminates all white spaces (fron and back of the string)
        if (name != null && name.length() > 0 && name.length() < 50) { //If the string is not null and doesnt excede the databases char length
            if (!isEditing) {
                playlistModel.createPlaylist(name);
                errorLabel.setText("Success: Successfully created the playlist");
            } else {
                playlistModel.editPlaylist(editingList, name);
                errorLabel.setText("Success: Successfully renamed the playlist");
            }
        } else {
            errorLabel.setText("Error : Check if the name you inserted is valid");
        }
        controller1.refreshList(); // Refreshes the list on the main window to reflect changes
    }

    /*
    Closes the scene
     */
    @FXML
    private void goBackFromPlaylist(ActionEvent event) {
        Stage stage = (Stage) playlistNameField.getScene().getWindow();
        stage.close();
    }

    /*
    Specifies that the method will edit the playlist and insert previous name into the text box
     */
    void setInfo(Playlist selectedItem) {
        isEditing = true;
        editingList = selectedItem;
        playlistNameField.setText(selectedItem.getName());
    }

    /*
    Sets up the controller of the main window
     */
    void setController(PlaylistController controller1) {
        this.controller1 = controller1;
        if (isEditing) {
            specificFunctionLabel.setText("Editing Playlist");
        } else {
            specificFunctionLabel.setText("Create Playlist");
        }
    }

}
