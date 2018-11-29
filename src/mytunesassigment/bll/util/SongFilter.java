/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.bll.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public class SongFilter {

    private ObservableList<Song> temp = FXCollections.observableArrayList();

    /*
    Searches trough all song titles and gets all items that start with specifies string
     */
    public ObservableList<Song> search(ObservableList<Song> items, String text) {
        temp.clear();
        for (Song item : items) {
            if (item.getTitle().toLowerCase().startsWith(text.toLowerCase())) {
                temp.add(item);
            }
        }
        return temp;
    }

}
