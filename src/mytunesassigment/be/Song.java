/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.be;

/**
 *
 * @author nedas
 */
public class Song {

    private String title;
    private String artist;
    private String category;
    private double playtime;
    private String location;

    public Song(String title, String artist, String category, double playtime, String location) {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.playtime = playtime;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPlaytime() {
        return playtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return title + " made by : " + artist ;
    }
}
