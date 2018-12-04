/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.gui.model;

import java.io.IOException;
import java.util.List;
import mytunesassigment.bll.LogicFacade;
import mytunesassigment.bll.Manager;

/**
 *
 * @author nedas
 */
public class CategoryModel {

    private final LogicFacade logiclayer;

    /*
    Initialises the logic layer manager
     */
    public CategoryModel() throws IOException {
        logiclayer = new Manager();
    }

    /*
    Gets all categories from database and then returns a string list of all categories
     */
    public List<String> getAllCategories() {
        return logiclayer.getAllCategories();
    }

    /*
    Creates a category with given name
     */
    public void createCategory(String name) {
        logiclayer.createCategory(name);
    }

    /*
    Deletes a category with given name
     */
    public void deleteCategory(String name) {
        logiclayer.deleteCategory(name);
    }
}
