/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mytunesassigment.be.Playlist;
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public class PlaylistSongDAO {

    SQLServerDataSource ds;

    /*
    Initialises the constructor. Gets the array from the DatabaseConnectionDAO and sets up the database so the class can use it.
     */
    public PlaylistSongDAO() throws IOException {
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
    Gets a joint playlist query. Which is used to create a playlist list of songs.
     */
    public List<Song> getPlaylistSongs(int id) {
        List<Song> newSongList = new ArrayList();
        try (Connection con = ds.getConnection()) {
            String query = "SELECT * FROM PlaylistSong INNER JOIN Song ON PlaylistSong.SongID = Song.id WHERE PlaylistSong.PlaylistID = ? ORDER by locationInListID desc"; // Gets all songs from a coresponding playlist.
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                Song son = new Song(rs.getString("name"), rs.getString("artist"), rs.getString("category"), rs.getInt("time"), rs.getString("url"), rs.getInt("id")); // Sets up a song object
                son.setLocationInList(rs.getInt("locationInListID")); //Inserts location in list int. It is used for updating possition on the ist
                newSongList.add(son); //adds song to a song array
            }
            return newSongList;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Removes a specific song from every playlist in the Playlist Song database table. (So the song can be removed from the song database table)
     */
    public void deleteFromPlaylistSongsEverything(Song songToDelete) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from PlaylistSong WHERE SongID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, songToDelete.getID());
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /*
    Adds song to playlist 
     */
    public Song addToPlaylist(Playlist playlist, Song song) {
        String sql = "INSERT INTO PlaylistSong(PlaylistID,SongID,locationInListID) VALUES (?,?,?)";
        int Id = -1;
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            Id = getNewestSongInPlaylist(playlist.getID()) + 1;
            ps.setInt(1, playlist.getID());
            ps.setInt(2, song.getID());
            ps.setInt(3, Id);
            ps.addBatch();
            ps.executeBatch();
            song.setLocationInList(Id); //sets up new location in list for user to see
            return song; //Returns song object
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;

        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Gets newest id inserted into a specific playlist
     */
    private int getNewestSongInPlaylist(int id) {
        int newestID = -1;
        try (Connection con = ds.getConnection()) {
            String query = "SELECT TOP(1) * FROM PlaylistSong WHERE PlaylistID = ? ORDER by locationInListID desc";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("locationInListID");
            }
            System.out.println(newestID);
            return newestID;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return newestID;
        } catch (SQLException ex) {
            System.out.println(ex);
            return newestID;
        }
    }

    /*
    Deletes playlist from the Playlist song table in database. (It allows playlist to be deleted from playlist table)
     */
    public void deleteFromPlaylistSongsEverything(Playlist play) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from PlaylistSong WHERE PlaylistID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, play.getID());
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /*
    Switches the song positions in the list but using batch proccesses.
     */
    public void editSongPosition(Playlist selectedItem, Song selected, Song exhangeWith) {
        try (Connection con = ds.getConnection()) {
            String query = "UPDATE PlaylistSong set locationInListID = ? WHERE PlaylistID = ? AND SongID = ? AND locationInListID = ? ";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, exhangeWith.getLocationInList());
            preparedStmt.setInt(2, selectedItem.getID());
            preparedStmt.setInt(3, selected.getID());
            preparedStmt.setInt(4, selected.getLocationInList());
            preparedStmt.addBatch();
            preparedStmt.setInt(1, selected.getLocationInList());
            preparedStmt.setInt(2, selectedItem.getID());
            preparedStmt.setInt(3, exhangeWith.getID());
            preparedStmt.setInt(4, exhangeWith.getLocationInList());
            preparedStmt.addBatch();
            preparedStmt.executeBatch();
            int temp = selected.getLocationInList(); // Creates a temporary ID
            selected.setLocationInList(exhangeWith.getLocationInList()); // switches the first song with exchange song ID
            exhangeWith.setLocationInList(temp); // Switches exchange song ID with the temporary ID
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /*
    Removes a specific song from playlist.
     */
    public void removeSongFromPlaylist(Playlist selectedItem, Song selectedSong) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from PlaylistSong WHERE PlaylistID = ? AND SongID = ? AND locationInListID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, selectedItem.getID());
            preparedStmt.setInt(2, selectedSong.getID());
            preparedStmt.setInt(3, selectedSong.getLocationInList());
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
