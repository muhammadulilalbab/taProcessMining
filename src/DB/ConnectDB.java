/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class ConnectDB {
    public Connection koneksi;
    
   
    public void getKoneksi(){
        String pesan = "";
        try {
            try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
			}
            try {
                String url = "jdbc:mysql://localhost:3306/ta";
                koneksi = DriverManager.getConnection(url, "root", "");
            } catch (SQLException se) {
                pesan = "Koneksi Gagal :( Error : " + se;
                JOptionPane.showMessageDialog(null, pesan,
                        "Kesalahan", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (ClassNotFoundException cnfe) {
            pesan = "Class tidak ditemukan. Error : " + cnfe;
            JOptionPane.showMessageDialog(null, pesan,
                    "Kesalahan", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
