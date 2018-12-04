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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunesassigment.gui.model.CategoryModel;

/**
 * FXML Controller class
 *
 * @author nedas
 */
public class PopupCategoryController implements Initializable {

    @FXML
    private Label specificFunctionLabel;
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> categoryChoice;
    @FXML
    private Label errorLabel;

    private CategoryModel categoryModel;
    PopupSongController controller1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { //Initialises the song Modal and category modal
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
    Closes the window.
     */
    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    /*
    On click. Checks if the name is valid and then creates a caregory.
     */
    @FXML
    private void createCategory(ActionEvent event) {
        String name = nameField.getText().trim();
        if (name != null && name.length() > 0 && name.length() < 50 && !categoryChoice.getItems().contains(name)) { // If name is typed and no dublicate names exist
            categoryModel.createCategory(name);
            errorLabel.setText("Success: Category " + name + " has been created succesfully");
            categoryChoice.getItems().add(name); //adds the name to current category box
            controller1.updateCategory(name, true); // updates the popupSong window category box
        } else {
            errorLabel.setText("Error: Please check if typed the name of the category correctly");
        }
    }

    /*
    On click . Checks if there is a category selected and if is selected. Deletes it.
     */
    @FXML
    private void deleteCategory(ActionEvent event) {
        String name = categoryChoice.getSelectionModel().getSelectedItem();
        if (name != null && name.length() > 0) {
            categoryModel.deleteCategory(name);
            errorLabel.setText("Success: Category " + name + " has been deleted succesfully");
            categoryChoice.getItems().remove(name); //deletes the name from current category box
            controller1.updateCategory(name, false); // updates the popupSong window category box
        } else {
            errorLabel.setText("Error: Please check if you selected a category");
        }

    }

    /*
    Sets up the previous window controller. So it would be possible to update values inside the previous window.
     */
    void setController(PopupSongController controller1) {
        this.controller1 = controller1;
    }

}
