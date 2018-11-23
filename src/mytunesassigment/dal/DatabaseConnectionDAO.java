/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nedas
 */

public class DatabaseConnectionDAO {
    
    /*
    The basic idea behind this is that you get your login info from a file and send an array back to your DAO classes .
    This prevents the user from retyping code .
    And its kinda safe. But be smart and when you upload to github either modify the ignore file to actually account for the login info txt file
    Or type out the login info from the TXT file.
    */
        public List<String> getDatabaseInfo() throws IOException {
        List<String> info = new ArrayList();
        String source = "data/loginDetails.txt";
        File file = new File(source);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        info.add(line);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        return info;
    }
}
