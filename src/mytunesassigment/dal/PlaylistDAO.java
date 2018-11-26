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
import java.sql.Statement;
import java.util.ArrayList;
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
    To create a playlist object you would need a List of songs (List<Song>) , total count of songs , total time of songs , 
    current song (aka first song)
     */
    public List<Playlist> getAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>();

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Playlist";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                List<Song> allSongs = getPlaylistSongs(id);
                Playlist pl = new Playlist(allSongs.size(), countTotalTime(allSongs), name, id);
                pl.setSongList(allSongs);
                allPlaylists.add(pl);
            }
            return allPlaylists;

        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    private int countTotalTime(List<Song> allSongs) {
        int totalTime = 0;
        for (Song allSong : allSongs) {
            totalTime += allSong.getPlaytime();
        }
        return totalTime;

    }

    private List<Song> getPlaylistSongs(int id) {
        List<Song> newSongList = new ArrayList();
        try (Connection con = ds.getConnection()) {
            String query = "SELECT * FROM PlaylistSong INNER JOIN Song ON PlaylistSong.SongID = Song.id WHERE PlaylistSong.PlaylistID = ? ORDER by locationInListID desc";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                Song son = new Song(rs.getString("name"), rs.getString("artist"), rs.getString("category"), rs.getInt("time"), rs.getString("url"), rs.getInt("id"));
                son.setLocationInList(rs.getInt("locationInListID"));
                newSongList.add(son);
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

    public void deletePlaylist(Playlist play) {
        try (Connection con = ds.getConnection()) {
            deleteFromPlaylistSongsEverything(play);
            String query = "DELETE from Playlist WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, play.getID());

            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    public Playlist createPlaylist(String name) {
        String sql = "INSERT INTO Playlist(name) VALUES (?)";
        int Id = -1;
        try (Connection con = ds.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.addBatch();

            ps.executeBatch();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        Playlist playlist = new Playlist(0, 0, name, getNewestPlaylist());
        return playlist;
    }

    public void updatePlaylist(Playlist selectedItem, String name) {
        try (Connection con = ds.getConnection()) {

            String query = "UPDATE Playlist set name = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setInt(2, selectedItem.getID());
            preparedStmt.executeUpdate();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    private int getNewestPlaylist() {
        int newestID = -1;
        try (Connection con = ds.getConnection()) {
            String query = "SELECT TOP(1) * FROM Playlist ORDER by id desc";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("id");
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

    public Song addToPlaylist(Playlist playlist, Song song) {
        System.out.println(song);
        String sql = "INSERT INTO PlaylistSong(PlaylistID,SongID,locationInListID) VALUES (?,?,?)";
        int Id = -1;
        try (Connection con = ds.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlist.getID());
            ps.setInt(2, song.getID());
            ps.setInt(3, getNewestSongInPlaylist(playlist.getID()) + 1);
            ps.addBatch();

            ps.executeBatch();
            return song;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;

        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

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

    private void deleteFromPlaylistSongsEverything(Playlist play) {
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
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }
}
