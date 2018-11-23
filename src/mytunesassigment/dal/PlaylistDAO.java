/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.IOException;
import java.util.List;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public class PlaylistDAO {

    SQLServerDataSource ds;

    /*
    In the constructor I initialise the database. Get the database info array from DatabaseConnectio DAO
    Then I insert data into the database and now I have a perminent connection to the database when ever I need to use it.
    Pros: You dont need to retype code . 
    Cons: None i can find.
     */
    public PlaylistDAO() throws IOException {
        this.ds = new SQLServerDataSource();
        DatabaseConnectionDAO connectionInfo = new DatabaseConnectionDAO();
        List<String> infoList = connectionInfo.getDatabaseInfo();
        ds.setDatabaseName(infoList.get(0));
        ds.setUser(infoList.get(1));
        ds.setPassword(infoList.get(2));
        ds.setPortNumber(Integer.parseInt(infoList.get(3)));
        ds.setServerName(infoList.get(4));
    }

    /*
        This class should get all the playlists which would be created as a playlist objects
    To create a playlist object you would need a List of songs (List<Song>) , total count of songs , total time of songs , current song (aka first song)
     */

    public List<Playlist> getAllPlaylists() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deletePlaylist(Playlist play) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Playlist createPlaylist(List<Song> songList, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Playlist updatePlaylist(List<Song> songList, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
