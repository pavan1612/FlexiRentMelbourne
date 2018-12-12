package FlexiRent.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import FlexiRent.Exception.AlertException;
import javafx.stage.Stage;
 

public class DBConnection implements DBPath {
	/**

	* The class helps to connect to FlexiDB database

	* @author Pavan Kumar Bore Gowda

	*/
    public static void connect() throws AlertException {
        Connection conn = null;
        try {
            
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     * @throws AlertException 
     */
    public static void main(String[] args) throws AlertException {
        connect();
    }
}