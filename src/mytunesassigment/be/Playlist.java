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

    private List<Song> songList;
    private int songCount;
    private double totalTime;
    private String currentSong;
    private String name;

    public Playlist(List<Song> songList, int songCount, double totalTime, String currentSong, String name) {
        this.songList = songList;
        this.songCount = songCount;
        this.totalTime = totalTime;
        this.currentSong = currentSong;
        this.name = name;
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

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getCurrentSong() {
        return currentSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
    }

}
