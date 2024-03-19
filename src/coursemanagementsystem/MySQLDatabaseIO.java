/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coursemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mateus
 */
public class MySQLDatabaseIO implements DatabaseIO {
    private String url = "jdbc:mysql://localhost";
    private String username = "pooa2024";
    private String password = "pooa2024";
    
    public MySQLDatabaseIO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    @Override
    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
