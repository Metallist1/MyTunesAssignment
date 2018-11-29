/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.be;

import java.util.List;

/**
 *
 * @author nedas
 */
public class Playlist {

    private List<Song> songList; //List of songs in the playlist
    private int songCount; // Total count of songs in the playlist
    private int totalTime; // Total time of all songs in the playlist
    private final String totalTimeString; // Total time but in an hour:minute:second format as a string
    private String name; // Name of playlist
    private final int ID; // Unique playlist ID

    public Playlist(int songCount, int totalTime, String name, int ID) {
        this.songCount = songCount;
        this.totalTime = totalTime;
        totalTimeString = getTotalTimeString();
        this.name = name;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " Name=" + name + "Total song count =" + songCount + ", Total play Time=" + totalTime;
    }

    /*
    Converts seconds to hours, minute and seconds and puts it as a string
     */
    public String getTotalTimeString() {
        String minutesString;
        String secondString;
        int hours = totalTime / 3600;
        int minutes = (totalTime % 3600) / 60;
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }
        int seconds = totalTime % 60;
        if (10 > seconds) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        return hours + ":" + minutesString + ":" + secondString;
    }
}
